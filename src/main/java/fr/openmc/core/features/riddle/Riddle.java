package fr.openmc.core.features.riddle;

import java.util.List;

public record Riddle(String question, List<String> answers) {
    public boolean accepts(String answer) {
        if (answer == null) {
            return false;
        }

        String normalizedAnswer = answer.trim();
        return answers.stream().anyMatch(expected -> expected.equalsIgnoreCase(normalizedAnswer));
    }

    public String primaryAnswer() {
        return answers.getFirst();
    }
}
