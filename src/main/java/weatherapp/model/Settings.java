package weatherapp.model;

/**
 * {@code Settings} holds the settings data for the application.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class Settings {
    private boolean metric, precision, darkMode;

    public Settings(boolean isMetric, boolean hasPrecision, boolean darkMode) {
        this.metric = isMetric;
        this.precision = hasPrecision;
        this.darkMode = darkMode;
    }

    public void switchMetric() {
        metric = !metric;
    }
    public void switchPrecision() {
        precision = !precision;
    }
    public void switchDarkMode() {
        darkMode = !darkMode;
    }

    public boolean isMetric() {
        return metric;
    }
    public boolean hasPrecision() {
        return precision;
    }
    public boolean isDarkMode() {
        return darkMode;
    }
}
