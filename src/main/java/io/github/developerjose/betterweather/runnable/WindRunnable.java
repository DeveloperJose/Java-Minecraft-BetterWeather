package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BWeather;
import io.github.developerjose.betterweather.BetterWeatherPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class WindRunnable extends BukkitRunnable {
    public static final int TICK_PERIOD = 5;

    private BetterWeatherPlugin mPlugin;
    private World mWorld;
    private int mCurrentTicks;
    private int mDelayTicks;
    private int mDurationTicks;

    public WindRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
        mWorld = mPlugin.getServer().getWorlds().get(0);
        mCurrentTicks = 0;

        // Get the duration and delay from the config
        mDelayTicks = BWeather.currentType.getConfigEffectDelay(plugin.getConfig());
        mDurationTicks = BWeather.currentType.getConfigEffectDuration(plugin.getConfig());
    }

    public BukkitTask runTask() {
        return runTaskTimer(mPlugin, mDelayTicks, TICK_PERIOD);
    }

    public void run() {
        boolean isFirst = mCurrentTicks == 0;

        // Update tick counter
        mCurrentTicks += TICK_PERIOD;
        boolean isLast = mCurrentTicks >= mDurationTicks;

        for (Player p : mWorld.getPlayers()) {
            // Push player
            Vector velocity = p.getVelocity();
            p.setVelocity(velocity.add(BWeather.windDirection));

            // Spawn particles
            Location particleLoc = p.getLocation();
            particleLoc.setY(p.getEyeLocation().getY());
            p.spawnParticle(Particle.SMOKE_NORMAL, particleLoc.add(BWeather.windDirection), 20);

            // Play or stop sound
            if (isFirst)
                p.playSound(p.getLocation(), Sound.ITEM_ELYTRA_FLYING, 1f, 0.5f);
            else if (isLast)
                p.stopSound(Sound.ITEM_ELYTRA_FLYING);
        }

        // Cancel this task and schedule a delayed one after we reach the duration
        if (mCurrentTicks >= mDurationTicks) {
            new WindRunnable(mPlugin).runTask();
            this.cancel();
        }
    }
}
