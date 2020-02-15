package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.ConstantEffectRunnable;
import io.github.developerjose.betterweather.runnable.WeatherChangeRunnable;
import io.github.developerjose.betterweather.weathers.Hail;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

/**
 * BetterWeather plugin inspired by https://bukkit.org/threads/better-weather.482739/
 *
 * @author DeveloperJose
 */
public class BetterWeatherPlugin extends JavaPlugin implements Listener {
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

        // Start a weather change sometime in the future
        int minSeconds = getConfig().getInt("weather-change-min-seconds");
        int maxSeconds = getConfig().getInt("weather-change-max-seconds");
        int durationSeconds = (minSeconds + new Random().nextInt(maxSeconds - minSeconds));
        int durationTicks = durationSeconds * 20;
        new WeatherChangeRunnable(this).runTaskLater(this, durationTicks);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent ev) {
        if (!Weather.isPluginChangingWeather) {
            log("Minecraft is trying to change the weather!");
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        boolean isHail = Weather.currentType instanceof Hail;
        if (!isHail)
            return;

        boolean isCustomDamage = ev.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM;
        if (!isCustomDamage)
            return;

        String deathMessage = getConfig().getString("hail-death-message");
        deathMessage = deathMessage.replace("PLAYER", ev.getEntity().getDisplayName());
        deathMessage = ChatColor.translateAlternateColorCodes('&', deathMessage);
        ev.setDeathMessage(deathMessage);
    }

    public void log(String message, Object... args) {
        if (getConfig().getBoolean("debug"))
            getServer().broadcastMessage(String.format(ChatColor.DARK_AQUA + "[BetterWeather]" + ChatColor.GREEN + message, args));
    }

    public void sendMessage(CommandSender sender, String message, Object... args) {
        sender.sendMessage(String.format(ChatColor.DARK_AQUA + "[BetterWeather]" + ChatColor.GREEN + message, args));
    }
}
