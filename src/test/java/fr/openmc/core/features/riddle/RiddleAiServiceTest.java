package fr.openmc.core.features.riddle;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiddleAiServiceTest {
    @Test
    void testGenerationWorksWithoutOpenAiConfiguration() {
        RiddleAiService service = new RiddleAiService("");

        Riddle riddle = service.generateRiddle(LocalDate.of(2026, 9, 29)).join();

        assertFalse(riddle.question().isBlank());
        assertFalse(riddle.answers().isEmpty());
    }

    @Test
    void testValidationWorksWithoutOpenAiConfiguration() {
        RiddleAiService service = new RiddleAiService("");
        Riddle riddle = new Riddle("Question", java.util.List.of("Réponse"));

        assertTrue(service.validateAnswer(riddle, " réponse ").join());
        assertFalse(service.validateAnswer(riddle, "mauvaise").join());
    }
}
