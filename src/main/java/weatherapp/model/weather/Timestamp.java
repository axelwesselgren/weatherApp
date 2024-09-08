package weatherapp.model.weather;

import java.time.LocalTime;

import weatherapp.enums.WeatherDay;
import weatherapp.enums.WeatherNight;
import weatherapp.interfaces.WeatherType;
import weatherapp.model.Settings;
import weatherapp.tools.UnitConverter;

/**
 * {@code Timestamp} is a class that contains weather data for a specific hour.
 * It extends {@code UnitConverter} to convert the data stored.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class Timestamp extends UnitConverter {
    private final double temp, windSpeed, gust;
    private final LocalTime time;
    /**
     * Weather type of the timestamp eg. Clear sky, rain, snow.
     */
    private final WeatherType weatherType;

    /**
     * Constructs a {@code Timestamp} instance with the given parameters.
     * It checks if the {@code WeatherType} is day or night and sets the correct weather type.
     * It also calls the super constructor to set the settings.
     * 
     * @param temp Temperature in Celsius as a {@code double}.
     * @param windSpeed Wind speed in m/s as a {@code double}.
     * @param gust Gust in m/s as a {@code double}.
     * @param time Time of the timestamp
     * @param weatherType The int corresponding to the weather type.
     * @param settings Settings object to use the settings.
     */
    public Timestamp(
        double temp, 
        double windSpeed, 
        double gust, 
        LocalTime time, 
        int weatherType,
        Settings settings
    ) {
        super(settings);
        this.temp = temp;
        this.time = time;
        this.windSpeed = windSpeed;
        this.gust = gust;

        if (isDay(time)) this.weatherType = WeatherDay.getWeather(weatherType);
        else this.weatherType = WeatherNight.getWeather(weatherType);
    }

    /**
     * Checks if the hour is daytime or nighttime.
     * 
     * @param time Time to check if it is day or night.
     * @return {@code true} if the time is between 6 and 20, {@code false} otherwise.
     */
    public static boolean isDay(LocalTime time) {
        int hour = time.getHour();
        if (hour > 5 && hour < 21) return true;
        return false;
    }

    /**
     * Returns the temperature in Celsius.
     * 
     * @return Temperature in Celsius as a {@code double}.
     */
    public double getTempC() {
        return temp;
    }
    /**
     * Returns the gust in m/s.
     * 
     * @return Gust in m/s as a {@code double}.
     */
    public double getGustD() {
        return gust;
    }
    /**
     * Returns the wind speed in m/s.
     * 
     * @return Wind speed in m/s as a {@code double}.
     */
    public double getWindSpeedD() {
        return windSpeed;
    }

    /**
     * Returns the time of the timestamp.
     * 
     * @return Time of the timestamp as a {@code LocalTime}.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Returns the temperature as a {@code String} based on the settings.
     * 
     * @return Temperature as a {@code String}.
     */
    public String getTemp() {
        return getTemp(temp);
    }
    /**
     * Returns the gust as a {@code String} based on the settings.
     * 
     * @return Gust as a {@code String}.
     */
    public String getGust() {
        return getWind(gust);
    }
    /**
     * Returns the wind speed as a {@code String} based on the settings.
     * 
     * @return Wind speed as a {@code String}.
     */
    public String getWindSpeed() {
        return getWind(windSpeed);
    }

    /**
     * Returns the weather type of the timestamp.
     * 
     * @return Weather type of the timestamp as a {@code WeatherType}.
     */
    public WeatherType getWeatherType() {
        return weatherType;
    }
    /**
     * Returns the file name of the weather type.
     * 
     * @return File name of the weather type as a {@code String}.
     */
    public String getWeatherFileName() {
        return weatherType.getFileName();
    }
}
