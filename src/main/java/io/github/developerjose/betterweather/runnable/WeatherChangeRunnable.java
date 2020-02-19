package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Util;
import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.BWeatherType;
import org.bukkit.scheduler.BukkitRunnable;

public class WeatherChangeRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;

    public WeatherChangeRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
    }

    public void run() {
        // Get a random weather type and modification
        BWeatherType newWeather = Util.getRandomElementFromArray(Weather.ALL_TYPES);

        // Change the weather
        Weather.changeWeather(mPlugin, newWeather);

        mPlugin.log("* Weather changed to %s for %s ticks, (%s seconds), (%s minutes)",
                newWeather, Weather.currentDuration, Weather.currentDuration / 20, Weather.currentDuration / 20 / 60);

        int effectDelayTicks = newWeather.getConfigEffectDelay(mPlugin.getConfig());
        if (effectDelayTicks > 0) {
            mPlugin.log("Effect Delay: %s ticks (%s seconds), (%s minutes)",
                    effectDelayTicks, effectDelayTicks / 20, effectDelayTicks / 20 / 60);
            mPlugin.log("Weather Config Prefix: %s", newWeather.getConfigPrefix(mPlugin.getConfig()));
        }
    }
}