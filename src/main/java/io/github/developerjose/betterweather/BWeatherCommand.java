package io.github.developerjose.betterweather;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BWeatherCommand implements CommandExecutor, TabCompleter {
    private static final List<String> COMMANDS = ImmutableList.of("start", "clear", "modifier");
    private static final List<String> WEATHER_TYPES = ImmutableList.of(
            "clear", "hail", "light-rain", "light-wind", "light-windyrain", "heavy-rain", "heavy-wind", "heavy-windyrain");

    private JavaPlugin mPlugin;

    public BWeatherCommand(JavaPlugin plugin) {
        mPlugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
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
            return onStartCommand(commandSender, Arrays.asList(new String[]{"clear"}));

        return false;
    }

    private boolean onStartCommand(CommandSender commandSender, List<String> args) {
        if (args.size() >= 1) {
            // Check if weather is valid
            String rawWeatherName = args.get(0).toLowerCase();
            if (!WEATHER_TYPES.contains(rawWeatherName)) {
                commandSender.sendMessage("Weather type is invalid");
                return false;
            }

            // Split the name and mod if neccesary
            String weatherName = rawWeatherName;
            if (weatherName.contains("-"))
                weatherName = weatherName.substring(weatherName.indexOf("-"));

            WeatherMod newMod = WeatherMod.LIGHT;
            if (rawWeatherName.contains("heavy"))
                newMod = WeatherMod.HEAVY;

            // Get the weather object
            WeatherType newWeather = Weather.weatherFromString(weatherName);

            // Get the duration from the arguments if provided, or use the default plugin duration
            int durationTicks = newWeather.getConfigWeatherDuration(mPlugin.getConfig());
            if (args.size() >= 2) {
                String rawDurationSeconds = args.get(1);
                try {
                    durationTicks = Integer.parseInt(rawDurationSeconds) * 20;
                } catch (NumberFormatException ex) {
                    commandSender.sendMessage("Duration must be an integer.");
                    return false;
                }
            }

            // Change the weather
            Weather.changeWeather(mPlugin, newWeather, newMod, durationTicks);
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        // Hint the commands
        if (args.length == 1)
            return StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<String>(COMMANDS.size()));

        // Hint the arguments depending on the command
        if (args.length == 2) {
            String strCommand = args[0];
            if (strCommand.equalsIgnoreCase("start"))
                return StringUtil.copyPartialMatches(args[1], WEATHER_TYPES, new ArrayList<String>(WEATHER_TYPES.size()));
        }

        return ImmutableList.of();
    }

}
