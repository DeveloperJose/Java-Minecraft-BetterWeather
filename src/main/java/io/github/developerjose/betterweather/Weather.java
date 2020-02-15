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

public class Weather {
    public static WeatherType CLEAR = new Clear();
    public static WeatherType RAIN = new Rain();
    public static WeatherType WIND = new Wind();
    public static WeatherType WINDYRAIN = new WindyRain();
    public static WeatherType HAIL = new Hail();
    public static final WeatherType[] ALL_TYPES = new WeatherType[]{CLEAR, RAIN, WIND, WINDYRAIN, HAIL};

    public static boolean isPluginChangingWeather = false;
    public static int currentDuration = 0;
    public static WeatherType currentType = CLEAR;
    public static WeatherMod currentMod = WeatherMod.LIGHT;

    public static Vector windDirection = new Vector(0, 0, 0);

    public static WeatherType weatherFromString(String weatherName) {
        for (WeatherType w : Weather.ALL_TYPES)
            if (w.toString().equalsIgnoreCase(weatherName))
                return w;
        return null;
    }

    public static void changeWeather(BetterWeatherPlugin plugin, WeatherType newType, WeatherMod newMod) {
        changeWeather(plugin, newType, newMod, newType.getConfigWeatherDuration(plugin.getConfig()));
    }

    public static void changeWeather(BetterWeatherPlugin plugin, WeatherType newType, WeatherMod newMod, int durationTicks) {
        // After hail, force the weather to change to heavy rain
        if (currentType instanceof Hail) {
            currentType = Weather.RAIN;
            currentMod = WeatherMod.HEAVY;
        }

        // Update static variables
        isPluginChangingWeather = true;
        currentType = newType;
        currentMod = newMod;
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

        // Set wind direction
        double windFactor = plugin.getConfig().getDouble("wind-push-factor");
        windDirection = Vector.getRandom();
        windDirection = windDirection.setY(0);
        windDirection.normalize();
        windDirection.multiply(windFactor);

        // Repeat task to pick new weather once this one finishes
        new WeatherChangeRunnable(plugin).runTaskLater(plugin, Weather.currentDuration);

        // Get the effect delay from the configuration and create the effect task
        int effectDelay = newType.getConfigEffectDelay(plugin.getConfig());
        new ConstantEffectRunnable(plugin).runTaskTimer(plugin, effectDelay, effectDelay);

        new ParticleRunnable(plugin).runTaskTimer(plugin, 60, 60);

        isPluginChangingWeather = false;
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration, level);
    }
}
