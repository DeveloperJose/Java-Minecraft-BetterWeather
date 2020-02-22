package io.github.developerjose.betterweather;

import io.github.developerjose.betterweather.runnable.RandomWeatherChangeRunnable;
import io.github.developerjose.betterweather.weathers.Hail;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        int durationSeconds = getConfig().getInt("weather-change-delay");
        int durationTicks = durationSeconds * 20;
        new RandomWeatherChangeRunnable(this).runTaskLater(this, durationTicks);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // Clear weather and effects
        World w = getServer().getWorlds().get(0);
        Util.clearWorldEffects(w);

        for (Player p : w.getPlayers())
            Util.clearPlayerEffects(p);
        
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent ev) {
        // Cancel the weather change if the plugin isn't the one who is changing it
        ev.setCancelled(!BWeatherManager.isPluginChangingWeather);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        boolean isHail = BWeatherManager.currentWeatherType instanceof Hail;
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
            getServer().broadcastMessage(String.format(ChatColor.DARK_AQUA + "[BWeather Debug] " + ChatColor.RESET + message, args));
    }

    public void sendPluginMessage(CommandSender sender, String message, Object... args) {
        sender.sendMessage(String.format(ChatColor.DARK_AQUA + "[BWeather] " + ChatColor.RESET + message, args));
    }
}
