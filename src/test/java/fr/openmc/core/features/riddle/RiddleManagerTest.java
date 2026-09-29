package fr.openmc.core.features.riddle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RiddleManagerTest {
    @Test
    void testDailyRiddleIsGeneratedOnceAndCached(@TempDir Path tempDirectory) {
        AtomicInteger generationCount = new AtomicInteger();
        Riddle generated = new Riddle("Question", List.of("Réponse"));
        RiddleGenerator generator = date -> {
            generationCount.incrementAndGet();
            return CompletableFuture.completedFuture(generated);
        };

        RiddleManager manager = new RiddleManager(
                new RiddleStorage(tempDirectory.toFile()),
                generator
        );

        Riddle first = manager.getDailyRiddle().join();
        Riddle second = manager.getDailyRiddle().join();

        assertSame(generated, first);
        assertSame(first, second);
        assertEquals(1, generationCount.get());
    }

    @Test
    void testConcurrentRequestsShareTheSameGeneration(@TempDir Path tempDirectory) {
        AtomicInteger generationCount = new AtomicInteger();
        CompletableFuture<Riddle> pending = new CompletableFuture<>();
        Riddle generated = new Riddle("Question", java.util.List.of("Réponse"));

        RiddleManager manager = new RiddleManager(
                new RiddleStorage(tempDirectory.toFile()),
                date -> {
                    generationCount.incrementAndGet();
                    return pending;
                }
        );

        CompletableFuture<Riddle> first = manager.getDailyRiddle();
        CompletableFuture<Riddle> second = manager.getDailyRiddle();

        assertSame(first, second);
        assertEquals(1, generationCount.get());

        pending.complete(generated);
        assertSame(generated, first.join());
    }

    @Test
    void testStoredRiddleIsUsedForCurrentDay(@TempDir Path tempDirectory) {
        LocalDate today = LocalDate.now();
        Riddle stored = new Riddle("Question sauvegardée", List.of("réponse"));
        RiddleStorage storage = new RiddleStorage(tempDirectory.toFile());
        storage.save(today, stored);

        AtomicInteger generationCount = new AtomicInteger();
        RiddleManager manager = new RiddleManager(
                storage,
                date -> {
                    generationCount.incrementAndGet();
                    return CompletableFuture.completedFuture(
                            new Riddle("Question générée", List.of("autre"))
                    );
                }
        );

        Riddle result = manager.getDailyRiddle().join();

        assertEquals(stored, result);
        assertEquals(0, generationCount.get());
    }
}
