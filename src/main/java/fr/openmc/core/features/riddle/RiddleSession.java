package fr.openmc.core.features.riddle;

import java.time.LocalDate;

public record RiddleSession(LocalDate date, Riddle riddle) {
}
