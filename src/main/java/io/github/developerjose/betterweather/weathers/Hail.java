package io.github.developerjose.betterweather.weathers;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class Hail extends BWeatherType {
    @Override
    public void worldEffect(World w) {
        w.setStorm(true);
    }

    @Override
    public void constantPlayerEffect(Player p, Biome b) {
        p.damage(1);
        p.setLastDamageCause(new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, 1));
    }

    @Override
    public String toString() {
        return "Hail";
    }
}
