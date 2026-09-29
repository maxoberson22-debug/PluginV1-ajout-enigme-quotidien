package fr.openmc.core.features.riddle;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class RiddleManager {
    private final RiddleStorage storage;
    private final RiddleGenerator riddleGenerator;

    private LocalDate cachedDate;
    private Riddle cachedRiddle;
    private LocalDate loadingDate;
    private CompletableFuture<Riddle> loadingFuture;

    public RiddleManager(RiddleStorage storage, RiddleGenerator riddleGenerator) {
        this.storage = storage;
        this.riddleGenerator = riddleGenerator;
    }

    public synchronized CompletableFuture<Riddle> getDailyRiddle() {
        LocalDate today = LocalDate.now();

        if (today.equals(cachedDate) && cachedRiddle != null) {
            return CompletableFuture.completedFuture(cachedRiddle);
        }

        Optional<Riddle> storedRiddle = storage.load(today);
        if (storedRiddle.isPresent()) {
            cachedDate = today;
            cachedRiddle = storedRiddle.get();
            return CompletableFuture.completedFuture(cachedRiddle);
        }

        if (today.equals(loadingDate) && loadingFuture != null) {
            return loadingFuture;
        }

        loadingDate = today;
        loadingFuture = riddleGenerator.generateRiddle(today)
                .thenApply(riddle -> {
                    synchronized (this) {
                        storage.save(today, riddle);
                        cachedDate = today;
                        cachedRiddle = riddle;
                    }
                    return riddle;
                })
                .whenComplete((riddle, error) -> {
                    synchronized (this) {
                        if (today.equals(loadingDate)) {
                            loadingDate = null;
                            loadingFuture = null;
                        }
                    }
                });

        return loadingFuture;
    }
}
