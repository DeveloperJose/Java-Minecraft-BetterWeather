package io.github.developerjose.betterweather;

import org.bukkit.entity.Player;

import java.util.Random;

public class Util {
    public static <T> T getRandomElementFromArray(T[] arr) {
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }

    public static boolean isPlayerUnderBlockCover(Player p){
        int blockAbovePlayerY = p.getLocation().getBlockY() + 1;
        int highestBlockY = p.getWorld().getHighestBlockYAt(p.getLocation());
        return blockAbovePlayerY <= highestBlockY;
    }
}
