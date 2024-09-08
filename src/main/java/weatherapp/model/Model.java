package weatherapp.model;

import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.event.SwingPropertyChangeSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weatherapp.enums.Changes;
import weatherapp.model.ip.IP;
import weatherapp.model.ip.IPGrabber;
import weatherapp.model.weather.SMHI;
import weatherapp.model.weather.Weather;
import weatherapp.ui.panel.WeatherPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Model} is the class that holds the data and logic for the application.
 * It is responsible for fetching weather data from the SMHI API and IP data from
 * the IPGrabber class. It also holds the settings for the application.
 * 
 * <p>The class implements the {@code Runnable} interface to run a thread that
 * refreshes the weather upon request. And reruns it every 5 seconds if it fails.
 * 
 * <p>It also uses the {@code SwingPropertyChangeSupport} class to notify the
 * Views of any changes in the data.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class Model implements Runnable {
    /**
     * The {@code SwingPropertyChangeSupport} object that notifies the Views of any changes.
     */
    private final SwingPropertyChangeSupport pcs;
    /**
     * The {@code SMHI} instance that fetches the 10 day weather forecast.
     */
    private final SMHI smhi;
    /**
     * The {@code Settings} object that holds the settings for the application.
     */
    private final Settings settings;
    /**
     * The {@code Thread} object that runs the refresh loop.
     */
    private final Thread thread;
    /**
     * The list of {@code WeatherPanel} objects that holds the weather data.
     */
    private List<WeatherPanel> weatherPanelsAdv;
    /**
     * The {@code IP} of the user.
     */
    private IP ip;
    
    /**
     * The boolean that tells the thread to refresh the weather data.
     */
    private volatile boolean refresh;

    private static final Logger logger = LogManager.getLogger(Model.class);
    
    /**
     * Constructs an instance of the {@code Model} class.
     * Default settings are:
     * <ul>
     *  <li>Metric: {@code true}</li>
     *  <li>Precision: {@code false}</li>
     *  <li>Dark mode: {@code true}</li>
     * </ul>
     * Thread is started upon construction.
     */
    public Model() {
        this(
            new Settings(
            true,
            false,
            true
            )
        );
    }
    /**
     * Constructs an instance of the {@code Model} class
     * using custom settings.
     * 
     * @param settings The settings for the application
     */
    public Model(Settings settings) {
        this.settings = settings;

        thread = new Thread(this);

        pcs = new SwingPropertyChangeSupport(this);
        smhi = new SMHI(settings);
        weatherPanelsAdv = new ArrayList<>();

        thread.start();
    }

    /**
     * Fires a property change event.
     * 
     * @param newValue the new value of the property
     * @param change the {@code Changes} enum that holds the property name
     */
    private void fireChange(boolean newValue, Changes change) {
        pcs.firePropertyChange(change.getChange(), !newValue, newValue);
    }
    /**
     * Add the view to the {@code SwingPropertyChangeSupport} object.
     * 
     * @param listener The view to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    /**
     * Remove the view from the {@code SwingPropertyChangeSupport} object.
     * 
     * @param listener The view to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Requests a refresh of the application including IP and weather data.
     */
    public void requestRefresh() {
        refresh = true;
        logger.info("Refreshing weather data");
        pcs.firePropertyChange(Changes.REFRESHING.getChange(), null, null);
    }

    /**
     * Refreshes the weather data.
     * 
     * @throws IOException if the IP or weather data could not be fetched eg. no internet connection
     */
    private void refresh() throws IOException {
        String URL = SMHI.generateURL(ip.getLon(), ip.getLat());
        smhi.setSettings(settings);
        List<Weather> weathers = smhi.getWeather10D(URL);

        weatherPanelsAdv.clear();
        for (Weather w : weathers) {
            weatherPanelsAdv.add(new WeatherPanel(w));
        }

        logger.info("\n{}{}", ip.toString(), URL);
        pcs.firePropertyChange(Changes.REFRESHED.getChange(), null, ip);
    }

    /**
     * Grabs the Public IP of the user and then the full IP.
     * 
     * @throws IOException if the IP could not be fetched eg. no internet connection
     */
    private void grabIP() throws IOException {
        String ipAdress = IPGrabber.grabIpAdress();
        ip = IPGrabber.grabFullIP(ipAdress);
    }

    /**
     * Changes the theme of the application.
     * From either light to dark or dark to light.
     */
    public void changeTheme() {
        settings.switchDarkMode();
        fireChange(settings.isDarkMode(), Changes.SETTINGS_UPDATED);
    }
    /**
     * Changes the precision of the units displayed.
     * From either double to int or int to double.
     */
    public void togglePrecision() {
        settings.switchPrecision();
        weatherPanelsAdv.forEach(w -> w.update(settings));
        fireChange(settings.hasPrecision(), Changes.SETTINGS_UPDATED);
    }
    /**
     * Change the system of the units displayed.
     * From either metric to imperial or imperial to metric.
     */
    public void toggleMetric() {
        settings.switchMetric();
        weatherPanelsAdv.forEach(w -> w.update(settings));
        fireChange(settings.isMetric(), Changes.SETTINGS_UPDATED);
    }
    
    /**
     * Returns the settings of the application.
     * 
     * @return The settings of the application.
     */
    public Settings getSettings() {
        return settings;
    }
    /**
     * Returns the list of {@code WeatherPanel} objects.
     * 
     * @return The list of {@code WeatherPanel} objects.
     */
    public List<WeatherPanel> getWeatherPanelsAdv() {
        return weatherPanelsAdv;
    }
    /**
     * Returns the IP of the user.
     * 
     * @return The IP of the user.
     */
    public IP getIp() {
        return ip;
    }

    /**
     * The run method of the thread that refreshes the weather data.
     * It grabs the IP and then refreshes the weather data.
     * If it fails it retries every 5 seconds.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if (refresh) {
                try {
                    grabIP();
                } catch (IOException e) {
                    logger.error("Failed to grab IP");
                    logger.info("Retrying in 5 seconds");
                    try {
                        Thread.sleep(5000);
                        continue;
                    } catch (InterruptedException e1) {}
                }
                
                try {
                    refresh();
                } catch (IOException e) {
                    logger.error("Failed to refresh weather data");
                    logger.info("Retrying in 5 seconds");
                    try {
                        Thread.sleep(5000);
                        continue;
                    } catch (InterruptedException e1) {}
                }
                refresh = false;
            }
        }
    }
}