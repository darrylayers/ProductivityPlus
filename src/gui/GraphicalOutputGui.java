package gui;

import javax.swing.JDialog;

/**
 * Gui class for Graphical Output window.
 * 
 * @author Austin Ayers
 * @version 9/28/18
 * 
 */
public class GraphicalOutputGui extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Launch the graphical output pop up window.
     */
    public static void newWindow() {
        try {
            GraphicalOutputGui dialog = new GraphicalOutputGui();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            Main.setWindowLoc();
            dialog.setLocation(Main.getWindowLoc().x, Main.getWindowLoc().y);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public GraphicalOutputGui() {
        setAlwaysOnTop(true);
        setTitle("Graphs");
        setBounds(100, 100, 450, 300);

    }
}
