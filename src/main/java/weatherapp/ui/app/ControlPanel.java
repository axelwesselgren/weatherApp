package weatherapp.ui.app;

import static weatherapp.tools.JTools.BTN_DIMENSION;
import static weatherapp.tools.JTools.FONT_SMALL;
import static weatherapp.tools.JTools.NONE;
import static weatherapp.tools.JTools.createJButton;
import static weatherapp.tools.JTools.createJLabel;
import static weatherapp.tools.JTools.getDefaultCon;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weatherapp.enums.Changes;
import weatherapp.model.Model;
import weatherapp.model.Settings;
import weatherapp.ui.custom.AppleSwitch;


public class ControlPanel extends JPanel implements PropertyChangeListener {
    private final JPanel pnlMain;
    private final JLabel lblPrecision, lblMetricImperial;
    private final AppleSwitch switchPrecision, switchCF;
    private final JButton btnRefresh, btnChangeLight;
    private final Model model;

    private static final Logger logger = LogManager.getLogger(ControlPanel.class);

    public ControlPanel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
        setLayout(new GridBagLayout());

        pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());

        switchPrecision = new AppleSwitch();
        switchCF = new AppleSwitch();

        lblPrecision = createJLabel(FONT_SMALL, NONE, "Precision");
        lblMetricImperial = createJLabel(FONT_SMALL, NONE, "Metric/Imperial");

        btnChangeLight = createJButton(BTN_DIMENSION, "Light Mode", e -> model.changeTheme());
        btnRefresh = createJButton(BTN_DIMENSION, "Refresh", e -> model.requestRefresh());

        switchPrecision.addActionListener(e -> {
            logger.info("Precision toggled");
            model.togglePrecision();
            repaint();
        });
        switchCF.addActionListener(e -> {
            logger.info("Degree toggled");
            model.toggleMetric();
            repaint();
        });

        addMainComps();
    }

    private void addMainComps() {
        GridBagConstraints con = getDefaultCon();

        con.insets.top = 10;
        con.insets.left = 5;
        pnlMain.add(lblPrecision, con);

        con.gridy = 1;
        con.insets.top = 10;
        pnlMain.add(switchPrecision, con);

        con.gridy = 2;
        con.insets.top = 20;
        pnlMain.add(lblMetricImperial, con);

        con.gridy = 3;
        con.insets.top = 10;
        pnlMain.add(switchCF, con);

        con.gridy = 4;
        con.insets.top = 20;
        pnlMain.add(btnChangeLight, con);

        con.gridy = 5;
        con.insets.top = 10;
        pnlMain.add(btnRefresh, con);

        con.gridy = 0;
        con.gridwidth = 3;
        con.fill = GridBagConstraints.HORIZONTAL;
        add(pnlMain, con);
    }

    private void disableAllButtons() {
        btnRefresh.setEnabled(false);
        btnChangeLight.setEnabled(false);

        switchCF.setEnabled(false);
        switchPrecision.setEnabled(false);
    }
    private void enableAllButtons() {
        btnRefresh.setEnabled(true);
        btnChangeLight.setEnabled(true);

        switchCF.setEnabled(true);
        switchPrecision.setEnabled(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (Changes.valueOf(evt.getPropertyName())) {
            case REFRESHING -> disableAllButtons();
            case REFRESHED -> enableAllButtons();
            case SETTINGS_UPDATED -> {
                Settings settings = model.getSettings();
                btnChangeLight.setText(settings.isDarkMode() ? "Light Mode" : "Dark Mode");
            }
            default -> {}
        }
    }
}
