package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.ConstantEffectRunnable;
import io.github.developerjose.betterweather.runnable.WeatherChangeRunnable;
import io.github.developerjose.betterweather.weathers.BWeatherType;
import io.github.developerjose.betterweather.weathers.Hail;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class BWeather {
    public static boolean isPluginChangingWeather = false;

    public static BWeatherType currentType = BWeatherType.CLEAR;
    public static int currentDuration = 0;

    public static Vector windDirection = new Vector(0, 0, 0);

    public static void changeWeather(BetterWeatherPlugin plugin, BWeatherType newType) {
        int durationTicks = newType.getConfigWeatherDuration(plugin.getConfig());
        changeWeather(plugin, newType, durationTicks);
    }

    public static void changeWeather(BetterWeatherPlugin plugin, BWeatherType newType, int durationTicks) {
        // Update static variables
        isPluginChangingWeather = true;
        currentType = newType;
        currentDuration = durationTicks;

        // Cancel all previous tasks
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTasks(plugin);

        // Set weather duration
        World w = plugin.getServer().getWorlds().get(0);
        w.setWeatherDuration(durationTicks);
        w.setThunderDuration(durationTicks);

        // Run world effect
        newType.worldEffect(w);

        // Run initial effect
        for (Player p : w.getPlayers()) {
            Biome b = p.getLocation().getBlock().getBiome();
            newType.initialPlayerEffect(p, b);
        }

        // Set wind direction and force
        double windFactor = plugin.getConfig().getDouble("wind-push-factor");
        windDirection = Vector.getRandom();
        windDirection = windDirection.setY(0);
        windDirection.normalize();
        windDirection.multiply(windFactor);

        // Repeat task to pick new weather once this one finishes and the delay passes
        int changeDelayTicks = plugin.getConfig().getInt("weather-change-delay") * 20;

        // After hail, force the weather to change to one of the hail list immediately
        if (currentType instanceof Hail) {
            currentType = Util.getRandomElementFromArray(BWeatherType.AFTER_HAIL);
            changeDelayTicks = 0;
        }

        new WeatherChangeRunnable(plugin).runTaskLater(plugin, BWeather.currentDuration + changeDelayTicks);

        // Get the effect delay from the configuration and create the effect task
        int effectDelay = newType.getConfigEffectDelay(plugin.getConfig());
        int effectDuration = newType.getConfigEffectDuration(plugin.getConfig());
        if (effectDelay > 0)
            new ConstantEffectRunnable(plugin, effectDelay, effectDuration).runTaskTimer(plugin, effectDelay, 20);

        // Announce the new weather
        String weatherMessage = newType.getConfigBroadcastMessage(plugin.getConfig());
        if (weatherMessage.length() > 0)
            plugin.getServer().broadcastMessage(weatherMessage);

        // Debug logging of weather change and information
        plugin.log("Weather changed to %s for %s ticks, (%s sec), (%s min)",
                newType, BWeather.currentDuration, BWeather.currentDuration / 20, BWeather.currentDuration / 20 / 60);

        if (effectDelay > 0) {
            plugin.log("Effect Delay: %s ticks (%s sec), (%s min)",
                    effectDelay, effectDelay / 20, effectDelay / 20 / 60);
            plugin.log("Effect Duration: %s ticks (%s sec), (%s min)",
                    effectDuration, effectDuration / 20, effectDuration / 20 / 60);
            plugin.log("Weather Config Prefix: %s", newType.getConfigPrefix(plugin.getConfig()));
        }

        isPluginChangingWeather = false;
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration - 20, level);
    }
}
