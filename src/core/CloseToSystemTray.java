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
        if (!SystemTray.isSupported()) {
            return;
        }

        SystemTray systemTray = SystemTray.getSystemTray();

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem maximize = new MenuItem("Maximize");
        maximize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.setFrameVisible();
            }
        });
        trayPopupMenu.add(maximize);

        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DataHandling.saveMap();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        trayPopupMenu.add(close);

        Image image = Toolkit.getDefaultToolkit()
            .getImage(
                this.getClass().getResource("/favicon.png"));

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
