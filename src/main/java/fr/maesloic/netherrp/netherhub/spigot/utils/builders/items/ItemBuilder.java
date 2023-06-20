package fr.maesloic.netherrp.netherhub.spigot.utils.builders.items;

import fr.maesloic.netherrp.netherhub.spigot.utils.builders.components.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemBuilder implements Cloneable {
    // FIELDS
    private final ItemStack item;

    // CONSTRUCTORS
    public ItemBuilder(final @NotNull ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(final @NotNull Material material, final int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(final @NotNull Material material) {
        this(material, 1);
    }

    // METHODS
    public @NotNull ItemBuilder apply(final @NotNull ItemMeta meta) {
        this.item.setItemMeta(meta);
        return this;
    }

    // SETTERS
    public final @NotNull ItemBuilder displayName(final @Nullable Component component) {
        final ItemMeta meta = this.meta();
        meta.displayName(Objects.isNull(component) ? new TextBuilder("&0").build() : component);
        return this.apply(meta);
    }

    // GETTERS
    public @NotNull ItemMeta meta() {
        return this.item.getItemMeta();
    }

    @Override
    public final ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public final @NotNull ItemStack build() {
        return this.item;
    }
}
