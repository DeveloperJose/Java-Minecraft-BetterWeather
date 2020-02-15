package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.ConstantEffectRunnable;
import io.github.developerjose.betterweather.weathers.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class Weather {
    public static WeatherType CLEAR = new Clear();
    public static WeatherType RAIN = new Rain();
    public static WeatherType SNOW = new Snow();
    public static WeatherType WIND = new Wind();
    public static WeatherType WINDYRAIN = new WindyRain();
    public static WeatherType HAIL = new Hail();
    public static final WeatherType[] ALL_TYPES = new WeatherType[]{CLEAR, RAIN, WIND, WINDYRAIN, HAIL};

    public static int currentDuration = 0;
    public static WeatherType currentType = CLEAR;
    public static WeatherMod currentMod = WeatherMod.LIGHT;

    public static Vector windDirection = new Vector(0, 0, 0);

    public static WeatherType weatherFromString(String weatherName){
        for (WeatherType w : Weather.ALL_TYPES)
            if (w.toString().equalsIgnoreCase(weatherName))
                return w;
        return null;
    }

    public static void changeWeather(JavaPlugin plugin, WeatherType newType, WeatherMod newMod){
        changeWeather(plugin, newType, newMod, newType.getConfigWeatherDuration(plugin.getConfig()));
    }

    public static void changeWeather(JavaPlugin plugin, WeatherType newType, WeatherMod newMod, int durationTicks) {
        // Update static variables
        currentType = newType;
        currentMod = newMod;
        currentDuration = durationTicks;

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
        windDirection = Vector.getRandom();
        windDirection = windDirection.setY(0);
        windDirection = windDirection.normalize();
        windDirection = windDirection.multiply(0.3f);

        // Cancel the previous constant effect runnable
        int taskID = BetterWeatherPlugin.constantEffectRunnable.getTaskId();
        plugin.getServer().getScheduler().cancelTask(taskID);

        // Get the new constant effect tick timer
        int effectDelay = newType.getConfigEffectDelay(plugin.getConfig());

        ConstantEffectRunnable newRun = new ConstantEffectRunnable(plugin);
        BetterWeatherPlugin.constantEffectRunnable = newRun;
        newRun.runTaskTimer(plugin, 0, effectDelay);
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration, level);
    }
}
