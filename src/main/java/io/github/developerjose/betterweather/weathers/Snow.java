package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Snow extends Rain {
    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        if (Weather.currentMod == WeatherMod.LIGHT){
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.WEAKNESS, 2));
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.SLOW, 1));
        }
        else{
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.WEAKNESS, 4));
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.SLOW, 2));
        }
    }

    @Override
    public String toString() {
        return "Snow";
    }
}
