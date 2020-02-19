package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.ConstantEffectRunnable;
import io.github.developerjose.betterweather.runnable.ParticleRunnable;
import io.github.developerjose.betterweather.runnable.WeatherChangeRunnable;
import io.github.developerjose.betterweather.weathers.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import static io.github.developerjose.betterweather.BWeatherType.CLEAR;

public class Weather {
    public static boolean isPluginChangingWeather = false;

    public static BWeatherType currentType = CLEAR;
    public static int currentDuration = 0;

    public static Vector windDirection = new Vector(0, 0, 0);

    public static void changeWeather(BetterWeatherPlugin plugin, BWeatherType newType) {
        int durationTicks = newType.getConfigWeatherDuration(plugin.getConfig());
        changeWeather(plugin, newType, durationTicks);
    }

    public static void changeWeather(BetterWeatherPlugin plugin, BWeatherType newType, int durationTicks) {
        // After hail, force the weather to change to one of the hail list
        if (currentType instanceof Hail) {
            currentType = Util.getRandomElementFromArray(BWeatherType.AFTER_HAIL);
        }

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
        new WeatherChangeRunnable(plugin).runTaskLater(plugin, Weather.currentDuration + changeDelayTicks);

        // Get the effect delay from the configuration and create the effect task
        int effectDelay = newType.getConfigEffectDelay(plugin.getConfig());
        int effectDuration = newType.getConfigEffectDuration(plugin.getConfig());
        if (effectDelay > 0)
            new ConstantEffectRunnable(plugin, effectDelay, effectDuration).runTaskTimer(plugin, effectDelay, 20);

        new ParticleRunnable(plugin).runTaskTimer(plugin, 60, 60);

        isPluginChangingWeather = false;
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration - 20, level);
    }
}
