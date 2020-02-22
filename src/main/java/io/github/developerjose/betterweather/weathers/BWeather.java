package io.github.developerjose.betterweather.weathers;

public class BWeather {
    public static BWeatherType CLEAR = new Clear();
    public static BWeatherType HAIL = new Hail();

    public static BWeatherType HEAVY_RAIN = new HeavyRain();
    public static BWeatherType LIGHT_RAIN = new LightRain();

    public static BWeatherType HEAVY_SNOW = new HeavySnow();
    public static BWeatherType LIGHT_SNOW = new LightSnow();

    public static BWeatherType HEAVY_WIND = new HeavyWind();
    public static BWeatherType LIGHT_WIND = new LightWind();

    public static BWeatherType LIGHT_WIND_LIGHT_RAIN = new BWeatherTypePair(LIGHT_WIND, LIGHT_RAIN);
    public static BWeatherType HEAVY_WIND_LIGHT_RAIN = new BWeatherTypePair(HEAVY_WIND, LIGHT_RAIN);
    public static BWeatherType HEAVY_WIND_HEAVY_RAIN = new BWeatherTypePair(HEAVY_WIND, HEAVY_RAIN); // Alias: Thunderstorm

    public static BWeatherType LIGHT_WIND_LIGHT_SNOW = new BWeatherTypePair(LIGHT_WIND, LIGHT_SNOW);
    public static BWeatherType HEAVY_WIND_HEAVY_SNOW = new BWeatherTypePair(HEAVY_WIND, HEAVY_SNOW); // Alias: Blizzard

    public static final BWeatherType[] ALL_TYPES = new BWeatherType[]{
            CLEAR, HAIL,
            LIGHT_RAIN, LIGHT_WIND, LIGHT_SNOW,
            HEAVY_RAIN, HEAVY_WIND, HEAVY_SNOW,
            LIGHT_WIND_LIGHT_RAIN, HEAVY_WIND_LIGHT_RAIN, HEAVY_WIND_HEAVY_RAIN,
            LIGHT_WIND_LIGHT_SNOW, HEAVY_WIND_HEAVY_SNOW
    };

    public static final BWeatherType[] AFTER_HAIL = new BWeatherType[]{
            CLEAR, HEAVY_RAIN, HEAVY_SNOW
    };
}