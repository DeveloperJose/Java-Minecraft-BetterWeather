package io.github.developerjose.betterweather.weathers;

import org.bukkit.World;

public class Hail extends BWeatherType {
    @Override
    public void worldEffect(World w) {
        w.setStorm(true);
    }

    @Override
    public String toString() {
        return "Hail";
    }
}
