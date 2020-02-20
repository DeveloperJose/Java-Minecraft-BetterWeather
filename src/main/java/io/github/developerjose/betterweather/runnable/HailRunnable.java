package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BWeather;
import io.github.developerjose.betterweather.BetterWeatherPlugin;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HailRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;
    private World mWorld;
    private int mDelayTicks;

    public HailRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
        mWorld = plugin.getServer().getWorlds().get(0);
        mDelayTicks = BWeather.currentType.getConfigEffectDelay(plugin.getConfig());
    }

    public BukkitTask runTask() {
        mPlugin.log("DelayTicks: %s, %s", mDelayTicks, BWeather.currentType.getConfigPrefix(mPlugin.getConfig()));
        return runTaskTimer(mPlugin, 0, mDelayTicks);
    }

    public void run() {
        for (Player p : mWorld.getPlayers()) {
            p.damage(1);
            p.setLastDamageCause(new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, 1));
        }
    }
}
