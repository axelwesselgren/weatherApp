package weatherapp.enums;

import weatherapp.interfaces.WeatherType;

/**
 * {@code WeatherNight} is an enum that contains the
 * different types of weathers during an hour/day
 * at nighttime.
 * 
 * <p>
 * Every enum constant contains an id and a file name
 * corresponding to the image of the weather.
 * </p>
 * 
 * <p>
 * It implements the {@link WeatherType} interface.
 * <p>
 * 
 * @author Axel Lönnby Wesselgren
 */
public enum WeatherNight implements WeatherType {
    FILE_1(1, "day/1.png"),
    FILE_2(2, "day/2.png"),
    FILE_3(3, "day/3.png"),
    FILE_4(4, "day/4.png"),
    FILE_5(5, "day/5.png"),
    FILE_6(6, "day/6.png"),
    FILE_7(7, "day/7.png"),
    FILE_8(8, "day/8.png"),
    FILE_9(9, "day/9.png"),
    FILE_10(10, "day/10.png"),
    FILE_11(11, "day/11.png"),
    FILE_12(12, "day/12.png"),
    FILE_13(13, "day/13.png"),
    FILE_14(14, "day/14.png"),
    FILE_15(15, "day/15.png"),
    FILE_16(16, "day/16.png"),
    FILE_17(17, "day/17.png"),
    FILE_18(18, "day/18.png"),
    FILE_19(19, "day/19.png"),
    FILE_20(20, "day/20.png"),
    FILE_21(21, "day/21.png"),
    FILE_22(22, "day/22.png"),
    FILE_23(23, "day/23.png"),
    FILE_24(24, "day/24.png"),
    FILE_25(25, "day/25.png"),
    FILE_26(26, "day/26.png"),
    FILE_27(27, "day/27.png");

    /**
     * Identifier of the weather.
     */
    public final int id;

    /**
     * Path to the image located in /resources/weather/night.
     */
    public final String fileName;

    /**
     * Constructor for the {@code WeatherNight}.
     * 
     * @param id The identifier of the weather.
     * @param fileName The path to the image.
     */
    private WeatherNight(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    /**
     * Get the {@code WeatherNight} constant based on the id.
     * 
     * @param id The identifier of the weather.
     * @return The WeatherDay corresponding to the id.
     */
    public static WeatherNight getWeather(int id) {
        for (WeatherNight w : WeatherNight.values()) {
            if (w.id == id) return w;
        }
        return null;
    }

    /**
     * Get the path to the image.
     * 
     * @return The path to the image.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the identifier of the weather.
     * 
     * @return The identifier of the weather.
     */
    public int getId() {
        return id;
    }
}
