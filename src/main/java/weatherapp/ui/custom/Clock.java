package weatherapp.ui.custom;

import java.time.LocalTime;
import javax.swing.JLabel;

/**
 * {@code Clock} is a custom JLabel that displays the current time in HH:MM:SS format.
 * The time is updated every second using a thread.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class Clock extends JLabel implements Runnable {
    private final Thread thread;
    private int hours, minutes, seconds;

    /**
     * Constructs a {@code Clock} instance with the {@code LocalTime.now()} as the initial time.
     * The time is updated every second using a thread.
     * Variables are updated according to the LocalTime.
     */
    public Clock() {
        thread = new Thread(this);

        LocalTime time = LocalTime.now();
        hours = time.getHour();
        minutes = time.getMinute();
        seconds = time.getSecond();

        updateText();
    }

    /**
     * Updates the text of the {@code JLabel} to the current time in HH:MM:SS format.
     * If the hours, minutes, or seconds are less than 10, a leading zero is added.
     */
    private void updateText() {
        String text = "";

        if (hours < 10) text += "0";
        text += hours + ":";
        if (minutes < 10) text += "0";
        text += minutes + ":";
        if (seconds < 10) text += "0";
        text += seconds;
        
        setText(text);
    }

    /**
     * Returns the thread that updates the time.
     * 
     * @return The thread that updates the time
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Starts the thread that updates the time.
     * Every second the seconds are incremented and variables are updated accordingly.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
                seconds++;

                if (seconds >= 60) {
                    seconds = 0;
                    minutes++;
                }

                if (minutes >= 60) {
                    minutes = 0;
                    hours++;
                }

                if (hours >= 24) {
                    hours = 0;
                }

                updateText();
            } catch (InterruptedException e) {}
        }
    }
}
