package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.BWeatherManager;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class HeavySnow extends LightSnow {
    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        if (b == Biome.ICE_SPIKES) {
            BWeatherManager.makePotionEffect(PotionEffectType.WEAKNESS, 4);
            BWeatherManager.makePotionEffect(PotionEffectType.SLOW, 2);
            p.setPlayerWeather(org.bukkit.WeatherType.DOWNFALL);
        }
    }

    @Override
    public String toString() {
        return "Heavy-Snow";
    }
}
