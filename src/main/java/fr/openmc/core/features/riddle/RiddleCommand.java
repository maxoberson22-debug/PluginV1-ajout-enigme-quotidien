package fr.openmc.core.features.riddle;

import fr.openmc.api.chronometer.Chronometer;
import fr.openmc.api.chronometer.ChronometerType;
import fr.openmc.core.OMCPlugin;
import fr.openmc.core.utils.text.messages.MessageType;
import fr.openmc.core.utils.text.messages.MessagesManager;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.annotation.Description;

import java.time.LocalDate;

public final class RiddleCommand {
    private static final int RIDDLE_DURATION_SECONDS = 30;
    static final NamespacedKey SOLVED_DATE_KEY =
            new NamespacedKey("openmc", "daily_riddle_solved");

    private final RiddleManager riddleManager;
    private final RiddleAiService aiService;
    private final RiddleSessionManager sessionManager;

    public RiddleCommand(
            RiddleManager riddleManager,
            RiddleAiService aiService,
            RiddleSessionManager sessionManager
    ) {
        this.riddleManager = riddleManager;
        this.aiService = aiService;
        this.sessionManager = sessionManager;
    }

    @Command("riddle")
    @CommandPermission("omc.commands.riddle")
    @Description("Ouvre l'énigme quotidienne")
    private void riddle(Player player) {
        if (hasSolvedToday(player)) {
            sendMessage(player, "feature.riddle.already_solved");
            return;
        }

        if (sessionManager.get(player) != null || sessionManager.isLoading(player)) {
            sendMessage(player, "feature.riddle.in_progress");
            return;
        }

        RiddleDialog.showStart(player, () -> startChallenge(player));
    }

    private void startChallenge(Player player) {
        if (hasSolvedToday(player) || !sessionManager.beginLoading(player)) {
            return;
        }

        RiddleDialog.showLoading(player);

        riddleManager.getDailyRiddle().whenComplete((riddle, error) -> {
            Bukkit.getScheduler().runTask(OMCPlugin.getInstance(), () -> {
                sessionManager.finishLoading(player);

                if (!player.isOnline()) {
                    return;
                }

                if (error != null) {
                    OMCPlugin.getInstance().getLogger().warning(
                            "Impossible de générer l'énigme quotidienne: " + error.getMessage()
                    );
                    RiddleDialog.showError(player);
                    return;
                }

                LocalDate today = LocalDate.now();
                sessionManager.start(player, today, riddle);
                RiddleDialog.showChallenge(player, riddle, answer -> submitAnswer(player, answer));

                Chronometer.startChronometer(
                        player,
                        RiddleListener.CHRONOMETER_GROUP,
                        RIDDLE_DURATION_SECONDS,
                        ChronometerType.ACTION_BAR,
                        "feature.riddle.timer",
                        ChronometerType.ACTION_BAR,
                        null
                );
            });
        });
    }

    private void submitAnswer(Player player, String answer) {
        RiddleSession session = sessionManager.remove(player);
        if (session == null) {
            RiddleDialog.showResult(player, false);
            return;
        }

        Chronometer.stopChronometer(
                player,
                RiddleListener.CHRONOMETER_GROUP,
                ChronometerType.ACTION_BAR,
                null
        );

        if (!LocalDate.now().equals(session.date())) {
            RiddleDialog.showResult(player, false);
            return;
        }

        RiddleDialog.showLoading(player);

        aiService.validateAnswer(session.riddle(), answer).whenComplete((correct, error) -> {
            Bukkit.getScheduler().runTask(OMCPlugin.getInstance(), () -> {
                if (!player.isOnline()) {
                    return;
                }

                if (error != null) {
                    OMCPlugin.getInstance().getLogger().warning(
                            "Impossible de valider la réponse à l'énigme quotidienne: " + error.getMessage()
                    );
                    RiddleDialog.showError(player);
                    return;
                }

                if (!Boolean.TRUE.equals(correct)) {
                    RiddleDialog.showResult(player, false);
                    return;
                }

                player.getPersistentDataContainer().set(
                        SOLVED_DATE_KEY,
                        PersistentDataType.STRING,
                        LocalDate.now().toString()
                );
                player.giveExp(10);
                RiddleDialog.showResult(player, true);
            });
        });
    }

    private boolean hasSolvedToday(Player player) {
        String solvedDate = player.getPersistentDataContainer().get(
                SOLVED_DATE_KEY,
                PersistentDataType.STRING
        );
        return LocalDate.now().toString().equals(solvedDate);
    }

    private void sendMessage(Player player, String translationKey) {
        MessagesManager.sendMessage(
                player,
                TranslationManager.translation(translationKey),
                Prefix.OPENMC,
                MessageType.INFO,
                false
        );
    }
}
