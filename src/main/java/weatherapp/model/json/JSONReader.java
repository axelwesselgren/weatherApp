package weatherapp.model.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URI;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@code JSONReader} is a class that contains a method to fetch and read JSON from a URL.
 * The JSON is read and then returned as a {@code JSONObject}.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class JSONReader {
    /**
     * Logger for the {@code JSONReader} class.
     */
    private static final Logger logger = LogManager.getLogger(JSONReader.class);
    
    /**
     * Fetches JSON from the given URL param and returns it as a {@code JSONObject}.
     * 
     * @param url URL to fetch the JSON from.
     * @return JSON as a {@code JSONObject}.
     * @throws IOException if an I/O error occurs, usually lack of internet connection.
     * @throws JSONException if the JSON is not valid or invalid code to read the JSON.
     */
    public static JSONObject readJsonFromURL(String url) throws IOException, JSONException {
        InputStream inputStream = URI.create(url).toURL().openStream();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            String jsonText = "";
            int c;

            while ((c = reader.read()) != -1) {
                jsonText += (char) c;
            }

            logger.info("Connection Established: " + url);
            return new JSONObject(jsonText);
        }
    }
}
