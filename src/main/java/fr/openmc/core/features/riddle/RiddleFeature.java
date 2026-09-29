package fr.openmc.core.features.riddle;

import fr.openmc.core.OMCPlugin;
import fr.openmc.core.bootstrap.features.Feature;
import fr.openmc.core.bootstrap.features.types.HasCommands;
import fr.openmc.core.bootstrap.features.types.HasListeners;
import fr.openmc.core.bootstrap.listeners.ListenerFactory;

import java.io.File;
import java.util.Set;

public final class RiddleFeature extends Feature implements HasCommands, HasListeners {
    private final RiddleSessionManager sessionManager;
    private final RiddleAiService aiService;
    private final RiddleManager riddleManager;

    public RiddleFeature() {
        this.sessionManager = new RiddleSessionManager();
        this.aiService = new RiddleAiService();
        this.riddleManager = new RiddleManager(
                new RiddleStorage(new File(OMCPlugin.getInstance().getDataFolder(), "riddle")),
                aiService
        );
    }

    @Override
    public Set<Object> getCommands() {
        return Set.of(new RiddleCommand(riddleManager, aiService, sessionManager));
    }

    @Override
    public Set<ListenerFactory> getListeners() {
        return Set.of(() -> new RiddleListener(sessionManager));
    }
}
