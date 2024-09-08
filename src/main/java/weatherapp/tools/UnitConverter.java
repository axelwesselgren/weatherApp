package weatherapp.tools;

import weatherapp.model.Settings;

/**
 * {@code UnitConverter} is a class to convert units of various types.
 * It converts units based on the settings of the application.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class UnitConverter {
    /**
     * The {@code Settings} object that holds the settings for the application.
     */
    private Settings settings;
    /**
     * The conversion factor from m/s to mph.
     */
    public static final double MPH = 2.23694;

    /**
     * Constructs an instance of the {@code UnitConverter} class.
     * 
     * @param settings The settings for the application.
     */
    public UnitConverter (Settings settings) {
        this.settings = settings;
    }

    /**
     * Updates the settings of the {@code UnitConverter} object.
     * 
     * @param settings The new settings for the application.
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * Converts temperature from Celsius to Fahrenheit.
     * 
     * @param temp The temperature in Celsius to convert.
     * @return The temperature in Fahrenheit.
     */
    private double cToF(double temp) {
        return temp * 1.8 + 32;
    }
    /**
     * Converts speed from km/h to mph.
     * 
     * @param d The speed in km/h to convert.
     * @return The speed in mph.
     */
    private double kmToMph(double d) {
        return d * MPH;
    }
    /**
     * Converts a double to an integer by rounding it.
     * 
     * @param d The double to convert.
     * @return The integer value of the double.
     */
    private int doubleToInt(double d) {
        return (int) Math.round(d);
    }

    /**
     * Converts the wind speed to the correct unit represented as a {@code String}
     * using the settings of the application.
     * 
     * @param value The wind speed to convert.
     * @return The wind speed in the correct unit  as a {@code String}.
     */
    public String getWind(double value) {
        return getString(
            value,
            doubleToInt(value),
            kmToMph(value),
            doubleToInt(kmToMph(value)),
            false
        );
    }

    /**
     * Converts the temperature to the correct unit represented as a {@code String}
     * using the settings of the application.
     * 
     * @param value The temperature to convert.
     * @return The temperature in the correct unit as a {@code String}.
     */
    public String getTemp(double value) {
        return getString(
            value,
            doubleToInt(value),
            cToF(value),
            doubleToInt(cToF(value)),
            true
        );
    }

    /**
     * Returns a formattes {@code String} based on the given parameters and settings.
     * If the settings are metric, the metric values are used, otherwise the imperial values are used.
     * Same with precision, if the settings have precision, the double values are used, otherwise the integer values are used.
     * 
     * @param doubleMetric The value in metric units and with precision.
     * @param intMetric The value in metric units and without precision.
     * @param doubleImperial The value in imperial units and with precision.
     * @param intImperial The value in imperial units and without precision.
     * @param isTemp If the value is a temperature or not.
     * @return The formatted {@code String}.
     */
    private String getString(double doubleMetric, int intMetric, double doubleImperial, int intImperial, boolean isTemp) {
        String valueString;
        String unit;
    
        if (settings.isMetric()) {
            valueString = settings.hasPrecision() ? String.format("%.1f", doubleMetric) : String.valueOf(intMetric);
            unit = isTemp ? "°C" : " m/s";
        } else {
            valueString = settings.hasPrecision() ? String.format("%.1f", doubleImperial) : String.valueOf(intImperial);
            unit = isTemp ? "°F" : " mph";
        }
        
        return valueString + unit;
    }
}
