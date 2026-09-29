package fr.openmc.core.features.riddle;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public final class RiddleStorage {
    private static final String DATE_PATH = "date";
    private static final String QUESTION_PATH = "question";
    private static final String ANSWER_PATH = "answer";

    private final File file;

    public RiddleStorage(File dataFolder) {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Impossible de créer le dossier des données de l'énigme.");
        }

        this.file = new File(dataFolder, "daily-riddle.yml");
    }

    public Optional<Riddle> load(LocalDate date) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!date.toString().equals(config.getString(DATE_PATH))) {
            return Optional.empty();
        }

        String question = config.getString(QUESTION_PATH);
        String answer = config.getString(ANSWER_PATH);

        if (question == null || question.isBlank() || answer == null || answer.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(new Riddle(question, java.util.List.of(answer)));
    }

    public void save(LocalDate date, Riddle riddle) {
        YamlConfiguration config = new YamlConfiguration();
        config.set(DATE_PATH, date.toString());
        config.set(QUESTION_PATH, riddle.question());
        config.set(ANSWER_PATH, riddle.primaryAnswer());

        try {
            config.save(file);
        } catch (IOException exception) {
            throw new IllegalStateException("Impossible de sauvegarder l'énigme quotidienne.", exception);
        }
    }
}
