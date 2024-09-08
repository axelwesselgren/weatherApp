package weatherapp.ui.panel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import weatherapp.model.Settings;
import weatherapp.model.weather.Timestamp;
import weatherapp.model.weather.Weather;
import weatherapp.tools.JTools;

import static weatherapp.tools.JTools.FONT_BIG;
import static weatherapp.tools.JTools.FONT_SMALL;
import static weatherapp.tools.JTools.LBL_DIMENSION;
import static weatherapp.tools.JTools.addRowComps;
import static weatherapp.tools.JTools.createJLabel;
import static weatherapp.tools.JTools.getDefaultCon;
import static weatherapp.tools.JTools.toPascal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * {@code WeatherPanel} is a custom {@code JPanel} that displays weather information.
 * The panel contains a main panel and a dropdown panel.
 * The main panel displays the current weather information.
 * The dropdown panel displays the weather information for each hour of the day.
 * The panel is collapsible and expands when clicked.
 * 
 * <p>The main panel displays the following information:
 * <ul>
 * <li>Day of the week</li>
 * <li>Date</li>
 * <li>Weather type</li>
 * <li>Minimum temperature</li>
 * <li>Maximum temperature</li>
 * <li>Average wind speed</li>
 * <li>Maximum gust speed</li>
 * </ul>
 * 
 * @author Axel Lönnby Wesselgren
 */
public class WeatherPanel extends JPanel {
    /**
     * The original color of the panel before hovering
     */
    private Color ogColor;
    /**
     * The weather object that contains the weather information
     */
    private Weather weather;
    /**
     * A boolean that indicates if the mouse is hovering over the panel
     */
    private boolean hovering;

    private final JPanel pnlMain, pnlDropdown;
    private final JScrollPane slpDropdown;
    private final JLabel 
    lblHour, lblTemp, lblType, 
    lblTypeImg, lblGustI, lblWindI, 
    lblWind, lblGust, lblDay,
    lblDate, lblMinTemp, lblMaxTemp;

    /**
     * Constructs a {@code WeatherPanel} instance with the specified {@code Weather} instance.
     * 
     * @param weather the weather object that contains the weather information
     */
    public WeatherPanel(Weather weather) {
        this.weather = weather;

        pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());
        pnlMain.setPreferredSize(new Dimension(pnlMain.getWidth(), 50));
        pnlMain.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));

        pnlDropdown = new JPanel();
        pnlDropdown.setLayout(new GridBagLayout());
        
        slpDropdown = new JScrollPane(pnlDropdown);
        slpDropdown.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        slpDropdown.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        slpDropdown.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, Color.BLACK));
        slpDropdown.setVisible(false);

        lblHour = createJLabel(FONT_BIG, LBL_DIMENSION, "Hour");
        lblTemp = createJLabel(FONT_BIG, LBL_DIMENSION, "Temperature");
        lblType = createJLabel(FONT_BIG, LBL_DIMENSION, "Type");
        lblGustI = createJLabel(FONT_BIG, LBL_DIMENSION, "Gust");
        lblWindI = createJLabel(FONT_SMALL, LBL_DIMENSION, "Wind Speed");
        
        lblDay = createJLabel(FONT_SMALL, LBL_DIMENSION, toPascal(weather.getDate().getDayOfWeek().toString()));
        lblDate = createJLabel(FONT_SMALL, LBL_DIMENSION, weather.getDate().toString());
        lblMinTemp = createJLabel(FONT_SMALL, LBL_DIMENSION, weather.getMinT());
        lblMaxTemp = createJLabel(FONT_SMALL, LBL_DIMENSION, weather.getMaxT());
        lblWind = createJLabel(FONT_SMALL, LBL_DIMENSION, weather.getWindSpeedAvg());
        lblGust = createJLabel(FONT_SMALL, LBL_DIMENSION, weather.getGustMax());

        lblTypeImg = new JLabel();
        JTools.setIconImage(lblTypeImg, "/weather/" + weather.getWeatherType().getFileName());

        pnlMain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                slpDropdown.setVisible(!slpDropdown.isVisible());
                SwingUtilities.updateComponentTreeUI(WeatherPanel.this);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                pnlMain.setBackground(ogColor);
            }
        });

        pnlMain.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (hovering) return;
                hovering = true;
                ogColor = pnlMain.getBackground();
                pnlMain.setBackground(Color.GRAY);
            }
        });
        
        addTimestamps();
        
        setLayout(new GridBagLayout());
        GridBagConstraints con = getDefaultCon();

        con.fill = GridBagConstraints.BOTH;
        add(pnlMain, con);
        
        con.gridy = 1;
        add(slpDropdown, con);

        con = getDefaultCon();
        con.insets = new Insets(10, 15, 0,0);
        
        addRowComps(con, 0, pnlMain,
            lblDay,
            lblDate,
            lblTypeImg,
            lblMinTemp,
            lblMaxTemp,
            lblWind,
            lblGust
        );
    }

    /**
     * Adds the timestamps to the dropdown panel.
     * The timestamps are displayed in a grid layout.
     * The timestamps are displayed in the following order:
     * <ul>
     * <li>Time</li>
     * <li>Weather type</li>
     * <li>Temperature</li>
     * <li>Wind speed</li>
     * <li>Gust speed</li>
     * </ul>
     */
    private void addTimestamps() {
        int y = 1;
        GridBagConstraints outerCon = getDefaultCon();
        outerCon.insets = new Insets(5, 15, 10, 0);

        addRowComps(outerCon, 0, pnlDropdown, 
            lblHour,
            lblType,
            lblTemp,
            lblWindI,
            lblGustI
        );

        for (Timestamp t : weather.getTimeStamps()) {
            JLabel lblTime = new JLabel(t.getTime().toString().substring(0, 2));
            JLabel lblTemp = new JLabel(t.getTemp());
            JLabel lblWind = new JLabel(t.getWindSpeed());
            JLabel lblGust = new JLabel(t.getGust());
            JLabel lblWeather = new JLabel();

            JTools.setIconImage(lblWeather, "/weather/" + t.getWeatherFileName());

            GridBagConstraints con = getDefaultCon();
            con.gridy = y++;
            con.insets = new Insets(0, 15, 5, 0);

            addRowComps(con, 0, pnlDropdown,
                lblTime,
                lblWeather,
                lblTemp,
                lblWind,
                lblGust
            );
        }
    }

    /**
     * Updates the {@code WeatherPanel} with the specified {@code Settings} instance.
     * The weather information is updated with the new settings.
     * The labels are updated with the new weather information.
     * The dropdown panel is removed and the timestamps are added again in order to update the timestamps.
     * 
     * @param settings
     */
    public void update(Settings settings) {
        weather.setSettings(settings);

        lblMaxTemp.setText(weather.getMaxT());
        lblMinTemp.setText(weather.getMinT());
        lblWind.setText(weather.getWindSpeedAvg());
        lblGust.setText(weather.getGustMax());

        pnlDropdown.removeAll();
        addTimestamps();
        pnlDropdown.repaint();
    }
}
