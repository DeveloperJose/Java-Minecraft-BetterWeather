package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.ConstantEffectRunnable;
import io.github.developerjose.betterweather.runnable.WeatherChangeRunnable;
import io.github.developerjose.betterweather.weathers.Hail;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * BetterWeather plugin inspired by https://bukkit.org/threads/better-weather.482739/
 * @author DeveloperJose
 */
public class BetterWeatherPlugin extends JavaPlugin implements Listener {
    public static WeatherChangeRunnable weatherChangeRunnable;
    public static ConstantEffectRunnable constantEffectRunnable;

    @Override
    public void onEnable() {
        super.onEnable();

        // Config
        saveDefaultConfig();

        // Events
        getServer().getPluginManager().registerEvents(this, this);

        // Commands
        BWeatherCommand bWeatherCommand = new BWeatherCommand(this);
        getCommand("bweather").setExecutor(bWeatherCommand);
        getCommand("bweather").setTabCompleter(bWeatherCommand);

        // Set-up runnables
        weatherChangeRunnable = new WeatherChangeRunnable(this);
        constantEffectRunnable = new ConstantEffectRunnable(this);

        // Start a weather change sometime in the future
        int minSeconds = getConfig().getInt("weather-change-min-seconds");
        int maxSeconds = getConfig().getInt("weather-change-max-seconds");
        int durationSeconds = (minSeconds + new Random().nextInt(maxSeconds - minSeconds));
        int durationTicks = durationSeconds * 20;
        weatherChangeRunnable.runTaskLater(this, durationTicks);
        constantEffectRunnable.runTaskLater(this, durationTicks);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        boolean isHail = Weather.currentType instanceof Hail;
        if (!isHail)
            return;

        boolean isCustomDamage = ev.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM;
        if (!isCustomDamage)
            return;

        ev.setDeathMessage(ChatColor.WHITE + ev.getEntity().getName() + ChatColor.RESET + " has been killed by " + ChatColor.GRAY + "Hail");
    }
}
