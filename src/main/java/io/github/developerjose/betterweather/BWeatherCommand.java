package io.github.developerjose.betterweather;

import com.google.common.collect.ImmutableList;
import io.github.developerjose.betterweather.weathers.BWeatherType;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BWeatherCommand implements CommandExecutor, TabCompleter {
    private static final List<String> COMMANDS = ImmutableList.of("start", "clear", "reload", "help");
    private static final List<String> WEATHER_TYPES = ImmutableList.of("clear", "hail",
            "light-rain", "light-wind", "light-snow",
            "heavy-rain", "heavy-wind", "heavy-snow",
            "light-wind-light-rain", "heavy-wind-light-rain", "heavy-wind-heavy-rain", "thunderstorm",
            "light-wind-light-snow", "heavy-wind-heavy-snow", "blizzard");

    private BetterWeatherPlugin mPlugin;

    public BWeatherCommand(BetterWeatherPlugin plugin) {
        mPlugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        // Check permissions and if not allowed, don't say anything
        if (!commandSender.hasPermission("bweather"))
            return true;

        if (args.length < 1)
            return false;

        // Remove the command name from the list of arguments
        List<String> argsList = new ArrayList(Arrays.asList(args));
        argsList.remove(0);

        // Check which command was performed
        String strCommand = args[0];
        if (strCommand.equalsIgnoreCase("start"))
            return onStartCommand(commandSender, argsList);
        else if (strCommand.equalsIgnoreCase("clear"))
            return onStartCommand(commandSender, Arrays.asList("clear"));
        else if (strCommand.equalsIgnoreCase("reload"))
            return onReloadCommand(commandSender);
        else if (strCommand.equalsIgnoreCase("help"))
            return onHelpCommand(commandSender);

        return false;
    }

    private boolean onStartCommand(CommandSender commandSender, List<String> args) {
        if (args.size() >= 1) {
            // Check if weather is valid
            String rawWeatherName = args.get(0).toLowerCase();
            if (!WEATHER_TYPES.contains(rawWeatherName)) {
                mPlugin.sendPluginMessage(commandSender, ChatColor.RED + "%s is not a valid weather type.", rawWeatherName);
                return false;
            }

            // Split the name and mod if necessary
            // Get the weather object
            BWeatherType newWeather = BWeatherType.fromString(rawWeatherName);
            if (newWeather == null) {
                mPlugin.sendPluginMessage(commandSender, ChatColor.RED + "An error has occurred while converting %s into a WeatherType.", rawWeatherName);
                return false;
            }

            // Get the duration from the arguments if provided, or use the default plugin duration
            int durationTicks = newWeather.getConfigWeatherDuration(mPlugin.getConfig());
            if (args.size() >= 2) {
                String rawDurationSeconds = args.get(1);
                try {
                    durationTicks = Integer.parseInt(rawDurationSeconds) * 20;
                } catch (NumberFormatException ex) {
                    mPlugin.sendPluginMessage(commandSender, ChatColor.RED + "Duration must be an integer");
                    return false;
                }
            }

            // Change the weather
            BWeather.changeWeather(mPlugin, newWeather, durationTicks);

            // String broadcastMessage = String.format("[BWeather] Weather changed to %s for %s sec (%s min)", rawWeatherName, durationTicks / 20, durationTicks / 20 / 60);
            // Command.broadcastCommandMessage(commandSender, broadcastMessage);
            return true;
        }
        return true;
    }

    private boolean onReloadCommand(CommandSender sender) {
        mPlugin.reloadConfig();
        mPlugin.sendPluginMessage(sender, "Reloaded configuration file.");
        return true;
    }

    private boolean onHelpCommand(CommandSender sender) {
        mPlugin.sendPluginMessage(sender, ChatColor.GOLD + "Plugin commands:");
        sender.sendMessage(ChatColor.AQUA + "/bweather start <weather> [duration (sec)]\n" +
                ChatColor.GREEN + "   Starts the specified weather type. If duration is not given, the default is retrieved from the configuration.");

        sender.sendMessage(ChatColor.AQUA + "/bweather clear\n" +
                ChatColor.GREEN + "   Changes the current weather to clear weather.");

        sender.sendMessage(ChatColor.AQUA + "/bweather reload\n" +
                ChatColor.GREEN + "   Reloads the configuration file.");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        // Check permissions and if not allowed, don't say anything
        if (!sender.hasPermission("bweather"))
            return ImmutableList.of();

        // Hint the commands
        if (args.length == 1)
            return StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<String>(COMMANDS.size()));

        // Hint the arguments depending on the command
        if (args.length >= 2) {
            String strCommand = args[0];
            if (strCommand.equalsIgnoreCase("start") && args.length == 2)
                return StringUtil.copyPartialMatches(args[1], WEATHER_TYPES, new ArrayList<String>(WEATHER_TYPES.size()));

            if (args.length == 3)
                return ImmutableList.of("[duration (sec)]");
        }

        return ImmutableList.of();
    }

}
