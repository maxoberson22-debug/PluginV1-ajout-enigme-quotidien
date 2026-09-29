package fr.openmc.core.features.riddle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiddleTest {
    @Test
    void testAcceptsExpectedAnswerIgnoringCaseAndWhitespace() {
        Riddle riddle = new Riddle("Question", List.of("une éponge", "éponge"));

        assertTrue(riddle.accepts(" ÉPONGE "));
    }

    @Test
    void testRejectsNullAnswer() {
        Riddle riddle = new Riddle("Question", List.of("éponge"));

        assertFalse(riddle.accepts(null));
    }

    @Test
    void testRejectsUnexpectedAnswer() {
        Riddle riddle = new Riddle("Question", List.of("éponge"));

        assertFalse(riddle.accepts("serviette"));
    }

    @Test
    void testPrimaryAnswerReturnsFirstConfiguredAnswer() {
        Riddle riddle = new Riddle("Question", List.of("éponge", "une éponge"));

        assertEquals("éponge", riddle.primaryAnswer());
    }
}
