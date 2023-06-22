package fr.maesloic.netherrp.netherhub.spigot.commands;

import fr.maesloic.netherrp.netherhub.commons.builders.components.TextBuilder;
import fr.maesloic.netherrp.netherhub.spigot.events.EntryStreamListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HotBarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof final Player sender)) return this.sendMessage(commandSender, "&cBe a player is required to process this command!");

        EntryStreamListener.setupInventory(sender);
        return this.sendMessage(sender, "&7[&b&lSERVEUR&7]&a Et voilà, vous êtes comme neuf !");
    }

    private boolean sendMessage(final @NotNull CommandSender commandSender, final @NotNull String message) {
        commandSender.sendMessage(new TextBuilder(message).build());
        return true;
    }
}
