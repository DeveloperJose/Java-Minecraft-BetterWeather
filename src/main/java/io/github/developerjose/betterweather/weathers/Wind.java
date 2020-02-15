package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import io.github.developerjose.betterweather.WeatherType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Wind extends WeatherType {
    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        if (Weather.currentMod == WeatherMod.LIGHT)
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.SLOW, 1));
        else
            p.addPotionEffect(Weather.makePotionEffect(PotionEffectType.SLOW, 3));
    }

    @Override
    public void constantPlayerEffect(Player p, Biome b) {
        Vector velocity = p.getVelocity();
        p.setVelocity(velocity.add(Weather.windDirection));
        p.playSound(p.getLocation(), Sound.BLOCK_PUMPKIN_CARVE, 0.5f, 0.5f);

        Location particleLoc = p.getLocation();
        particleLoc.setY(p.getEyeLocation().getY());
        p.spawnParticle(Particle.FIREWORKS_SPARK, particleLoc.add(Weather.windDirection), 20);
    }

    @Override
    public boolean isAffectedByModifiers() {
        return true;
    }

    @Override
    public String toString() {
        return "Wind";
    }
}
