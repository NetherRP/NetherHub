package fr.maesloic.netherrp.netherhub.spigot.utils.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {
    public abstract @NotNull String name();
    public abstract @NotNull String[] aliases();
    public abstract @NotNull String[] permissions();
    public abstract @Nullable String description();
    public abstract @NotNull List<String> tabComplete(final @NotNull CommandSender sender, final @NotNull String[] strings);
    public abstract boolean process(final @NotNull CommandSender sender, final @NotNull String[] strings);
}
