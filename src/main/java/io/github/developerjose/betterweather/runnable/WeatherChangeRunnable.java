package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import io.github.developerjose.betterweather.WeatherType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WeatherChangeRunnable extends BukkitRunnable {
    private JavaPlugin mPlugin;

    public WeatherChangeRunnable(JavaPlugin plugin) {
        mPlugin = plugin;
    }

    public void run() {
        // Get a random weather type and modification
        WeatherType newWeather = getRandomElementFromArray(Weather.ALL_TYPES);
        WeatherMod newMod = getRandomElementFromArray(WeatherMod.values());

        // Change the weather
        Weather.changeWeather(mPlugin, newWeather, newMod);

        // Repeat task to pick new weather once this one finishes
        WeatherChangeRunnable newRun = new WeatherChangeRunnable(mPlugin);
        BetterWeatherPlugin.weatherChangeRunnable = newRun;
        newRun.runTaskLater(mPlugin, Weather.currentDuration);

        log("** Debug: Weather changed to %s [%s] for %s ticks, (%s seconds), (%s minutes) **",
                newWeather, newMod, Weather.currentDuration, Weather.currentDuration / 20, Weather.currentDuration / 20 / 60);

        int effectDelayTicks = newWeather.getConfigEffectDelay(mPlugin.getConfig());
        if (effectDelayTicks > 0) {
            log("Effect Delay: %s ticks (%s seconds), (%s minutes)",
                    effectDelayTicks, effectDelayTicks / 20, effectDelayTicks / 20 / 60);
            log("Weather Config Prefix: %s", newWeather.getConfigPrefix(mPlugin.getConfig()));
        }
    }

    private void log(String message, Object... args) {
        if (mPlugin.getConfig().getBoolean("debug")) {
            mPlugin.getServer().broadcastMessage(String.format("[BetterWeather]" + message, args));
        }
    }

    private static <T> T getRandomElementFromArray(T[] arr) {
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }
}