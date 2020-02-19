package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.BWeather;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class LightRain extends BWeatherType {

    @Override
    public void worldEffect(World w) {
        w.setStorm(true);
    }

    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        p.addPotionEffect(BWeather.makePotionEffect(PotionEffectType.WEAKNESS, 1));
    }

    @Override
    public String toString() {
        return "Light-Rain";
    }
}
