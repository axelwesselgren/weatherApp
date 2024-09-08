package weatherapp.ui.logic;

import javax.swing.SwingWorker;

import weatherapp.interfaces.Action;

/**
 * {@code BackgroundWorker} simplifies {@code SwingWorker} usage by providing a constructor that takes two {@code Action} instances.
 * 
 * @author Axel Lönnby Wesselgren
 */
public class BackgroundWorker {
    /**
     * The {@code SwingWorker} instance
     */
    private final SwingWorker<Boolean, Void> worker;
    
    /**
     * Constructs a {@code BackgroundWorker} instance with the specified {@code Action} instances.
     * 
     * @param first The action to perfrom in the background
     * @param last The action to perform when the background action is done
     */
    public BackgroundWorker(Action first, Action last) {
        worker = new SwingWorker<Boolean,Void>() {
            @Override
            public Boolean doInBackground() throws Exception {
                first.action();
                return true;
            }
            @Override
            public void done() {
                last.action();
            }  
        };
        worker.execute();
    }
}
