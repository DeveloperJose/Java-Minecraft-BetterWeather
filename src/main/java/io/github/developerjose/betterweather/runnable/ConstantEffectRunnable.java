package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Weather;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ConstantEffectRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;
    private World mWorld;

    public ConstantEffectRunnable(BetterWeatherPlugin plugin) {
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
