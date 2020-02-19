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
    private int mDelayTicks;
    private int mDurationTicks;
    private int mCurrentTicks;

    public ConstantEffectRunnable(BetterWeatherPlugin plugin, int delayTicks, int durationTicks) {
        mPlugin = plugin;
        mWorld = mPlugin.getServer().getWorlds().get(0);
        mDelayTicks = delayTicks;
        mDurationTicks = durationTicks;

        mCurrentTicks = 0;
    }

    public void run() {
        for (Player p : mWorld.getPlayers()) {
            Biome b = p.getLocation().getBlock().getBiome();
            Weather.currentType.constantPlayerEffect(p, b);
        }

        mCurrentTicks += 20;
        if (mCurrentTicks > mDurationTicks) {
            this.cancel();
            new ConstantEffectRunnable(mPlugin, mDelayTicks, mDurationTicks).runTaskTimer(mPlugin, mDelayTicks, 20);
        }
    }
}
