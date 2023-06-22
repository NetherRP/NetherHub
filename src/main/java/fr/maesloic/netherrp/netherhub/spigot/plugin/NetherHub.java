package fr.maesloic.netherrp.netherhub.spigot.plugin;

import fr.maesloic.netherrp.netherhub.spigot.Main;
import fr.maesloic.netherrp.netherhub.spigot.commands.HotBarCommand;
import fr.maesloic.netherrp.netherhub.spigot.events.EntryStreamListener;
import fr.maesloic.netherrp.netherhub.spigot.events.PreventionListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class NetherHub {
    // FIELDS
    private final Main plugin;
    private FileConfiguration config;
    private final String BASE = "rules";
    private Location spawn;

    // CONSTRUCTORS
    public NetherHub(final @NotNull Main plugin) {
        this.plugin = plugin;
    }

    // METHODS
    public final void load() {
        this.plugin.saveDefaultConfig();
        this.config = this.plugin.getConfig();
        if (this.config.getBoolean("rules.features.custom_config.enable")) this.setupCustomSettings();
        else this.setupSettings(this.config.getBoolean("rules.features.custom_config.default_value"));
        Arrays.asList(Settings.values()).forEach(v -> System.out.printf("%s: %s%n", v.toString("rules"), v.value()));

        this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
    }

    private void setupSettings(final boolean value) {
        Arrays.asList(Settings.values()).forEach(v -> {
            if (v.equals(Settings.TP_TO_SPAWN) && !v.exists(BASE, this.config)) v.value(false);
            if (v.parent().equals(Settings.Category.FEATURES) && !v.exists(BASE, this.config)) v.value(value);
            if (!v.exists(BASE, this.config)) v.saveToConfig(BASE, this.config);
            else if (v.parent().equals(Settings.Category.FEATURES)) v.value(value);
            else v.loadFromConfig(BASE, this.config);
        });
        this.plugin.saveConfig();
    }

    private void setupCustomSettings() {
        Arrays.asList(Settings.values()).forEach(v -> {
            if (v.equals(Settings.TP_TO_SPAWN) && !v.exists(BASE, this.config)) v.value(false);
            if (v.parent().equals(Settings.Category.FEATURES) && !v.exists(BASE, this.config)) v.value(true);
            if (!v.exists(BASE, this.config)) v.saveToConfig(BASE, this.config);
            else v.loadFromConfig(BASE, this.config);
        });
        this.plugin.saveConfig();
    }

    public final void enable() {
        // SPAWN LOADING
        this.loadSpawn();
        // COMMANDS
        this.plugin.getCommand("hotbar").setExecutor(new HotBarCommand());
        // EVENTS
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        pm.registerEvents(new EntryStreamListener(this), this.plugin);
        pm.registerEvents(new PreventionListener(this), this.plugin);
    }

    private void loadSpawn() {
        final String world = this.config.getString("spawn.world");
        final double x     = this.config.getDouble("spawn.point.x");
        final double y     = this.config.getDouble("spawn.point.y");
        final double z     = this.config.getDouble("spawn.point.z");
        final float yaw    = (float) this.config.getDouble("spawn.facing.yaw");
        final float pitch  = (float) this.config.getDouble("spawn.facing.pitch");

        this.spawn = new Location(Bukkit.getWorld(Objects.requireNonNull(world)), x, y, z, yaw, pitch);
    }

    public final void disable() {
        Arrays.asList(Settings.values()).forEach(v -> v.saveToConfig(BASE, this.config));
    }

    // SETTERS
    public final void spawn(@Nullable Location location) {
        if (Objects.isNull(location)) location = new Location(Bukkit.getWorlds().get(0), 0, 100, 0, 0F, 0F);
        this.spawn = location;

        this.config.set("spawn.world", location.getWorld().getName());
        this.config.set("spawn.point.x", location.getWorld().getName());
        this.config.set("spawn.point.y", location.getWorld().getName());
        this.config.set("spawn.point.z", location.getWorld().getName());
        this.config.set("spawn.facing.yaw", location.getWorld().getName());
        this.config.set("spawn.facing.pitch", location.getWorld().getName());
        this.plugin.saveConfig();
    }

    // GETTERS
    public final @NotNull Main plugin() {
        return this.plugin;
    }

    public final @NotNull FileConfiguration config() {
        return this.config;
    }

    public final @NotNull Location spawn() {
        return this.spawn;
    }
}
