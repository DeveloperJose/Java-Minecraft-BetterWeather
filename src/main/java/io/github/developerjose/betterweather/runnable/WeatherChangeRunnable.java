package io.github.developerjose.betterweather.runnable;

import io.github.developerjose.betterweather.BetterWeatherPlugin;
import io.github.developerjose.betterweather.Weather;
import io.github.developerjose.betterweather.WeatherMod;
import io.github.developerjose.betterweather.weathers.Hail;
import io.github.developerjose.betterweather.weathers.Rain;
import io.github.developerjose.betterweather.weathers.Clear;
import io.github.developerjose.betterweather.WeatherType;
import io.github.developerjose.betterweather.weathers.Wind;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WeatherChangeRunnable extends BukkitRunnable {
    private static final int TICKS_PER_SECOND = 20;

    private JavaPlugin mPlugin;
    private World mWorld;

    public WeatherChangeRunnable(JavaPlugin plugin) {
        mPlugin = plugin;
        mWorld = mPlugin.getServer().getWorlds().get(0);
    }

    public void run() {
        // Get a random weather duration
        int minSeconds = mPlugin.getConfig().getInt("weather-change-min-seconds", 300);
        int maxSeconds = mPlugin.getConfig().getInt("weather-change-max-seconds", 900);
        int durationSeconds = (minSeconds + new Random().nextInt(maxSeconds - minSeconds));
        int durationTicks = durationSeconds * TICKS_PER_SECOND;

        // Get a random weather type and modification
        WeatherType newType = getRandomElementFromArray(Weather.ALL_TYPES);
        WeatherMod newMod = getRandomElementFromArray(WeatherMod.values());

        // Change the weather
        Weather.changeWeather(mPlugin, mWorld, newType, newMod, durationTicks);

        // Repeat task to pick new weather once this one finishes
        WeatherChangeRunnable newRun = new WeatherChangeRunnable(mPlugin);
        BetterWeatherPlugin.weatherChangeRunnable = newRun;
        newRun.runTaskLater(mPlugin, Weather.currentDuration);

        log("** Random Weather Change **");
        log("Weather Duration (Ticks): %s", Weather.currentDuration);
        log("Weather Duration (Seconds): %s", Weather.currentDuration / TICKS_PER_SECOND);
        log("Weather Type: %s %s", Weather.currentMod, Weather.currentType);
    }

    private void log(String message, Object... args) {
        if (mPlugin.getConfig().getBoolean("debug", false)) {
            mPlugin.getLogger().info(String.format("[BetterWeather]" + message, args));
            mPlugin.getServer().broadcastMessage(String.format("[BetterWeather]" + message, args));
        }
    }

    private static <T> T getRandomElementFromArray(T[] arr) {
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }
}
