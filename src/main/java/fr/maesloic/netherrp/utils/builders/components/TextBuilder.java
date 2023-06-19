package fr.maesloic.netherrp.utils.builders.components;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class TextBuilder {
    // FIELDS
    private Component component;

    // CONSTRUCTORS
    public TextBuilder(final @NotNull Component component) {
        this.component = component;
    }

    public TextBuilder() {
        this(Component.empty());
    }

    public TextBuilder(final @NotNull String text) {
        this(LegacyComponentSerializer.legacyAmpersand().deserialize(text));
    }

    // GETTERS
    public final Component build() {
        return this.component;
    }
}
