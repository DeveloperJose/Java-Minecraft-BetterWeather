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
    private static final int TICKS_PER_SECOND = 20;
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

    public static void changeWeather(JavaPlugin plugin, World w, WeatherType newType, WeatherMod newMod, int newDuration) {
        // Set weather duration
        w.setWeatherDuration(newDuration);
        w.setThunderDuration(newDuration);

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

        // Update static variables
        currentType = newType;
        currentMod = newMod;
        currentDuration = newDuration;

        // Cancel the previous constant effect runnable
        int taskID = BetterWeatherPlugin.constantEffectRunnable.getTaskId();
        plugin.getServer().getScheduler().cancelTask(taskID);

        // Get the new constant effect tick timer
        int effectSeconds = 10;
        if (newType instanceof Hail)
            effectSeconds = plugin.getConfig().getInt("hail-effect-seconds", 5);
        if (newType instanceof Wind)
            effectSeconds = plugin.getConfig().getInt("wind-effect-seconds", 1);

        ConstantEffectRunnable newRun = new ConstantEffectRunnable(plugin);
        BetterWeatherPlugin.constantEffectRunnable = newRun;
        newRun.runTaskTimer(plugin, 0, effectSeconds * TICKS_PER_SECOND);
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration, level);
    }
}
