package fr.openmc.core.features.riddle;

import fr.openmc.core.utils.text.messages.TranslationManager;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public final class RiddleDialog {
    private RiddleDialog() {
    }

    public static void showStart(Player player, Runnable onStart) {
        showNotice(
                player,
                "feature.riddle.dialog.start.title",
                "feature.riddle.dialog.start.body",
                "feature.riddle.dialog.start.button",
                onStart
        );
    }

    public static void showLoading(Player player) {
        showNotice(
                player,
                "feature.riddle.dialog.loading.title",
                "feature.riddle.dialog.loading.body",
                "feature.riddle.dialog.loading.button",
                player::closeDialog
        );
    }

    public static void showChallenge(Player player, Riddle riddle, Consumer<String> onAnswer) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(
                                TranslationManager.translation("feature.riddle.dialog.challenge.title")
                        )
                        .body(List.of(
                                DialogBody.plainMessage(Component.text(riddle.question()), 600),
                                DialogBody.plainMessage(
                                        TranslationManager.translation("feature.riddle.dialog.challenge.time"),
                                        250
                                )
                        ))
                        .inputs(List.of(
                                DialogInput.text(
                                        "answer",
                                        TranslationManager.translation("feature.riddle.dialog.challenge.input")
                                )
                                .maxLength(100)
                                .build()
                        ))
                        .canCloseWithEscape(true)
                        .build()
                )
                .type(DialogType.confirmation(
                        ActionButton.builder(
                                        TranslationManager.translation("feature.riddle.dialog.challenge.button")
                                )
                                .action(DialogAction.customClick((response, audience) -> {
                                    if (audience instanceof Player clicked) {
                                        clicked.closeDialog();
                                        String answer = response.getText("answer");
                                        onAnswer.accept(answer == null ? "" : answer);
                                    }
                                }, ClickCallback.Options.builder().build()))
                                .build()
                ))
        );

        player.showDialog(dialog);
    }

    public static void showResult(Player player, boolean success) {
        String keyPrefix = success
                ? "feature.riddle.dialog.success"
                : "feature.riddle.dialog.lost";

        showNotice(
                player,
                keyPrefix + ".title",
                keyPrefix + ".body",
                "feature.riddle.dialog.result.button",
                player::closeDialog
        );
    }

    public static void showError(Player player) {
        showNotice(
                player,
                "feature.riddle.dialog.error.title",
                "feature.riddle.dialog.error.body",
                "feature.riddle.dialog.result.button",
                player::closeDialog
        );
    }

    private static void showNotice(
            Player player,
            String titleKey,
            String bodyKey,
            String buttonKey,
            Runnable action
    ) {
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(TranslationManager.translation(titleKey))
                        .body(List.of(
                                DialogBody.plainMessage(
                                        TranslationManager.translation(bodyKey),
                                        350
                                )
                        ))
                        .canCloseWithEscape(true)
                        .build()
                )
                .type(DialogType.notice(
                        ActionButton.builder(TranslationManager.translation(buttonKey))
                                .action(DialogAction.customClick((response, audience) -> {
                                    if (audience instanceof Player) {
                                        action.run();
                                    }
                                }, ClickCallback.Options.builder().build()))
                                .build()
                ))
        );

        player.showDialog(dialog);
    }
}
