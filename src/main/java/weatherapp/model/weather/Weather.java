package weatherapp.model.weather;

import java.time.LocalDate;
import java.util.List;

import weatherapp.interfaces.WeatherType;
import weatherapp.model.Settings;
import weatherapp.tools.UnitConverter;

/**
 * {@code Weather} is a class that contains weather data for a specific day.
 * It extends {@code UnitConverter} to convert the data stored.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class Weather extends UnitConverter {
    /**
     * List of timestamps for the day as {@code Timestamp}.
     */
    private final List<Timestamp> timestamps;
    /**
     * Date of the weather data.
     */
    private final LocalDate date;
    /**
     * Weather type of the day eg. Clear sky, rain, snow.
     */
    private WeatherType weatherType;
    private double minT, maxT, windSpeed, gust;

    /**
     * Constructs a {@code Weather} instance with the given parameters.
     * It calculates the max and min temperature, wind speed and weather type.
     * It also calls the super constructor to set the settings.
     * 
     * @param date Date of the Weather forecast.
     * @param timestamps List of timestamps for the day as {@code Timestamp}.
     * @param settings Settings object to use the settings.
     */
    public Weather(
        LocalDate date, 
        List<Timestamp> timestamps, 
        Settings settings
    ) {
        super(settings);
        this.date = date;
        this.timestamps = timestamps;

        findMaxMin();
        calcWindSpeed();
        findWeatherType();
    }

    /**
     * Finds the max and min temperature and gust for the day.
     * Uses streams to find the max and min values via the {@code Timestamp} and its methods 
     * {@code getTempC()}, {@code getGustD()}.
     */
    private void findMaxMin() {
        minT = timestamps.stream()
            .mapToDouble(Timestamp::getTempC)
            .min()
            .orElse(Double.MIN_VALUE);
            
        maxT = timestamps.stream()
            .mapToDouble(Timestamp::getTempC)
            .max()
            .orElse(Double.MAX_VALUE);

        gust = timestamps.stream()
            .mapToDouble(Timestamp::getGustD)
            .max()
            .orElse(Double.MAX_VALUE);
    }

    /**
     * Calculates the average wind speed for the day
     * via the {@code timestamps} list.
     */
    private void calcWindSpeed() {
        timestamps.forEach(t -> windSpeed += t.getWindSpeedD());
        windSpeed /= timestamps.size();
    }

    /**
     * Finds the weather type for the day.
     * If the date is today, the weather type is set to the first timestamp.
     * Else if the hour 14 exists the weather type is set to the timestamp with that hour.
     * At last if the hour 12 exists the weather type is set to the timestamp with that hour.
     */
    private void findWeatherType() {
        if (checkDate()) return;

        if (checkHour(14)) return;
        if (checkHour(12)) return;

        weatherType = timestamps.get(0).getWeatherType();
    }
    /**
     * Checks if the Weather forecast date is the same as the local date.
     * If so the weather type is set to the first timestamp.
     * 
     * @return {@code true} if the date is the same as the local date, {@code false} otherwise.
     */
    private boolean checkDate() {
        if (date.equals(LocalDate.now())) {
            weatherType = timestamps.get(0).getWeatherType();
        }
        return weatherType != null;
    }
    /**
     * Checks if the hour exists in the timestamps list.
     * If so the weather type is set to the timestamp with that hour.
     * 
     * @param hour Hour to check if it exists in the timestamps list.
     * @return {@code true} if the hour exists in the timestamps list, {@code false} otherwise.
     */
    private boolean checkHour(int hour) {
        weatherType = timestamps.stream()
            .filter(t -> t.getTime().getHour() == hour)
            .map(Timestamp::getWeatherType)
            .findFirst()
            .orElse(null);

        return weatherType != null;
    }

    /**
     * Returns the max gust with the correct unit using {@code settings}.
     * 
     * @return Max gust using the correct unit via {@code settings}.
     */
    public String getGustMax() {
        return getWind(gust);
    }
    /**
     * Returns the average wind speed with the correct unit using {@code settings}.
     * 
     * @return Average wind speed using the correct unit via {@code settings}.
     */
    public String getWindSpeedAvg() {
        return getWind(windSpeed);
    }
    /**
     * Returns the max and min temperature with the correct unit using {@code settings}.
     * 
     * @return Max temperature using the correct unit via {@code settings}.
     */
    public String getMaxT() {
        return getTemp(maxT);
    }
    /**
     * Returns the min temperature with the correct unit using {@code settings}.
     * 
     * @return Min temperature using the correct unit via {@code settings}.
     */
    public String getMinT() {
        return getTemp(minT);
    }

    /**
     * Returns the date of the weather forecast.
     * 
     * @return Date of the weather forecast.
     */
    public LocalDate getDate() {
        return date;
    }
    /**
     * Returns the timestamps list.
     * 
     * @return List of timestamps for the day as {@code Timestamp}.
     */
    public List<Timestamp> getTimeStamps() {
        return timestamps;
    }
    /**
     * Returns the weather type of the day.
     * 
     * @return Weather type of the day.
     */
    public WeatherType getWeatherType() {
        return weatherType;
    }

    /**
     * Updates the settings for the weather and all timestamps.
     */
    @Override
    public void setSettings(Settings settings) {
        super.setSettings(settings);
        timestamps.forEach(t -> t.setSettings(settings));
    }
    /**
     * Returns a string representation of the {@code Weather} object.
     * 
     * @return String representation of the {@code Weather} object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Max T: " + maxT + "\n");
        sb.append("Min T: " + minT + "\n");
        sb.append("Date: " + date + "\n\n");

        timestamps.forEach(t -> {
            sb.append(t.getTime() + " Temp: " + t.getTempC() + "\n");
        });

        return sb.toString();
    }
}
