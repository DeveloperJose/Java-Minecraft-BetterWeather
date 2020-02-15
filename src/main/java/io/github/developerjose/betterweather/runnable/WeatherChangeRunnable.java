package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import io.github.developerjose.betterweather.WeatherType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WeatherChangeRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;

    public WeatherChangeRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
    }

    public void run() {
        // Get a random weather type and modification
        WeatherType newWeather = getRandomElementFromArray(Weather.ALL_TYPES);
        WeatherMod newMod = getRandomElementFromArray(WeatherMod.values());

        // Change the weather
        Weather.changeWeather(mPlugin, newWeather, newMod);

        mPlugin.log("* Weather changed to %s [%s] for %s ticks, (%s seconds), (%s minutes)",
                newWeather, newMod, Weather.currentDuration, Weather.currentDuration / 20, Weather.currentDuration / 20 / 60);

        int effectDelayTicks = newWeather.getConfigEffectDelay(mPlugin.getConfig());
        if (effectDelayTicks > 0) {
            mPlugin.log("Effect Delay: %s ticks (%s seconds), (%s minutes)",
                    effectDelayTicks, effectDelayTicks / 20, effectDelayTicks / 20 / 60);
            mPlugin.log("Weather Config Prefix: %s", newWeather.getConfigPrefix(mPlugin.getConfig()));
        }
    }

    private static <T> T getRandomElementFromArray(T[] arr) {
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }
}