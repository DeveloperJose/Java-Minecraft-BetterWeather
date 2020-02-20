package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.HailRunnable;
import io.github.developerjose.betterweather.runnable.RandomWeatherChangeRunnable;
import io.github.developerjose.betterweather.runnable.WindRunnable;
import io.github.developerjose.betterweather.weathers.BWeatherType;
import io.github.developerjose.betterweather.weathers.BWeatherTypePair;
import io.github.developerjose.betterweather.weathers.Hail;
import io.github.developerjose.betterweather.weathers.LightWind;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class BWeather {
    public static boolean isPluginChangingWeather = false;

    public static BWeatherType currentType = BWeatherType.CLEAR;
    public static int currentDuration = 0;

    public static Vector windDirection = new Vector(0, 0, 0);

    public static void changeWeather(BetterWeatherPlugin plugin, BWeatherType newType, int durationTicks) {
        // Update static variables
        isPluginChangingWeather = true;
        currentType = newType;
        currentDuration = durationTicks;

        // Cancel all previous tasks
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTasks(plugin);

        // Set weather duration and clear previous if necessary
        World w = plugin.getServer().getWorlds().get(0);
        w.setThundering(false);
        w.setStorm(false);
        w.setWeatherDuration(durationTicks);
        w.setThunderDuration(durationTicks);

        // Clear previous effects
        for (Player p : w.getPlayers()) {
            p.removePotionEffect(PotionEffectType.SLOW);
            p.removePotionEffect(PotionEffectType.WEAKNESS);
        }

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

        // Create extra effect if necessary
        if (newType instanceof LightWind || newType instanceof BWeatherTypePair)
            new WindRunnable(plugin).runTask();
        else if (newType instanceof Hail)
            new HailRunnable(plugin).runTask();

        // Get the delay between weather changes
        int changeDelayTicks = durationTicks;
        if (durationTicks > 0)
            changeDelayTicks += plugin.getConfig().getInt("weather-change-delay") * 20;

        // Repeat task to pick new weather
        new RandomWeatherChangeRunnable(plugin).runTaskLater(plugin, changeDelayTicks);

        // Announce the new weather
        String weatherMessage = newType.getConfigBroadcastMessage(plugin.getConfig());
        if (weatherMessage.length() > 0)
            plugin.getServer().broadcastMessage(weatherMessage);

        // Debug logging of weather change and information
        plugin.log("Weather changed to %s for %s ticks, (%s sec), (%s min)",
                newType, BWeather.currentDuration, BWeather.currentDuration / 20, BWeather.currentDuration / 20 / 60);

        isPluginChangingWeather = false;
    }

    public static PotionEffect makePotionEffect(PotionEffectType t, int level) {
        return new PotionEffect(t, currentDuration - 20, level);
    }
}
