package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BWeatherManager;
import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Util;
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
        mDelayTicks = BWeatherManager.currentWeatherType.getConfigEffectDelay(plugin.getConfig());
    }

    public BukkitTask runTask() {
        mPlugin.log("DelayTicks: %s, %s", mDelayTicks, BWeatherManager.currentWeatherType.getConfigPrefix(mPlugin.getConfig()));
        return runTaskTimer(mPlugin, 0, mDelayTicks);
    }

    public void run() {
        for (Player p : mWorld.getPlayers()) {
            // Don't damage players under cover
            if (Util.isPlayerUnderBlockCover(p))
                continue;

            p.damage(1);
            p.setLastDamageCause(new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CUSTOM, 1));

        }
    }
}
