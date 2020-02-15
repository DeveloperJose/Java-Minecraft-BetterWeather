package io.github.developerjose.betterweather;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Base class for new weathers
 */
public abstract class WeatherType {

    /**
     * Is this weather affected by the light and heavy modifiers?
     *
     * @return True if this weather has a light and heavy version
     */
    public abstract boolean isAffectedByModifiers();

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

    public String getConfigPrefix(FileConfiguration config) {
        String configTag = "";
        // Add the light/heavy info if it's required
        if (isAffectedByModifiers())
            configTag += Weather.currentMod + "-";

        // Add the name of the weather
        configTag += toString();
        return configTag.toLowerCase();
    }
}