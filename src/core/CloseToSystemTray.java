package core;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import gui.Main;

/**
 * CloseToSystemTray.java creates the system tray icon and functionality used to
 * maximize the program inside the tray.
 * 
 * @author Austin Ayers
 */
public class CloseToSystemTray {

    /**
     * Creates the tray and all the functionality.
     * 
     * @throws IOException
     */
    public void startTray() throws IOException {
        // Make sure tray is supported
        if (!SystemTray.isSupported()) {
            return;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        PopupMenu trayPopupMenu = new PopupMenu();

        // Add tray option to maximize program
        MenuItem maximize = new MenuItem("Maximize");
        maximize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.setFrameVisible();
            }
        });
        trayPopupMenu.add(maximize);

        // Add tray option to close program
        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Save the map before closing
                    DataHandling.saveMap();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    // Allow time to save before closing.
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        // Change the app icon
        Image image = Toolkit.getDefaultToolkit()
            .getImage(
                this.getClass().getResource("/icon.png"));

        TrayIcon trayIcon =
            new TrayIcon(image, "Productivity Plus", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }
}
