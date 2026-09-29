package fr.openmc.core.features.riddle;

import fr.openmc.api.chronometer.Chronometer;
import fr.openmc.api.chronometer.ChronometerType;
import fr.openmc.core.utils.text.messages.MessageType;
import fr.openmc.core.utils.text.messages.MessagesManager;
import fr.openmc.core.utils.text.messages.Prefix;
import fr.openmc.core.utils.text.messages.TranslationManager;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDate;

public final class RiddleListener implements Listener {
    static final String CHRONOMETER_GROUP = "riddle";

    private final RiddleSessionManager sessionManager;

    public RiddleListener(RiddleSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String solvedDate = player.getPersistentDataContainer().get(
                RiddleCommand.SOLVED_DATE_KEY,
                PersistentDataType.STRING
        );

        if (!LocalDate.now().toString().equals(solvedDate)) {
            MessagesManager.sendMessage(
                    player,
                    TranslationManager.translation("feature.riddle.available")
                            .clickEvent(ClickEvent.runCommand("/riddle")),
                    Prefix.OPENMC,
                    MessageType.INFO,
                    false
            );
        }
    }

    @EventHandler
    public void onChronometerEnd(Chronometer.ChronometerEndEvent event) {
        if (!CHRONOMETER_GROUP.equals(event.getGroup()) || !(event.getEntity() instanceof Player player)) {
            return;
        }

        if (sessionManager.remove(player) != null) {
            RiddleDialog.showResult(player, false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Chronometer.containsChronometer(player.getUniqueId(), CHRONOMETER_GROUP)) {
            Chronometer.stopChronometer(player, CHRONOMETER_GROUP, ChronometerType.ACTION_BAR, null);
        }
        sessionManager.remove(player.getUniqueId());
    }
}
