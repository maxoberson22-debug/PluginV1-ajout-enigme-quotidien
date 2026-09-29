package fr.openmc.core.features.riddle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiddleStorageTest {
    @Test
    void testSaveAndLoad(@TempDir Path tempDirectory) {
        LocalDate date = LocalDate.of(2026, 9, 29);
        Riddle riddle = new Riddle("Question", java.util.List.of("Réponse"));

        RiddleStorage storage = new RiddleStorage(tempDirectory.toFile());
        storage.save(date, riddle);

        assertEquals(java.util.Optional.of(riddle), storage.load(date));
        assertTrue(storage.load(date.plusDays(1)).isEmpty());
    }
}
