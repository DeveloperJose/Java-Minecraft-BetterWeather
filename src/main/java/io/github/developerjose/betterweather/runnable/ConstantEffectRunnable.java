package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.Weather;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ConstantEffectRunnable extends BukkitRunnable {
    private JavaPlugin mPlugin;
    private World mWorld;

    public ConstantEffectRunnable(JavaPlugin plugin) {
        mPlugin = plugin;
        mWorld = mPlugin.getServer().getWorlds().get(0);
    }

    public void run() {
        for (Player p : mWorld.getPlayers()) {
            Biome b = p.getLocation().getBlock().getBiome();
            Weather.currentType.constantPlayerEffect(p, b);
        }
    }
}
