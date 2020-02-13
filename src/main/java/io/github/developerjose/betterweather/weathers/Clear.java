package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.WeatherType;
import org.bukkit.World;

public class Clear extends WeatherType {
    @Override
    public void worldEffect(World w) {
        w.setThundering(false);
        w.setStorm(false);
    }

    @Override
    public String toString() {
        return "Clear";
    }
}
