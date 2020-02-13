package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import io.github.developerjose.betterweather.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Rain extends WeatherType {

    @Override
    public void worldEffect(World w) {
        w.setStorm(true);
    }

    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        if (b == Biome.ICE_SPIKES)
            Weather.SNOW.initialPlayerEffect(p, b);
        else
            initialRainEffect(p);
    }

    private void initialRainEffect(Player p) {
        if (Weather.currentMod == WeatherMod.LIGHT) {
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.WEAKNESS, 1));
        } else {
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.WEAKNESS, 2));
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.SLOW, 1));
        }
    }

    @Override
    public void constantPlayerEffect(Player p, Biome b) {
        if (b == Biome.ICE_SPIKES)
            Weather.SNOW.constantPlayerEffect(p, b);
    }

    @Override
    public String toString() {
        return "Rain";
    }
}
