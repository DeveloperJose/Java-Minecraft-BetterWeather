package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.weathers.BWeatherType;
import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Util;
import io.github.developerjose.betterweather.BWeather;
import org.bukkit.scheduler.BukkitRunnable;

public class WeatherChangeRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;

    public WeatherChangeRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
    }

    public void run() {
        // Get a random weather type
        BWeatherType newWeather = Util.getRandomElementFromArray(BWeatherType.ALL_TYPES);

        // Change the weather
        BWeather.changeWeather(mPlugin, newWeather);
    }

}