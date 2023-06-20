package fr.maesloic.netherrp.netherhub.bungee;

import fr.maesloic.netherrp.netherhub.bungee.commands.LobbyCommand;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    @Override
    public void onEnable() {
        this.getProxy().getPluginManager().registerCommand(this, new LobbyCommand(this));
    }
}
