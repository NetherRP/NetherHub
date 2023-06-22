package fr.maesloic.netherrp.netherhub.spigot.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public enum Settings {
    // BUNGEE
    GAME_SERVER(Category.BUNGEE_CORD, null, "server_name"),
    // EVENTS
    TP_TO_SPAWN(Category.EVENTS, "login", "tp_to_spawn"), //
    INTRO_SOUND(Category.EVENTS, "login.intro", "sound"), //
    INTRO_TITLE(Category.EVENTS, "login.intro.title", "value"), //
    INTRO_SUBTITLE(Category.EVENTS, "login.intro.title", "subtitle"), //
    INTRO_TITLE_FADE_IN(Category.EVENTS, "login.intro.title.timings", "fade_in"), //
    INTRO_TITLE_PERSISTENCE(Category.EVENTS, "login.intro.title.timings", "persistence"), //
    INTRO_TITLE_FADE_OUT(Category.EVENTS, "login.intro.title.timings", "fade_out"), //
    INTRO_PRIVATE_MESSAGE(Category.EVENTS, "login.intro", "private_message"),
    TP_ITEM_TEXTURE(Category.EVENTS, "login.item", "texture"), //
    TP_ITEM_NAME(Category.EVENTS, "login.item", "name"), //
    // LOCATIONS
    VERTICAL_LIMIT(Category.LOCATIONS, null, "y_limit"), //
    // FEATURES
    // > Damages
    PVP(Category.FEATURES, "damages", "pvp"), //
    PVE(Category.FEATURES, "damages", "pve"), //
    FALL_DAMAGES(Category.FEATURES, "damages", "fall"), //
    EXPLOSION_DAMAGES(Category.FEATURES, "damages", "explosion"), //
    FIRE_DAMAGES(Category.FEATURES, "damages", "fire"), //
    DROWNING_DAMAGES(Category.FEATURES, "damages", "drowning"), //
    // > Player
    FOOD_LEVEL_CHANGE(Category.FEATURES, "player", "food_loss"), //
    AIR_LEVEL_CHANGE(Category.FEATURES, "player", "bubble_loss"), //
    // > Build
    PLACE_BLOCK(Category.FEATURES, "build", "place"), //
    BREAK_BLOCK(Category.FEATURES, "build", "break"), //
    // > Interactions
    // >> World
    OPEN_DOOR(Category.FEATURES, "interactions.world", "doors"), //
    OPEN_TRAP_DOOR(Category.FEATURES, "interactions.world", "trap_doors"), //
    OPEN_FENCE_GATE(Category.FEATURES, "interactions.world", "fence_gates"), //
    PRESS_BUTTON(Category.FEATURES, "interactions.world", "buttons"), //
    TOGGLE_LEVER(Category.FEATURES, "interactions.world", "levers"), //
    ACTIVATE_PRESSURE_PLATE(Category.FEATURES, "interactions.world", "pressure_plates"), //
    UPDATE_SIGN(Category.FEATURES, "interactions.world", "signs"), //
    // >> Inventory
    INVENTORY_CLICK(Category.FEATURES, "interactions.inventory", "click"), //
    DROP_ITEM(Category.FEATURES, "interactions.inventory", "drop"), //
    SWAP_HAND(Category.FEATURES, "interactions.inventory", "swap"), //
    PICKUP_ITEM(Category.FEATURES, "interactions.inventory", "pickup"); //

    // FIELDS
    private final Category parent;
    private final String category, ymlValue;
    private Object value = null;

    // CONSTRUCTORS
    Settings(final @Nullable Category parent, final @Nullable String category, final @NotNull String ymlValue) {
        this.parent = Objects.isNull(parent) ? Category.ROOT : parent;
        this.category = category;
        this.ymlValue = ymlValue;
    }

    // METHODS
    public final void loadFromConfig(@Nullable String base, final @NotNull FileConfiguration config) {
        if (Objects.isNull(base)) base = "";
        this.value = config.get(this.toString(base));
        if (Objects.nonNull(this.value) && this.value.equals("-0")) this.value = null;
    }

    public final void saveToConfig(@Nullable String base, final @NotNull FileConfiguration config) {
        if (Objects.isNull(base)) base = "";
        config.set(this.toString(base), Objects.isNull(this.value) ? "-0" : this.value);
    }

    // SETTERS
    public final void value(final @Nullable Object value) {
        this.value = value;
    }

    // CHECKS
    public final boolean isSet() {
        return Objects.nonNull(this.value);
    }

    public final boolean exists(final @NotNull String base, final @NotNull FileConfiguration config) {
        return config.contains(this.toString(base));
    }

    // GETTERS
    public final @NotNull Category parent() {
        return this.parent;
    }

    public final @NotNull String category() {
        return this.category;
    }

    public final @NotNull String ymlValue() {
        return this.ymlValue;
    }

    public final @Nullable Object value() {
        return this.value;
    }

    @Override
    public final String toString() {
        return "%s%s.%s".formatted(this.parent, Objects.isNull(this.category) ? "" : ".%s".formatted(this.category), this.ymlValue);
    }

    public final String toString(final @NotNull String parent) {
        return "%s.%s".formatted(parent, this);
    }


    // INNER CLASS
    public enum Category {
        ROOT("global"),
        LOCATIONS("locations"),
        FEATURES("features"),
        EVENTS("events"),
        BUNGEE_CORD("bungeecord");

        private final String ymlValue;

        Category(final @NotNull String ymlValue) {
            this.ymlValue = ymlValue;
        }

        @Override
        public final String toString() {
            return this.ymlValue;
        }

        public final String toString(final @NotNull String parent) {
            return "%s.%s".formatted(parent, this);
        }
    }
}
