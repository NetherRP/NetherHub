package fr.maesloic.netherrp.netherhub.spigot.utils.builders.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SkullBuilder extends ItemBuilder {
    // CONSTRUCTORS
    public SkullBuilder() {
        super(Material.PLAYER_HEAD);
    }

    public SkullBuilder(@NotNull ItemStack item) {
        super(item);
    }

    public SkullBuilder(int amount) {
        super(Material.PLAYER_HEAD, amount);
    }

    // METHODS
    public final @NotNull SkullBuilder apply(final @NotNull ItemMeta meta) {
        super.apply(meta);
        return this;
    }

    // SETTERS
    public final @NotNull SkullBuilder owner(final @NotNull OfflinePlayer player) {
        final SkullMeta meta = this.meta();
        meta.setOwningPlayer(player);
        return this.apply(meta);
    }

    public final @NotNull SkullBuilder customTexture(final @NotNull String texture) {
        final SkullMeta meta = this.meta();

        final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
        profile.setProperty(new ProfileProperty("textures", texture));
        meta.setPlayerProfile(profile);

        return this.apply(meta);
    }

    // GETTERS
    public final @NotNull SkullMeta meta() {
        return (SkullMeta) super.meta();
    }


}
