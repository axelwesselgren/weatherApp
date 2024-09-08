package weatherapp.interfaces;

/**
 * {@code WeatherType} is an interface contains
 * the extension and the amount of weather type images.
 * 
 * <p>
 * It also contains method needed to get the id and the file name
 * of the weather type.
 * </p>
 * 
 * @author Axel Lönnby Wesselgren
 */
public interface WeatherType {
    public static final String EXTENSION = ".png";
    public static final int TYPES = 27;

    int getId();
    String getFileName();
}
