package io.github.developerjose.betterweather.weathers;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BWeatherTypePair extends BWeatherType {
    private BWeatherType mWeather1;
    private BWeatherType mWeather2;

    public BWeatherTypePair(BWeatherType w1, BWeatherType w2) {
        mWeather1 = w1;
        mWeather2 = w2;
    }

    @Override
    public void worldEffect(World w) {
        mWeather1.worldEffect(w);
        mWeather2.worldEffect(w);
    }

    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        if (isSnowy() && b != Biome.ICE_SPIKES)
            return;

        mWeather1.initialPlayerEffect(p, b);
        mWeather2.initialPlayerEffect(p, b);
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

    @Override
    public boolean isSnowy() {
        return mWeather2 instanceof LightSnow || mWeather1 instanceof LightSnow;
    }
}
