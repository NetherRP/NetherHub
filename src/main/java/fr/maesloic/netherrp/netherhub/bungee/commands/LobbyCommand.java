package fr.maesloic.netherrp.netherhub.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.maesloic.netherrp.netherhub.bungee.Main;
import fr.maesloic.netherrp.netherhub.commons.builders.components.TextBuilder;
import net.kyori.adventure.text.ComponentBuilder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class LobbyCommand extends Command {
    private final Main main;

    public LobbyCommand(final @NotNull Main main) {
        super("lobby", "netherhub.commands.lobby", "hub");
        this.main = main;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof final ProxiedPlayer player)) return;
        if (player.getServer().getInfo().getName().equals("lobby")) return;
        player.connect(this.main.getProxy().getServerInfo("lobby"));
    }
}
