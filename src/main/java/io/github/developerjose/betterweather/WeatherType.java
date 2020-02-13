package io.github.developerjose.betterweather;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

/**
 * Base class for new weathers
 */
public abstract class WeatherType {

    /**
     * Transforms the world according to the weather
     * @param w World to change
     */
    public void worldEffect(World w) {

    }

    /**
     * Transforms the players according to the weather.
     * Only runs once, at the beginning of the weather change.
     * @param p Player to change
     * @param b Biome the player is in
     */
    public void initialPlayerEffect(Player p, Biome b) {

    }

    /**
     * Transforms the players according to the weather.
     * Constantly runs until the weather changes.
     * @param p Player to change
     * @param b Biome the player is in
     */
    public void constantPlayerEffect(Player p, Biome b) {

    }
}