package fr.maesloic.netherrp.netherhub.spigot.plugin;

import fr.maesloic.netherrp.netherhub.commons.builders.components.TextBuilder;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class ConsoleLogger {
    // FIELDS
    private final ConsoleCommandSender logger;

    // CONSTRUCTORS
    public ConsoleLogger(final @NotNull ConsoleCommandSender logger) {
        this.logger = logger;
    }

    public ConsoleLogger(final @NotNull Server server) {
        this.logger = server.getConsoleSender();
    }

    // METHODS
    public final void message(final @NotNull String message) {
        this.logger.sendMessage(new TextBuilder(message).build());
    }
}
