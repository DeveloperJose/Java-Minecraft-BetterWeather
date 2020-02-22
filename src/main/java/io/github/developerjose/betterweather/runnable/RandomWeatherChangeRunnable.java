package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BWeatherManager;
import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Util;
import io.github.developerjose.betterweather.weathers.BWeather;
import io.github.developerjose.betterweather.weathers.BWeatherType;
import io.github.developerjose.betterweather.weathers.Hail;
import org.bukkit.scheduler.BukkitRunnable;

public class RandomWeatherChangeRunnable extends BukkitRunnable {
    private BetterWeatherPlugin mPlugin;

    public RandomWeatherChangeRunnable(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
    }

    public void run() {
        // Get a random weather type
        BWeatherType newWeather = Util.getRandomElementFromArray(BWeather.ALL_TYPES);

        // Get the duration of that weather
        int durationTicks = newWeather.getConfigWeatherDuration(mPlugin.getConfig());

        // After hail, force the weather to change to one in the hail list
        // also force weather to change immediately
        BWeatherType previousType = BWeatherManager.currentWeatherType;
        if (previousType instanceof Hail) {
            newWeather = Util.getRandomElementFromArray(BWeather.AFTER_HAIL);
            durationTicks = 0;
        }
        BWeatherManager.changeWeather(mPlugin, newWeather, durationTicks);
    }

}