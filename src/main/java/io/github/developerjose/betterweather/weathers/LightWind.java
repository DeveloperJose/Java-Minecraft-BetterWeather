package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.BWeather;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class LightWind extends BWeatherType {
    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        p.addPotionEffect(BWeather.makePotionEffect(PotionEffectType.SLOW, 1));
    }

    @Override
    public String toString() {
        return "Light-Wind";
    }
}
