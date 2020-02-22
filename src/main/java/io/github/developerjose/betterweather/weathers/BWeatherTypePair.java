package io.github.developerjose.betterweather.weathers;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BWeatherTypePair extends BWeatherType {

    private BWeatherType mWeather1;
    private BWeatherType mWeather2;
    private Biome mAllowedBiome;

    public BWeatherTypePair(BWeatherType w1, BWeatherType w2) {
        mWeather1 = w1;
        mWeather2 = w2;
    }

    public BWeatherTypePair(BWeatherType w1, BWeatherType w2, Biome allowedBiome) {
        this(w1, w2);
        mAllowedBiome = allowedBiome;
    }

    @Override
    public void worldEffect(World w) {
        mWeather1.worldEffect(w);
        mWeather2.worldEffect(w);
    }

    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        if (canApplyPlayerEffect(p, b)) {
            mWeather1.initialPlayerEffect(p, b);
            mWeather2.initialPlayerEffect(p, b);
        }
    }

    private boolean canApplyPlayerEffect(Player p, Biome b) {
        // Only allow effect if player is in the specified biome
        // If no biome is set, then assume you can apply the effect
        return (mAllowedBiome == null) || (mAllowedBiome == b);
    }

    @Override
    public String toString() {
        return mWeather1.toString() + "-" + mWeather2.toString();
    }

    @Override
    public int getConfigEffectDelay(FileConfiguration config) {
        return Math.max(mWeather1.getConfigEffectDelay(config), mWeather2.getConfigEffectDelay(config));
    }

    @Override
    public int getConfigEffectDuration(FileConfiguration config) {
        return Math.max(mWeather1.getConfigEffectDuration(config), mWeather2.getConfigEffectDuration(config));
    }
}
