package fr.openmc.core.features.riddle;

import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class RiddleSessionManager {
    private final Map<UUID, RiddleSession> activeSessions = new HashMap<>();
    private final Set<UUID> loadingPlayers = new HashSet<>();

    public boolean beginLoading(Player player) {
        return loadingPlayers.add(player.getUniqueId());
    }

    public void finishLoading(Player player) {
        loadingPlayers.remove(player.getUniqueId());
    }

    public boolean isLoading(Player player) {
        return loadingPlayers.contains(player.getUniqueId());
    }

    public void start(Player player, LocalDate date, Riddle riddle) {
        activeSessions.put(player.getUniqueId(), new RiddleSession(date, riddle));
    }

    public RiddleSession get(Player player) {
        return activeSessions.get(player.getUniqueId());
    }

    public RiddleSession remove(Player player) {
        return activeSessions.remove(player.getUniqueId());
    }

    public void remove(UUID playerId) {
        activeSessions.remove(playerId);
        loadingPlayers.remove(playerId);
    }
}
