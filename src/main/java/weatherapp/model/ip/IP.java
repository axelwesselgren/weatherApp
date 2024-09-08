package weatherapp.model.ip;

/**
 * {@code IP} is a data class that contains the information related to an IP.
 * @author Axel Lönnby Wesselgren
 */
public class IP {
    /**
     * The latitude and longitude of the IP.
     */
    private final double lat, lon;

    /**
     * The IP address, city, region, country, and ISP of the IP.
     */
    private final String ipAdress, city, region, country, isp;

    /**
     * Constructs a new instance of {@code IP} with the given IP address, city, region, country, ISP, latitude, and longitude.
     * 
     * @param ipAdress Public IP address
     * @param city City
     * @param region Region
     * @param country Country Code
     * @param isp Internet Service Provider
     * @param lat Latitude
     * @param lon Longitude
     */
    public IP(
        String ipAdress, 
        String city, 
        String region,
        String country,
        String isp,
        double lat,
        double lon
    ) {
        this.ipAdress = ipAdress;
        this.city = city;
        this.region = region;
        this.country = country;
        this.isp = isp;

        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Returns the city of the IP.
     * 
     * @return City
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the region of the IP.
     * 
     * @return Region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns the region of the IP.
     * 
     * @return Region
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the IP address of the IP.
     * 
     * @return IP address
     */
    public String getIpAdress() {
        return ipAdress;
    }

    /**
     * Returns the ISP of the IP.
     * 
     * @return ISP
     */
    public String getIsp() {
        return isp;
    }

    /**
     * Returns the latitude and longitude of the IP.
     * 
     * @return Latitude and Longitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Returns the longitude of the IP.
     * 
     * @return Longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Returns the string representation of the IP.
     * With the IP address, city, region, country, ISP, latitude, and longitude.
     * 
     * @return String representation of the IP
     */
    @Override
    public String toString() {
        StringBuilder rs = new StringBuilder();

        rs.append("IP Adress: " + ipAdress + "\n");
        rs.append("Country: " + country + "\n");
        rs.append("Region: " + region + "\n");
        rs.append("City: " + city + "\n");
        rs.append("ISP: " + isp + "\n");
        rs.append("Latitude: " + lat + "\n");
        rs.append("Longitude: " + lon + "\n");

        return rs.toString();
    }
}
