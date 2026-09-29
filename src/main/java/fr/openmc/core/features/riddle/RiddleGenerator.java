package fr.openmc.core.features.riddle;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface RiddleGenerator {
    CompletableFuture<Riddle> generateRiddle(LocalDate date);
}
