package weatherapp.tools;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import weatherapp.tools.JTools;

import java.io.IOException;

/**
 * {@code JTools} is a class that holds static methods for creating JComponents.
 * It also holds static variables for fonts and dimensions.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class JTools {
    /**
     * The default font family for the application.
     * It is set to Arial.
     */
    public static final String FONT_FAMILY;
    /**
     * The default small font for the application.
     * It is set to {@code Arial}, {@code bold}, {@code 14}.
     */
    public static final Font FONT_SMALL;
    /**
     * The default big font for the application.
     * It is set to {@code Arial}, {@code bold}, {@code 16}.
     */
    public static final Font FONT_BIG;
    /**
     * The default dimension for a JLabel.
     * It is set to {@code 100, 20}.
     */
    public static final Dimension LBL_DIMENSION;
    /**
     * The default dimension for a JButton.
     * It is set to {@code 100, 25}.
     */
    public static final Dimension BTN_DIMENSION;
    /**
     * The default dimension for no dimension.
     */
    public static final Dimension NONE;

    /**
     * The default {@code GridBagConstraints} for the application.
     * <ul>
     * <li>{@code weightx} is set to {@code 1.0}.
     * <li>{@code weighty} is set to {@code 1.0}.
     * <li>{@code gridx} is set to {@code 0}.
     * <li>{@code gridy} is set to {@code 0}.
     * <li>{@code anchor} is set to {@code FIRST_LINE_START}.
     * </ul>
     */
    private static final GridBagConstraints DEFAULT_CON;

    static {
        FONT_FAMILY =  "Arial";
        FONT_SMALL = new Font(FONT_FAMILY, Font.BOLD, 14);
        FONT_BIG = new Font(FONT_FAMILY, Font.BOLD, 16);

        BTN_DIMENSION = new Dimension(100, 25);
        LBL_DIMENSION = new Dimension(100, 20);
        NONE = new Dimension();

        DEFAULT_CON = new GridBagConstraints();
        DEFAULT_CON.weightx = 1.0;
        DEFAULT_CON.weighty = 1.0;
        DEFAULT_CON.gridx = 0;
        DEFAULT_CON.gridy = 0;
        DEFAULT_CON.anchor = GridBagConstraints.FIRST_LINE_START;
    }

    /**
     * Creates a {@code JLabel} with an image and a dimension.
     * 
     * @param path The path to the image in the resources folder.
     * @param dimension The preferred dimension of the JLabel.
     * @return The created JLabel.
     */
    public static JLabel createJLabel(String path, Dimension dimension) {
        JLabel lbl = new JLabel();
        lbl.setIcon(new ImageIcon(FileTools.getURL(path)));
        if (!(dimension == NONE)) lbl.setPreferredSize(dimension);
        return lbl;
    }
    /**
     * Creates a {@code JLabel} with a font, preferred dimension and text.
     * 
     * @param font The font of the JLabel.
     * @param dimension The preferred dimension of the JLabel.
     * @param text The text of the JLabel.
     * @return The created JLabel.
     */
    public static JLabel createJLabel(Font font, Dimension dimension, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        if (!(dimension == NONE)) lbl.setPreferredSize(dimension);
        return lbl;
    }
    /**
     * Creates a {@code JLabel} with a font, text and preferred dimension.
     * 
     * @param dimension The preferred dimension of the JLabel.
     * @param text The text of the JLabel.
     * @param listener The ActionListener for the JButton.
     * @return The created JButton.
     */
    public static JButton createJButton(Dimension dimension, String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(dimension);
        btn.addActionListener(listener);
        return btn;
    }

    /**
     * Converts a string to Pascal case.
     * 
     * @param string The string to convert to Pascal case.
     * @return The string in Pascal case.
     */
    public static String toPascal(String string) {
        return string.substring(0, 1).toUpperCase() 
        + string.substring(1, string.length()).toLowerCase();
    }

    /**
     * Returns a clone of the default {@code GridBagConstraints}.
     * 
     * @return A clone of the default {@code GridBagConstraints}.
     */
    public static GridBagConstraints getDefaultCon() {
        return (GridBagConstraints) DEFAULT_CON.clone();
    }

    /**
     * Adds components to a JPanel in a row.
     * It's recursive and reruns until all components are added.
     * 
     * @param con The {@code GridBagConstraints} to use.
     * @param i The start gridx value.
     * @param pnl The JPanel to add the components to.
     * @param comps The components to add.
     */
    public static void addRowComps(GridBagConstraints con, int i, JPanel pnl, JComponent... comps) {
        if (i == comps.length) return;
        con.gridx = i;
        pnl.add(comps[i], con);
        addRowComps(con, ++i, pnl, comps);
    }

    /**
     * Simplifies setting an image to a JLabel.
     * 
     * @param lbl The JLabel to set the image to.
     * @param path The path to the image in the resources folder.
     */
    public static void setIconImage(JLabel lbl, String path) {
        try {
            BufferedImage img = FileTools.getBufferedImage(path);
            lbl.setIcon(new ImageIcon(FileTools.getResizedImage(img, 3)));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Missing weather images", "Missing image", JOptionPane.ERROR_MESSAGE);
        }
    }
} 
