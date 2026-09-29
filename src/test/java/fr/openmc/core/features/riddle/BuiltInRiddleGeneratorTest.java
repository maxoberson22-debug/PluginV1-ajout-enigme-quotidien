package fr.openmc.core.features.riddle;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltInRiddleGeneratorTest {
    @Test
    void testGeneratesRiddleDeterministicallyForDate() {
        BuiltInRiddleGenerator generator = new BuiltInRiddleGenerator();

        Riddle first = generator.generateRiddle(LocalDate.of(2026, 9, 29)).join();
        Riddle second = generator.generateRiddle(LocalDate.of(2026, 9, 29)).join();

        assertEquals(first, second);
    }

    @Test
    void testGeneratedRiddleContainsQuestionAndAnswer() {
        Riddle riddle = new BuiltInRiddleGenerator()
                .generateRiddle(LocalDate.of(2026, 9, 29))
                .join();

        assertEquals(false, riddle.question().isBlank());
        assertEquals(false, riddle.answers().isEmpty());
    }
}
