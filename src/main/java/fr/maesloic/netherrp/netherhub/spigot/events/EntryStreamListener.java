package fr.maesloic.netherrp.netherhub.spigot.events;

import fr.maesloic.netherrp.netherhub.spigot.plugin.NetherHub;
import fr.maesloic.netherrp.netherhub.spigot.plugin.Settings;
import fr.maesloic.netherrp.netherhub.commons.builders.components.TextBuilder;
import fr.maesloic.netherrp.netherhub.spigot.utils.builders.items.ItemBuilder;
import fr.maesloic.netherrp.netherhub.spigot.utils.builders.items.SkullBuilder;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class EntryStreamListener implements Listener {
    // FIELDS
    private final NetherHub plugin;

    // CONSTRUCTORS
    public EntryStreamListener(final @NotNull NetherHub hub) {
        this.plugin = hub;
    }

    // EVENTS
    @EventHandler
    public final void join(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if ((boolean) Objects.requireNonNull(Settings.TP_TO_SPAWN.value())) player.teleport(this.plugin.spawn());
        if (Settings.INTRO_SOUND.isSet())
            player.playSound(player.getLocation(), Sound.valueOf((String) Settings.INTRO_SOUND.value()), 1f, 1f);
        if (Settings.INTRO_TITLE.isSet())
            player.showTitle(Title.title(
                    new TextBuilder((String) Objects.requireNonNull(Settings.INTRO_TITLE.value())).build(),
                    new TextBuilder(Settings.INTRO_SUBTITLE.isSet() ? (String) Objects.requireNonNull(Settings.INTRO_SUBTITLE.value()) : "").build(),
                    Title.Times.times(
                            Duration.ofMillis(Settings.INTRO_TITLE_FADE_IN.isSet() ? Long.parseLong(Integer.toString((int) Objects.requireNonNull(Settings.INTRO_TITLE_FADE_IN.value()))) : 200L),
                            Duration.ofMillis(Settings.INTRO_TITLE_PERSISTENCE.isSet() ? Long.parseLong(Integer.toString((int) Objects.requireNonNull(Settings.INTRO_TITLE_PERSISTENCE.value()))) : 1000L),
                            Duration.ofMillis(Settings.INTRO_TITLE_FADE_OUT.isSet() ? Long.parseLong(Integer.toString((int) Objects.requireNonNull(Settings.INTRO_TITLE_FADE_OUT.value()))) : 200L))));
        if (Settings.INTRO_PRIVATE_MESSAGE.isSet())
            player.sendMessage(new TextBuilder((String) Objects.requireNonNull(Settings.INTRO_PRIVATE_MESSAGE.value())).build());

        setupInventory(player);
    }

    public static void setupInventory(final @NotNull Player player) {
        final ItemBuilder redPane    = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).displayName(null);
        final ItemBuilder orangePane = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).displayName(null);
        final ItemBuilder yellowPane = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).displayName(null);
        final SkullBuilder head      = (SkullBuilder) new SkullBuilder().displayName(Settings.TP_ITEM_NAME.isSet() ? new TextBuilder((String) Objects.requireNonNull(Settings.TP_ITEM_NAME.value())).clearItalic().build() : null);

        if (Settings.TP_ITEM_TEXTURE.isSet()) head.customTexture((String) Objects.requireNonNull(Settings.TP_ITEM_TEXTURE.value()));
        else head.owner(player);

        player.getInventory().clear();

        player.getInventory().setItem(0, redPane.build());
        player.getInventory().setItem(1, orangePane.build());
        player.getInventory().setItem(2, yellowPane.build());
        player.getInventory().setItem(4, head.build());
        player.getInventory().setItem(6, yellowPane.build());
        player.getInventory().setItem(7, orangePane.build());
        player.getInventory().setItem(8, redPane.build());

        player.updateInventory();
    }
}
