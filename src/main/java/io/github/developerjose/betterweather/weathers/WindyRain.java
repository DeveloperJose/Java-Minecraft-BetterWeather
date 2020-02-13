package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import io.github.developerjose.betterweather.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class WindyRain extends Wind {
    @Override
    public void worldEffect(World w) {
        Weather.RAIN.worldEffect(w);
        Weather.WIND.worldEffect(w);
        if (Weather.currentMod == WeatherMod.HEAVY)
            w.setThundering(true);
    }

    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        Weather.RAIN.initialPlayerEffect(p, b);
        Weather.WIND.initialPlayerEffect(p, b);
    }

    @Override
    public void constantPlayerEffect(Player p, Biome b) {
        Weather.RAIN.constantPlayerEffect(p, b);
        Weather.WIND.constantPlayerEffect(p, b);
    }

    @Override
    public String toString() {
        return "Windy Rain";
    }
}
