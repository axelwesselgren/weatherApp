package weatherapp;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import weatherapp.enums.Changes;
import weatherapp.model.Model;
import weatherapp.tools.FileTools;
import weatherapp.ui.app.App;
import weatherapp.ui.app.ControlPanel;
import weatherapp.ui.app.MainPanel;
import weatherapp.ui.app.TopBarPanel;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONParserConfiguration;

import static weatherapp.tools.FileTools.getImage;
import javax.swing.JOptionPane;

/**
 * {@code Main} is the entry point of the application.
 * It is used to run the application, set settings and create instances.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class Main {
    /**
     * The path to the icon image resource
     */
    private final static String ICON_PATH = "/cloud.png";
    /**
     * The title of the application
     */
    private final static String TITLE = "Weather App";
    /**
     * The dimension of the application window
     */
    private final static Dimension DIMENSION = new Dimension(960, 540);
    /**
     * A boolean that indicates if the application window is resizable
     */
    private final static boolean RESIZABLE = false;
    /**
     * The location of the application window
     */
    private final static JComponent LOCATION = null;
    /**
     * The look and feel to use for dark mode
     */
    private final static LookAndFeel DARK_MODE = new FlatMacDarkLaf();
    /**
     * The look and feel to use for light mode
     */
    private final static LookAndFeel LIGHT_MODE = new FlatMacLightLaf();

    /**
     * Runs the application with the default settings.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(DARK_MODE);
        } catch (UnsupportedLookAndFeelException e) {}

        Model model = new Model();

        ControlPanel controlPanel = new ControlPanel(model);
        TopBarPanel topBarPanel = new TopBarPanel(model);
        MainPanel mainPanel = new MainPanel(model);

        App app = new App(controlPanel, topBarPanel, mainPanel) {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (Changes.valueOf(evt.getPropertyName())) {
                    case SETTINGS_UPDATED -> setTheme(model.getSettings().isDarkMode());
                    default -> {}
                }
            }
        };
        app.setTitle(TITLE);
        try {
            app.setIconImage(getImage(ICON_PATH));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Icon image not found", "Missing image", JOptionPane.WARNING_MESSAGE);
        }
        app.setPreferredSize(DIMENSION);
        app.setResizable(RESIZABLE);
        app.setLocationRelativeTo(LOCATION);
        app.init();

        model.addPropertyChangeListener(app);
        model.requestRefresh();
    }
}
