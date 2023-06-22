package fr.maesloic.netherrp.netherhub.spigot.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.maesloic.netherrp.netherhub.commons.builders.components.TextBuilder;
import fr.maesloic.netherrp.netherhub.spigot.plugin.NetherHub;
import fr.maesloic.netherrp.netherhub.spigot.plugin.Settings;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class PreventionListener implements Listener {
    private final NetherHub plugin;

    public PreventionListener(final @NotNull NetherHub plugin) {
        this.plugin = plugin;
    }

    // DAMAGES
    @EventHandler
    public final void damage(final @NotNull EntityDamageEvent event) {
        final Entity entity                       = event.getEntity();
        final EntityDamageEvent.DamageCause cause = event.getCause();

        event.setCancelled(switch (cause) {
            case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> this.isPrevented(entity, "damages.explosion", !(boolean) Objects.requireNonNull(Settings.EXPLOSION_DAMAGES.value()));
            case FALL -> this.isPrevented(entity, "damages.fall", !(boolean) Objects.requireNonNull(Settings.FALL_DAMAGES.value()));
            case FIRE, FIRE_TICK -> this.isPrevented(entity, "damages.fire", !(boolean) Objects.requireNonNull(Settings.FIRE_DAMAGES.value()));
            case DROWNING -> this.isPrevented(entity, "damages.drowning", !(boolean) Objects.requireNonNull(Settings.DROWNING_DAMAGES.value()));
            default -> false;
        });
    }

    @EventHandler
    public final void damage(final @NotNull EntityDamageByEntityEvent event) {
        final Entity victim  = event.getEntity();
        final Entity damager = event.getDamager();

        event.setCancelled(
                this.isPrevented(damager, "damages.pvp", victim instanceof Player && damager instanceof Player && !(boolean) Objects.requireNonNull(Settings.PVP.value())) ||
                this.isPrevented(damager, "damages.pve", damager instanceof Player && !(victim instanceof Player) && !(boolean) Objects.requireNonNull(Settings.PVE.value()))
        );
    }

    // Y LIMIT
    @EventHandler
    public final void move(final @NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (this.isPrevented(player, "world.v_limit", Settings.VERTICAL_LIMIT.isSet() && event.getTo().getY() < (int) Objects.requireNonNull(Settings.VERTICAL_LIMIT.value()))) player.teleport(this.plugin.spawn());
    }

    // PLAYER
    @EventHandler
    public final void foodLoss(final @NotNull FoodLevelChangeEvent event) {
        event.setCancelled(this.isPrevented(event.getEntity(), "player.food_loss", !(boolean) Objects.requireNonNull(Settings.FOOD_LEVEL_CHANGE.value())));
    }

    @EventHandler
    public final void bubble(final @NotNull EntityAirChangeEvent event) {
        event.setCancelled(this.isPrevented(event.getEntity(), "player.bubble_loss", event.getEntity() instanceof Player && !(boolean) Objects.requireNonNull(Settings.AIR_LEVEL_CHANGE.value())));
    }

    // BUILD
    @EventHandler
    public final void placeBlock(final @NotNull BlockPlaceEvent event) {
        event.setCancelled(this.isPrevented(event.getPlayer(), "build.place", !(boolean) Objects.requireNonNull(Settings.PLACE_BLOCK.value())));
    }

    @EventHandler
    public final void placeBreak(final @NotNull BlockBreakEvent event) {
        event.setCancelled(this.isPrevented(event.getPlayer(), "build.break", !(boolean) Objects.requireNonNull(Settings.BREAK_BLOCK.value())));
    }

    // INTERACTIONS
    // > World
    @Deprecated
    @EventHandler
    public final void playerInteract(final @NotNull PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        final Player player  = event.getPlayer();
        final Action action  = event.getAction();
        if (event.isCancelled()) return;

        if (Objects.nonNull(item) && item.getType().equals(Material.PLAYER_HEAD)) {
            event.setCancelled(true);

            if (!Settings.GAME_SERVER.isSet()) return;
            player.sendMessage(new TextBuilder("&7[&b&lSERVEUR&7]&a Téléportation en cours...").build());
            final String serverName = (String) Objects.requireNonNull(Settings.GAME_SERVER.value());
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(this.plugin.plugin(), "BungeeCord", out.toByteArray());

            return;
        }

        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) return;
        final Block block = event.getClickedBlock();
        assert Objects.nonNull(block);

        event.setCancelled(
                this.isPrevented(player, "interactions.world.doors", !(boolean) Objects.requireNonNull(Settings.OPEN_DOOR.value()) && block.getType().name().contains("_DOOR")) ||
                this.isPrevented(player, "interactions.world.trapdoors", !(boolean) Objects.requireNonNull(Settings.OPEN_TRAP_DOOR.value()) && block.getType().name().contains("_TRAPDOOR")) ||
                this.isPrevented(player, "interactions.world.fence_gates", !(boolean) Objects.requireNonNull(Settings.OPEN_FENCE_GATE.value()) && block.getType().name().contains("_FENCE_GATE")) ||
                this.isPrevented(player, "interactions.world.buttons", !(boolean) Objects.requireNonNull(Settings.PRESS_BUTTON.value()) && block.getType().name().contains("_BUTTON")) ||
                this.isPrevented(player, "interactions.world.levers", !(boolean) Objects.requireNonNull(Settings.TOGGLE_LEVER.value()) && block.getType().equals(Material.LEVER)) ||
                this.isPrevented(player, "interactions.world.pressure_plates", !(boolean) Objects.requireNonNull(Settings.ACTIVATE_PRESSURE_PLATE.value()) && block.getType().name().contains("_PRESSURE_PLATE"))
        );
    }

    // > Inventory
    @EventHandler
    public final void inventoryClick(final @NotNull InventoryClickEvent event) {
        event.setCancelled(this.isPrevented(event.getWhoClicked(), "interactions.inventory.click", !(boolean) Objects.requireNonNull(Settings.INVENTORY_CLICK.value())));
    }

    @EventHandler
    public final void itemDrop(final @NotNull PlayerDropItemEvent event) {
        event.setCancelled(this.isPrevented(event.getPlayer(), "interactions.inventory.drop", !(boolean) Objects.requireNonNull(Settings.DROP_ITEM.value())));
    }

    @EventHandler
    public final void itemPickup(final @NotNull EntityPickupItemEvent event) {
        event.setCancelled(this.isPrevented(event.getEntity(), "interactions.inventory.pickup", !(boolean) Objects.requireNonNull(Settings.PICKUP_ITEM.value())));
    }

    @EventHandler
    public final void itemSwap(final @NotNull PlayerSwapHandItemsEvent event) {
        event.setCancelled(this.isPrevented(event.getPlayer(), "interactions.inventory.swap", !(boolean) Objects.requireNonNull(Settings.SWAP_HAND.value())));
    }

    private Boolean isPrevented(final @NotNull Entity player, final @NotNull String permission, final Boolean condition) {
        return !player.hasPermission("netherhub.bypass.%s".formatted(permission)) && condition;
    }
}
