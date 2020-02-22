package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.BWeatherManager;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class HeavyRain extends LightRain {
    @Override
    public void worldEffect(World w) {
        super.worldEffect(w);
        w.setThundering(true);
        w.setThunderDuration(BWeatherManager.currentDuration);
    }

    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        p.addPotionEffect(BWeatherManager.makePotionEffect(PotionEffectType.WEAKNESS, 2));
        p.addPotionEffect(BWeatherManager.makePotionEffect(PotionEffectType.SLOW, 1));
    }

    @Override
    public String toString() {
        return "Heavy-Rain";
    }
}
