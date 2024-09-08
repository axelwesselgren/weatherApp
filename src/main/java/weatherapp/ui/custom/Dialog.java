package weatherapp.ui.custom;

import static weatherapp.tools.FileTools.getImage;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JDialog;

public class Dialog extends JDialog {
    private static JDialog currentDialog;
    
    public Dialog(String title, JComponent parentComponent) {
        setTitle(title);
        setLocationRelativeTo(parentComponent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            setIconImage(getImage("/cloud.png"));
        } catch (IOException e) {}

        if (currentDialog != null) currentDialog.dispose();
        currentDialog = this;

        parentComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                dispose();
            }
        });
    }
}