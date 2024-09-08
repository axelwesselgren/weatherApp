package weatherapp.ui.app;

import static weatherapp.tools.JTools.FONT_SMALL;
import static weatherapp.tools.JTools.NONE;
import static weatherapp.tools.JTools.createJLabel;
import static weatherapp.tools.JTools.getDefaultCon;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import weatherapp.enums.Changes;
import weatherapp.model.Model;
import weatherapp.model.ip.IP;
import weatherapp.tools.FileTools;
import weatherapp.ui.custom.Clock;

import javax.swing.ImageIcon;

public class TopBarPanel extends JPanel implements PropertyChangeListener {
    private final Model model;
    private final JLabel lblLocation;
    private final JPanel pnlBtnLocation;
    private final Clock clock;

    public TopBarPanel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);

        lblLocation = createJLabel("/LoadingWheelSmall.gif", NONE);
        lblLocation.setFont(FONT_SMALL);

        pnlBtnLocation = new JPanel();
        pnlBtnLocation.setLayout(new GridBagLayout());
        GridBagConstraints pnlCon = getDefaultCon();

        pnlCon.insets.right = 0;
        pnlCon.insets.bottom = 0;
        pnlCon.anchor = GridBagConstraints.LINE_START;
        pnlBtnLocation.add(lblLocation, pnlCon);

        clock = new Clock();
        clock.setFont(FONT_SMALL);
        clock.getThread().start();

        GridBagConstraints con = getDefaultCon();
        setLayout(new GridBagLayout());

        con.anchor = GridBagConstraints.LINE_START;
        con.insets = new Insets(0, 15, 0, 0);
        add(pnlBtnLocation, con);

        con.gridx = 1;
        con.anchor = GridBagConstraints.LINE_END;
        con.insets = new Insets(0, 0, 0, 15);
        add(clock, con);

        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (Changes.valueOf(evt.getPropertyName())) {
            case REFRESHING -> {
                lblLocation.setIcon(new ImageIcon(FileTools.getURL("/LoadingWheelSmall.gif")));
                lblLocation.setText("");
            }
            case REFRESHED -> {
                IP ip = model.getIp();
                lblLocation.setIcon(null);
                lblLocation.setText(ip.getCity() + ", " + ip.getCountry());
            } 
            default -> {}
        }
    }
}
