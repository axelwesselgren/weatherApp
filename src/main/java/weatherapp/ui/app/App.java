package weatherapp.ui.app;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A custom JFrame class that sets up a layout with a top panel, side panel, and main panel.
 * 
 * <p>The layout uses {@link GridBagLayout} and adds components in specific grid positions:
 * <ul>
 *     <li>The side panel is added to the left side of the window.</li>
 *     <li>The top panel is added to the top of the window.</li>
 *     <li>The main panel occupies the remaining space.</li>
 * </ul>
 * </p>
 * 
 * <p>The theme can be set to either dark or light mode using FlatLaf themes.</p>
 * 
 * @author Axel Lönnby Wesselgren
 */
public class App extends JFrame implements PropertyChangeListener {
    private GridBagConstraints con;
    
    /**
     * Contructs a {@code App} instance with specified parameters.
     * 
     * @param title the title of the frame
     * @param dimension the dimension of the frame
     * @param iconPath the path to the icon image resource
     * @param sidePanel the panel to add to the side of the frame
     * @param topPanel the panel to add to the top of the frame
     * @param mainPanel the panel to add to the main area of the frame
     */
    public App(
        JPanel sidePanel,
        JPanel topPanel,
        JPanel mainPanel
    ) {
        setLayout(new GridBagLayout());

        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));

        addSidePanel(sidePanel);
        addTopPanel(topPanel);
        addMainPanel(mainPanel);
    }

    public void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    
    /**
     * Adds a panel to the side of the frame.
     * 
     * @param panel the panel to add
     */
    public void addSidePanel(JPanel panel) {
        con = new GridBagConstraints();

        con.gridy = 0;
        con.gridx = 0;
        con.weightx  = 0.125;
        con.weighty = 1;
        con.gridheight = 2;
        con.fill = GridBagConstraints.BOTH;
        add(panel, con);
    }

    /**
     * Adds a panel to the main area of the frame.
     * 
     * @param panel the panel to add
     */
    public void addMainPanel(JPanel panel) {
        con = new GridBagConstraints();

        con.gridy = 1;
        con.gridx = 1;
        con.weighty = 0.875;
        con.weightx = 0.875;
        con.gridwidth = 2;
        con.fill = GridBagConstraints.BOTH;
        add(panel, con);
    }

    /**
     * Adds a panel to the top of the frame.
     * 
     * @param panel the panel to add
     */
    public void addTopPanel(JPanel panel) {
        con = new GridBagConstraints();

        con.gridx = 1;
        con.gridy = 0;
        con.weightx = 0.875;
        con.weighty = 0.125;
        con.fill = GridBagConstraints.BOTH;
        add(panel, con); 
    }

    /**
     * Sets the look and feel theme for the frame.
     * 
     * @param dark {@code true} for dark theme, {@code false} for light theme
     */
    public void setTheme(boolean dark) {
        try {
            UIManager.setLookAndFeel(dark ? new FlatMacDarkLaf() : new FlatMacLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {}
    }
    
    /**
     * Sets the icon image for the frame.
     * 
     * @param path the path to the icon image resource
     */
    public void setIcon(String path) {
        URL urlImg = App.class.getResource(path);
        Image img = null;

        if (urlImg == null) {
            JOptionPane.showMessageDialog(this, "Icon image not found", "Missing image", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            img = ImageIO.read(urlImg);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Icon image not available", "Missing image", JOptionPane.WARNING_MESSAGE);
        }
        
        setIconImage(img);
    }

    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
