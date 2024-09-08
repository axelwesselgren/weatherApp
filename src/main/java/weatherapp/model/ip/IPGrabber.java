package weatherapp.model.ip;

import java.io.IOException;

import weatherapp.model.json.JSONReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@code IPGrabber} is a class that contains methods fetch IP and information based of that IP.
 * <p>
 * It uses IPify API to fetch the IP and IPInfo API to fetch the information based on the IP.
 * <p>
 * Uses {@code JSONReader} to read JSON from the URL.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class IPGrabber {
    /**
     * URL to fetch the IP.
     */
    private final static String IP_URL = "https://api.ipify.org?format=json";
    /**
     * URL to fetch the information based on the public IP.
     * {ip} is a placeholder for the IP address.
     */
    private final static String GEO_URL = "https://ipinfo.io/{ip}/json";


    /**
     * Fetches public IP of the user from the URL {@value #IP_URL}.
     * 
     * @return public IP of the user as a {@code String}.
     * @throws IOException if an I/O error occurs, usually lack of internet connection.
     */
    public static String grabIpAdress() throws IOException {
        return JSONReader.readJsonFromURL(IP_URL).getString("ip");
    }

    /**
     * Fetches full information of an user based on the given IP
     * from the URL {@value #GEO_URL}.
     * 
     * @param ipAdress IP address of the user as a {@code String}.
     * @return information of the user as an {@code IP} object.
     * @throws JSONException if the JSON is not valid or invalid code to read the JSON.
     * @throws IOException if an I/O error occurs, usually lack of internet connection.
     */
    public static IP grabFullIP(String ipAdress) throws JSONException, IOException {
        JSONObject fullIpJson = JSONReader.readJsonFromURL(GEO_URL.replace("{ip}", ipAdress));
        String[] geo = fullIpJson.getString("loc").split(",");

        return new IP(
            ipAdress, 
            fullIpJson.getString("city"), 
            fullIpJson.getString("region"), 
            fullIpJson.getString("country"), 
            fullIpJson.getString("org"), 
            Double.parseDouble(geo[0]), 
            Double.parseDouble(geo[1])
        );
    }
}
