package weatherapp.model.weather;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weatherapp.model.Settings;
import weatherapp.model.json.JSONReader;

/**
 * {@code SMHI} is a class that contains methods to fetch weather data from SMHI API.
 * <p>
 * It fetches the weather data for the next 10 days and returns it as a {@code List} of {@code Weather} objects.
 * <p>
 * Uses {@code JSONReader} to read JSON from the URL.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class SMHI {
    /**
     * Date and Time of the iteration.
     */
    private String iterateDate, iterateTime;
    /**
     * List of {@code Weather} objects to return.
     */
    private List<Weather> weathers;
    /**
     * List of {@code Timestamp} objects to add to the {@code Weather} object.
     */
    private List<Timestamp> timestamps;
    /**
     * List of JSONObjects to store the timestamps.
     */
    private List<JSONObject> timestampsJSON;
    /**
     * Current JSONArray to iterate.
     */
    private JSONArray currentArray;
    /**
     * Current date of the API.
     */
    private LocalDate currentDate;
    /**
     * Settings object to use the settings.
     */
    private Settings settings;
    /**
     * Temperature, wind speed and gust of the iteration.
     */
    private double temp, windSpeed, gust;
    /**
     * Weather type of the iteration.
     */
    private int weatherType;

    /**
     * URL to fetch the weather data from.
     * {lon} and {lat} are placeholders for the longitude and latitude.
     */
    private static final String GEO_URL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/{lon}/lat/{lat}/data.json";
    /**
     * Constants for the JSON keys.
     */
    private static final String TIME_SERIES, VALID_TIME, VALUES, PARAMETERS;
    /**
     * Logger for the {@code SMHI} class.
     */
    private static final Logger LOGGER = LogManager.getLogger(SMHI.class);

    static {
        TIME_SERIES = "timeSeries";
        VALID_TIME = "validTime";
        VALUES = "values";
        PARAMETERS = "parameters";
    }

    /**
     * Constructs a new {@code SMHI} instance with the given settings.
     * 
     * @param settings settings to use.
     */
    public SMHI(Settings settings) {
        weathers = new ArrayList<>();
        timestamps = new ArrayList<>();
        timestampsJSON = new ArrayList<>();
    }

    /**
     * Sets the settings
     * 
     * @param settings settings to set.
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    
    /**
     * Clears all the lists 
     */
    public void reset() {
        weathers.clear();
        timestamps.clear();
        timestampsJSON.clear();
    }

    /**
     * Returns a 10 day weather forecast based on the given URL
     * 
     * @param URL URL to fetch the weather data from.
     * @return a 10 day weather forecast as a {@code List} of {@code Weather} objects.
     */
    public List<Weather> getWeather10D(String URL) {
        reset();

        updateJSONTimestamps(URL);
        LOGGER.info("JSON Timestamps read");
        updateCurrentDate();
        LOGGER.info("API Date: {}", currentDate.toString());

        for (JSONObject j : timestampsJSON) {
            updateIteration(j);
            findValues();
            checkIfNewDate();
            addTimestamp();
        }
        
        LOGGER.info("Weather Data read");
        return weathers;
    }

    /**
     * Updates the JSON timestamps from the given URL
     * 
     * @param url URL to fetch the JSON timestamps from.
     */
    private void updateJSONTimestamps(String url) {
        try {
            JSONArray timeSeries = JSONReader.readJsonFromURL(url).getJSONArray(TIME_SERIES);

            for (int i = 0; i < timeSeries.length(); i++) {
                timestampsJSON.add(timeSeries.getJSONObject(i));
            }
        } catch (IOException e) {
            LOGGER.error("Connection FAILED: {}", url);
        } catch (JSONException e) {
            LOGGER.error("JSON Code Error");
        }
    }

    /**
     * Retrieves the date of the API response and updates the {@code currentDate} to that date.
     */
    private void updateCurrentDate() {
        currentDate = LocalDate.parse(
            timestampsJSON.get(0)
                        .getString(VALID_TIME)
                        .split("T")
                        [0], 
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
    }

    /**
     * Updates the current iteration variables based on the given JSON object.
     * 
     * @param j JSON object to update the variables from.
     */
    private void updateIteration(JSONObject j) {
        String[] dateTime = j.getString(VALID_TIME).split("T");
        iterateDate = dateTime[0];
        iterateTime = dateTime[1].replace("Z", "");

        currentArray = j.getJSONArray(PARAMETERS);
        weatherType = currentArray.getJSONObject(18)
                                    .getJSONArray(VALUES)
                                    .getInt(0);
    }

    /**
     * Searches for values in the current timestamp (JSON object) and updates the variables.
     */
    private void findValues() {
        for (int i = 0; i < currentArray.length(); i++) {
            JSONObject tempJ = currentArray.getJSONObject(i);
            String tempJStr = tempJ.getString("name");
            JSONArray values = tempJ.getJSONArray(VALUES);
            
            if (tempJStr.equals("t")) temp = values.getDouble(0);
            if (tempJStr.equals("ws")) windSpeed = values.getDouble(0);
            if (tempJStr.equals("gust")) gust = values.getDouble(0);   
        }
    }

    /**
     * Checks if the current timestamp iteration is a new date.
     * If it is, it creates a new {@code Weather} object and ads it to the {@code weathers} list.
     * It also updates the {@code currentDate} to the new date.
     * And clears the {@code timestamps} list.
     */
    private void checkIfNewDate() {
        if (!currentDate.toString().equals(iterateDate)) {
            weathers.add(new Weather(currentDate, timestamps, settings));
            currentDate = LocalDate.parse(iterateDate);

            timestamps = new ArrayList<>();
        }
    }
    
    /**
     * Adds a new {@code Timestamp} object to the {@code timestamps} list
     * based on the current iteration variables.
     */
    private void addTimestamp() {
        timestamps.add(
            new Timestamp(
                temp, 
                windSpeed, 
                gust, 
                LocalTime.parse(iterateTime), 
                weatherType,
                settings
            )
        );
    }

    /**
     * Generates a custom URL based on the given longitude and latitude.
     * The URL can be used to fetch weather data from SHMI API.
     * 
     * @param lon longitude
     * @param lat latitude
     * @return a custom URL as a {@code String}.
     */
    public static String generateURL(double lon, double lat) {
        String rs = GEO_URL;
        rs = rs.replace("{lon}", String.valueOf(lon));
        rs = rs.replace("{lat}", String.valueOf(lat));
        return rs;
    }
}