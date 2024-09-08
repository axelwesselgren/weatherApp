package weatherapp.ui.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * {@code AppleSwitch} is a custom JToggleButton that mimics the Apple switch found in iOS.
 * The switch animates when toggled on or off.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class AppleSwitch extends JToggleButton {
    /**
     * Default size
     */
    private static final int WIDTH = 60, HEIGHT = 25;
    /**
     * The x-coordinate of the circle
     */
    private int circleX;
    /**
     * The diameter of the circle
     */
    private int circleDiameter;
    /**
     * The timer that animates the switch
     */
    private final Timer timer;
    /**
     * A boolean that indicates if the switch is animating
     */
    private boolean animating = false;

    /**
     * Constructs an {@code AppleSwitch} instance with the specified width and height.
     * 
     * @param width the width of the switch
     * @param height the height of the switch
     */
    public AppleSwitch(int width, int height) {
        setSettings(width, height);
        timer = createTimer(width);
        addListener();
    }
    /**
     * Constructs an {@code AppleSwitch} instance with the default width {@value #WIDTH} and height {@value #HEIGHT}.
     */
    public AppleSwitch() {
        this(WIDTH, HEIGHT);
    }

    /**
     * Sets the preferred size, and the focus, border, and content area painted properties.
     * Also sets the circle diameter and x-coordinate.
     * 
     * <ul>
     *   <li>FocusPainted: {@code false}</li>
     *   <li>BorderPainted: {@code false}</li>
     *   <li>ContentAreaFilled: {@code false}</li>
     *   <li>CircleDiameter: {@code height - 5}</li>
     *   <li>CircleX: {@code 2}</li>
     * </ul>
     * 
     * @param width The preferred width
     * @param height The preferred height
     */
    private void setSettings(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);

        circleDiameter = height - 5;
        this.circleX = 2;
    }
    /**
     * Creates a timer that animates the switch.
     * Every 10 milliseconds the circleX is incremented or decremented by 2.
     * Depending on if the switch is selected or not.
     * When the circleX reaches the end of the switch, the timer stops.
     * 
     * @return
     */
    private Timer createTimer(int width) {
        return new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSelected() && circleX < width - circleDiameter - 2) {
                    circleX += 2;
                } else if (!isSelected() && circleX > 2) {
                    circleX -= 2;
                } else {
                    animating = false;
                    timer.stop();
                }
                repaint();
            }
        });
    }
    /**
     * Adds an action listener to the switch.
     * When the switch is clicked and not animating, the timer starts.
     */
    private void addListener() {
        addActionListener(e -> {
            if (!animating) {
                animating = true;
                timer.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = isSelected() ? new Color(50, 50, 50) : Color.LIGHT_GRAY;
        g2d.setColor(color);
        g2d.fillRoundRect(0, 0, WIDTH, HEIGHT, HEIGHT, HEIGHT);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(circleX, 2, circleDiameter, circleDiameter);
    }
}
