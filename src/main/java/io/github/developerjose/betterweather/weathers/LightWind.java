package io.github.developerjose.betterweather.weathers;

import io.github.developerjose.betterweather.BWeather;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class LightWind extends BWeatherType {
    @Override
    public void initialPlayerEffect(Player p, Biome b) {
        p.addPotionEffect(BWeather.makePotionEffect(PotionEffectType.SLOW, 1));
    }

    @Override
    public void constantPlayerEffect(Player p, Biome b) {
        Vector velocity = p.getVelocity();
        p.setVelocity(velocity.add(BWeather.windDirection));
        p.playSound(p.getLocation(), Sound.ITEM_ELYTRA_FLYING, 0.5f, 0.5f);

        Location particleLoc = p.getLocation();
        particleLoc.setY(p.getEyeLocation().getY());
        p.spawnParticle(Particle.SMOKE_NORMAL, particleLoc.add(BWeather.windDirection), 20);
    }

    @Override
    public String toString() {
        return "Light-Wind";
    }
}
