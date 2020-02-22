package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.HailRunnable;
import io.github.developerjose.betterweather.runnable.RandomWeatherChangeRunnable;
import io.github.developerjose.betterweather.runnable.WindRunnable;
import io.github.developerjose.betterweather.weathers.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class BWeatherManager {
    public static boolean isPluginChangingWeather = false;

    public static BWeatherType currentWeatherType = BWeather.CLEAR;
    public static int currentDuration = 0;
    public static Vector currentWindDirection = new Vector(0, 0, 0);

    public static void changeWeather(BetterWeatherPlugin mPlugin, BWeatherType newWeatherType, int newDurationTicks) {
        // Update static variables
        isPluginChangingWeather = true;
        currentWeatherType = newWeatherType;
        currentDuration = newDurationTicks;

        // Cancel all previous tasks
        BukkitScheduler scheduler = mPlugin.getServer().getScheduler();
        scheduler.cancelTasks(mPlugin);

        // Clear previous weather
        World w = mPlugin.getServer().getWorlds().get(0);
        Util.clearWorldEffects(w);

        for (Player p : w.getPlayers()) {
            // Clear previous effects, sounds, and weathers
            Util.clearPlayerEffects(p);

            // Run the initial effect
            Biome b = p.getLocation().getBlock().getBiome();
            newWeatherType.initialPlayerEffect(p, b);
        }

        // Run world effect
        newWeatherType.worldEffect(w);

        // Set wind direction and force
        double windFactor = mPlugin.getConfig().getDouble("wind-push-factor");
        currentWindDirection = Vector.getRandom();
        currentWindDirection = currentWindDirection.setY(0);
        currentWindDirection.normalize();
        currentWindDirection.multiply(windFactor);

        // Create extra effect runnables if necessary
        if (newWeatherType instanceof LightWind || newWeatherType instanceof BWeatherTypePair)
            new WindRunnable(mPlugin).runTask();
        else if (newWeatherType instanceof Hail)
            new HailRunnable(mPlugin).runTask();

        // Get the delay between weather changes
        int changeDelayTicks = newDurationTicks;
        if (newDurationTicks > 0)
            changeDelayTicks += mPlugin.getConfig().getInt("weather-change-delay") * 20;

        // Repeat task to pick new weather
        new RandomWeatherChangeRunnable(mPlugin).runTaskLater(mPlugin, changeDelayTicks);

        // Announce the new weather
        String weatherMessage = newWeatherType.getConfigBroadcastMessage(mPlugin.getConfig());
        if (weatherMessage.length() > 0)
            mPlugin.getServer().broadcastMessage(weatherMessage);

        // Debug logging of weather change and information
        mPlugin.log("Weather changed to %s for %s ticks, (%s sec), (%s min)",
                newWeatherType, BWeatherManager.currentDuration, BWeatherManager.currentDuration / 20, BWeatherManager.currentDuration / 20 / 60);

        isPluginChangingWeather = false;
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration - 20, level);
    }
}
