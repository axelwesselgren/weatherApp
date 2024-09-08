package weatherapp.ui.app;

import static weatherapp.tools.JTools.FONT_BIG;
import static weatherapp.tools.JTools.LBL_DIMENSION;
import static weatherapp.tools.JTools.NONE;
import static weatherapp.tools.JTools.addRowComps;
import static weatherapp.tools.JTools.createJLabel;
import static weatherapp.tools.JTools.getDefaultCon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import weatherapp.enums.Changes;
import weatherapp.model.Model;
import weatherapp.ui.panel.WeatherPanel;

public class MainPanel extends JPanel implements PropertyChangeListener {
    private final JScrollPane scrollPane;
    private final JPanel pnlWeather;
    private final GridBagConstraints con;
    private final JLabel lblDate, lblDay, lblMaxTemp, lblMinTemp, lblType, lblWind, lblGust, lblLoading;
    private List<WeatherPanel> weatherPnlsAdv;
    private Model model;

    public MainPanel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

        lblDay = createJLabel(FONT_BIG, LBL_DIMENSION, "Day");
        lblDate = createJLabel(FONT_BIG, LBL_DIMENSION, "Date");
        lblMinTemp = createJLabel(FONT_BIG, LBL_DIMENSION, "Min Temp");
        lblMaxTemp = createJLabel(FONT_BIG, LBL_DIMENSION, "Max Temp");
        lblType = createJLabel(FONT_BIG, LBL_DIMENSION, "Type");
        lblGust = createJLabel(FONT_BIG, LBL_DIMENSION, "Gust");
        lblWind = createJLabel(FONT_BIG, LBL_DIMENSION, "Wind Speed");
        lblLoading = createJLabel("/LoadingWheel.gif", NONE);

        pnlWeather = new JPanel();
        weatherPnlsAdv = model.getWeatherPanelsAdv();

        scrollPane = new JScrollPane(pnlWeather);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        setLayout(new GridBagLayout());
        GridBagConstraints conMain = getDefaultCon();
        conMain.weighty = 0.0;
        conMain.insets.top = 10;
        conMain.insets.left = 20;

        addRowComps(conMain, 0, this,
            lblDay, lblDate, lblType, 
            lblMinTemp, lblMaxTemp, lblWind, lblGust
        );

        conMain.gridx = 0;
        conMain.gridy = 1;
        conMain.gridwidth = 7;
        conMain.insets.left = -10;
        conMain.fill = GridBagConstraints.BOTH;
        conMain.weighty = 1.0;
        add(scrollPane, conMain);

        conMain.anchor = GridBagConstraints.CENTER;
        conMain.weightx = 1.0;
        conMain.insets.left = 0;
        conMain.gridy = 0;
        conMain.gridwidth = 1;
        // Change this to the middle of a {7} grid
        conMain.gridx = 3;
        add(lblLoading, conMain);

        pnlWeather.setLayout(new GridBagLayout());
        con = getDefaultCon();
        con.fill = GridBagConstraints.BOTH;
    }

    private void updateWeathers() {
        pnlWeather.removeAll();

        for (int i = 0; i < weatherPnlsAdv.size(); i++) {
            con.gridy = i;
            pnlWeather.add(weatherPnlsAdv.get(i), con);
        }

        pnlWeather.revalidate();
        pnlWeather.repaint();
    }
    private void refreshWeathers() {
        weatherPnlsAdv = new ArrayList<>();
        weatherPnlsAdv = model.getWeatherPanelsAdv();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (Changes.valueOf(evt.getPropertyName())) {
            case REFRESHING -> {
                scrollPane.setVisible(false);
                lblLoading.setVisible(true);
            }
            case REFRESHED -> {
                scrollPane.setVisible(true);
                lblLoading.setVisible(false);
                refreshWeathers();
                updateWeathers();
            }
            case SETTINGS_UPDATED -> {
                weatherPnlsAdv.forEach(w -> w.update((model.getSettings())));
                updateWeathers();
            }
            default -> {}
        }
    }
}
