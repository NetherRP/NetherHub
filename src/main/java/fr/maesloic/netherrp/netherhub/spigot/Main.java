package fr.maesloic.netherrp.netherhub.spigot;

import fr.maesloic.netherrp.netherhub.spigot.plugin.ConsoleLogger;
import fr.maesloic.netherrp.netherhub.spigot.plugin.NetherHub;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Main extends JavaPlugin {
    private final NetherHub plugin = new NetherHub(this);
    private final ConsoleLogger logger = new ConsoleLogger(this.getServer());

    // METHODS
    @Override
    public void onLoad() {
        this.logger.message("Loading...");
        this.plugin.load();
        this.logger.message("Loaded!");
    }

    @Override
    public void onEnable() {
        this.logger.message("Enabling...");
        this.plugin.enable();
        this.logger.message("Enabled!");
    }

    @Override
    public void onDisable() {
        this.logger.message("Disabling...");
        this.plugin.disable();
        this.logger.message("Disabled!");
    }

    // GETTERS
    public final @NotNull ConsoleLogger logger() {
        return this.logger;
    }

    public final @NotNull NetherHub plugin() {
        return this.plugin;
    }
}
