package io.github.developerjose.betterweather;

import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Util {
    public static <T> T getRandomElementFromArray(T[] arr) {
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }

    public static boolean isPlayerUnderBlockCover(Player p) {
        int blockAbovePlayerY = p.getLocation().getBlockY() + 1;
        int highestBlockY = p.getWorld().getHighestBlockYAt(p.getLocation());
        return blockAbovePlayerY <= highestBlockY;
    }

    public static void clearPlayerEffects(Player p) {
        p.removePotionEffect(PotionEffectType.SLOW);
        p.removePotionEffect(PotionEffectType.WEAKNESS);
        p.stopSound(Sound.ITEM_ELYTRA_FLYING);
        p.setPlayerWeather(WeatherType.CLEAR);
    }

    public static void clearWorldEffects(World w){
        w.setThundering(false);
        w.setStorm(false);
        w.setWeatherDuration(0);
        w.setThunderDuration(0);
    }
}
