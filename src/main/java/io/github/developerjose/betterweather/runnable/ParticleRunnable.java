package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BetterWeatherPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;

    public ParticleRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
    }

    public void run() {
        for (Player p : mPlugin.getServer().getWorlds().get(0).getPlayers()) {
            Biome b = p.getLocation().getBlock().getBiome();
            if (b == Biome.ICE_SPIKES){
                Location particleLoc = p.getLocation();
                particleLoc.setY(particleLoc.getY()-1);
                p.spawnParticle(Particle.SNOWBALL, particleLoc, 100);
            }
        }
    }
}
