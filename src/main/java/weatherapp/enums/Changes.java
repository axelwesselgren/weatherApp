package weatherapp.enums;

/**
 * {@code Changes} is an enum that contains and represents
 *  the different changes and updates in the application.
 * 
 * @author Axel Lönnby Wesselgren
 */
public enum Changes {
    /**
     * When the user requests a refresh/start of the application.
     */
    REFRESHING("REFRESHING"),
    /**
     * When the request refresh and startup is completed.
     */
    REFRESHED("REFRESHED"),
    /**
     * When the user request a change in the settings.
     * Example imperial or metric.
     */
    SETTINGS_UPDATED("SETTINGS_UPDATED");

    /**
     * Identifier/description of the change.
     */
    private final String change;

    /**
     * Constructor for the enum.
     * 
     * @param change The identifier/description of the change.
     */
    private Changes(String change) {
        this.change = change;
    }

    /**
     * Get the identifier/description of the change.
     * 
     * @return The identifier/description of the change.
     */
    public String getChange() {
        return change;
    }
}
