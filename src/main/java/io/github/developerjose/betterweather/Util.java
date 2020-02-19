package io.github.developerjose.betterweather;

import java.util.Random;

public class Util {
    public static <T> T getRandomElementFromArray(T[] arr) {
        int idx = new Random().nextInt(arr.length);
        return arr[idx];
    }
}
