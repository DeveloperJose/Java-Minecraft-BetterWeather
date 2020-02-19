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
            if (b == Biome.ICE_SPIKES) {
                Location l = p.getLocation();
                p.spawnParticle(Particle.CLOUD, l.getX() + 0.3, l.getY() + 0.3, l.getZ() + 0.3, 5);
                p.sendMessage("Affected by chill");
            }
        }
    }
}
