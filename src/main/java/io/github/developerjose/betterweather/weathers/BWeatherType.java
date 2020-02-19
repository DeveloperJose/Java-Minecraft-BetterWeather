package io.github.developerjose.betterweather.weathers;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Base class for new weathers
 */
public abstract class BWeatherType {
    public static BWeatherType CLEAR = new Clear();
    public static BWeatherType HAIL = new Hail();

    public static BWeatherType HEAVY_RAIN = new HeavyRain();
    public static BWeatherType LIGHT_RAIN = new LightRain();

    public static BWeatherType HEAVY_SNOW = new HeavySnow();
    public static BWeatherType LIGHT_SNOW = new LightSnow();

    public static BWeatherType HEAVY_WIND = new HeavyWind();
    public static BWeatherType LIGHT_WIND = new LightWind();

    public static BWeatherType LIGHT_WIND_LIGHT_RAIN = new BWeatherTypePair(LIGHT_WIND, LIGHT_RAIN);
    public static BWeatherType HEAVY_WIND_LIGHT_RAIN = new BWeatherTypePair(HEAVY_WIND, LIGHT_RAIN);
    public static BWeatherType HEAVY_WIND_HEAVY_RAIN = new BWeatherTypePair(HEAVY_WIND, HEAVY_RAIN); // Alias: Thunderstorm

    public static BWeatherType LIGHT_WIND_LIGHT_SNOW = new BWeatherTypePair(LIGHT_WIND, LIGHT_SNOW);
    public static BWeatherType HEAVY_WIND_HEAVY_SNOW = new BWeatherTypePair(HEAVY_WIND, HEAVY_SNOW); // Alias: Blizzard

    public static final BWeatherType[] ALL_TYPES = new BWeatherType[]{CLEAR, HAIL,
            LIGHT_RAIN, LIGHT_WIND, LIGHT_SNOW,
            HEAVY_RAIN, HEAVY_WIND, HEAVY_SNOW,
            LIGHT_WIND_LIGHT_RAIN, HEAVY_WIND_LIGHT_RAIN, HEAVY_WIND_HEAVY_RAIN,
            LIGHT_WIND_LIGHT_SNOW, HEAVY_WIND_HEAVY_SNOW
    };

    public static final BWeatherType[] AFTER_HAIL = new BWeatherType[]{CLEAR, HEAVY_RAIN, HEAVY_SNOW};

    /**
     * Converts a weather name string into a BWeatherType
     *
     * @param weatherName String containing the weather name
     * @return The given BWeather or null if an invalid name is given
     */
    public static BWeatherType fromString(String weatherName) {
        if (weatherName.equalsIgnoreCase("blizzard"))
            return HEAVY_WIND_HEAVY_SNOW;
        else if (weatherName.equalsIgnoreCase("thunderstorm"))
            return HEAVY_WIND_HEAVY_RAIN;

        for (BWeatherType w : BWeatherType.ALL_TYPES)
            if (w.toString().equalsIgnoreCase(weatherName))
                return w;
        return null;
    }

    /**
     * Transforms the world according to the weather
     *
     * @param w World to change
     */
    public void worldEffect(World w) {

    }

    /**
     * Transforms the players according to the weather.
     * Only runs once, at the beginning of the weather change.
     *
     * @param p Player to change
     * @param b Biome the player is in
     */
    public void initialPlayerEffect(Player p, Biome b) {

    }

    /**
     * Transforms the players according to the weather.
     * Constantly runs until the weather changes.
     *
     * @param p Player to change
     * @param b Biome the player is in
     */
    public void constantPlayerEffect(Player p, Biome b) {

    }

    /**
     * Get the duration of this weather from the config file
     *
     * @param config The file configuration of the plugin
     * @return The duration of this weather in ticks
     */
    public int getConfigWeatherDuration(FileConfiguration config) {
        String configTag = getConfigPrefix(config) + "-duration";
        return config.getInt(configTag) * 20;
    }

    /**
     * Get the delay between constant weather effects from the config file
     *
     * @param config The file configuration of the plugin
     * @return The delay of this effect in ticks
     */
    public int getConfigEffectDelay(FileConfiguration config) {
        String configTag = getConfigPrefix(config) + "-delay";
        return config.getInt(configTag, 0) * 20;
    }

    /**
     * Get the duration of the constant weather effect from the config file
     *
     * @param config The file configuration of the plugin
     * @return The delay of this effect in ticks
     */
    public int getConfigEffectDuration(FileConfiguration config) {
        String configTag = getConfigPrefix(config) + "-effect-duration";
        return config.getInt(configTag) * 20;
    }

    /**
     * Get the message to broadcast when the weather changes
     *
     * @param config The file configuration of the plugin
     * @return The broadcast message of this weather
     */
    public String getConfigBroadcastMessage(FileConfiguration config) {
        String configTag = getConfigPrefix(config) + "-message";
        String broadcastMessage = config.getString(configTag);
        return ChatColor.translateAlternateColorCodes('&', broadcastMessage);
    }

    /**
     * Gets the config file prefix for this weather
     *
     * @param config The file configuration of the plugin
     * @return The config prefix for this weather
     */
    public String getConfigPrefix(FileConfiguration config) {
        String configTag = "";
        // Add the name of the weather
        configTag += toString();
        return configTag.toLowerCase();
    }
}