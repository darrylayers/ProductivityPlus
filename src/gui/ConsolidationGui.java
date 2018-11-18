package gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.RowSorter;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import core.DataHandling;
import net.miginfocom.swing.MigLayout;

/**
 * Gui class for Program Consolidation.
 * 
 * @author Austin Ayers
 * 
 */
public class ConsolidationGui extends JDialog {

    private static final long serialVersionUID = -5312647494167181674L;

    private static JTable table;
    private static DefaultTableModel model;
    private static JScrollPane sc;
    private static JPanel mainPanel;
    private static JPanel tablePanel;
    private static byte[] bytes;
    private static Preferences prefs =
        Preferences.userRoot().node("ConsolidateGui");

    private final static String PREF_LIST = "pref";

    private JTextField txtInput;
    private JLabel lblProgsToCombine;

    private JButton btnClearList;
    private JTextPane txtpnWhatDoI;

    public static List<String> list = new ArrayList<String>();

    /**
     * Launch the About pop up window.
     */
    static void newWindow() {
        try {
            ConsolidationGui dialog = new ConsolidationGui();
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Main.setWindowLoc();
            dialog.setLocation(Main.getWindowLoc().x, Main.getWindowLoc().y);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    private ConsolidationGui() {
        setTitle("What to Track");
        setBounds(100, 100, 592, 419);

        // Frame panels and panes

        getContentPane()
            .setLayout(new MigLayout("", "[grow]", "[305.00,grow]"));

        mainPanel = new JPanel();
        mainPanel.setBorder(
            new EtchedBorder(EtchedBorder.LOWERED, null, null));
        tablePanel = new JPanel();
        getContentPane().add(mainPanel, "cell 0 0,grow");
        mainPanel
            .setLayout(
                new MigLayout("", "[][246.00][40.00]", "[][][][][][grow]"));

        getContentPane().add(tablePanel, "cell 0 0,grow");
        tablePanel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        // Programs to Combine label

        lblProgsToCombine = new JLabel("Programs to Combine");
        mainPanel.add(lblProgsToCombine, "cell 1 0,grow");

        // Remove program button

        JButton btnRemove = new JButton("Remove Program");
        btnRemove.setToolTipText("Remove a program to consolidate.");
        btnRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                // If the input is empty, give a warning, else remove the item
                // to
                // the list.
                if (DataHandling.checkEmpty(txtInput.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                } else {
                    removeRow("- " + txtInput.getText());
                }
            }
        });
        mainPanel.add(btnRemove, "cell 1 3,growx");

        // Enter program button

        JButton btnEnterProgram = new JButton("Enter Program");
        btnEnterProgram.setToolTipText("Add a program to consolidate.");
        btnEnterProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                // If the input is empty, give a warning, else add the item to
                // the list.
                if (DataHandling.checkEmpty(txtInput.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                } else {
                    addRow("- " + txtInput.getText());
                }
            }
        });
        mainPanel.add(btnEnterProgram, "cell 1 2,growx");

        // Text input box

        txtInput = new JTextField();
        txtInput.setToolTipText("Enter program name here.");
        mainPanel.add(txtInput, "cell 1 1,growx");
        txtInput.setColumns(10);

        // Clear list button

        btnClearList = new JButton("Clear List");
        btnClearList.setToolTipText("Remove all programs from the list.");
        btnClearList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                list = new ArrayList<String>();
                saveList(); // Save the list
                loadTable(); // Load the table with the saved list
            }
        });
        mainPanel.add(btnClearList, "cell 1 4,grow");

        // Instructional text pane

        txtpnWhatDoI = new JTextPane();
        txtpnWhatDoI.setEditable(false);
        txtpnWhatDoI.setBackground(new Color(240, 240, 240));
        txtpnWhatDoI.setText("Q: What do I enter? \n"
            + "A: At the end of each program entry, there is a separator, '|', followed by the program's name. \n"
            + "You must enter the program's name that follows the separator into the text box above to "
            + "be able to combine all of the separate entries of that program into a single item with a combined time. \n"
            + "Ex. input: 'Google Chrome'");
        mainPanel.add(txtpnWhatDoI, "cell 1 5,grow");

        loadTable(); // Refresh the table
    }

    /**
     * Load the table in the consolidation GUI.
     */
    private void loadTable() {

        // This if-else prevents the program
        // from removing the table when it
        // doesn't exist. One the first load,
        // the method does not remove the existing
        // table, but from then on it is always true
        // and does remove the existing table to update.
        if (Main.toTrack) {
            tablePanel.remove(sc);
        } else {
            loadList();
            Main.toTrack = true;
        }

        // Create the table
        model = new DefaultTableModel(0, 1);
        String[] colHeadings = {"Program in list"};
        model.setColumnIdentifiers(colHeadings);

        table = new JTable(model);
        // Remove empty string if present in list
        if (list.contains("")) {
            list.remove("");
        }
        // Load the able
        for (String entry : list) {
            model.addRow(new Object[] {entry.substring(2, entry.length())});
        }

        // Allow for sorting of rows
        RowSorter<TableModel> sorter =
            new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);

        // Create new scroll pane with the table inside
        sc = new JScrollPane(table);
        sc.setToolTipText(
            "These are all the programs that are being combined into the parent program.");
        tablePanel.add(sc, "cell 2 3");
    }

    /**
     * Add a row to the table.
     * 
     * @param item,
     *            the item to add.
     */
    private void addRow(String item) {

        // If the item is not in the list, show alert, else add the item and
        // refresh.
        if (inList(item)) {
            JOptionPane.showMessageDialog(null,
                "Item already in list, not added.");
        } else {
            list.add(item);
            txtInput.setText("");
            loadTable();
            saveList();
        }
    }

    /**
     * Remove a row to the table.
     * 
     * @param item,
     *            the item to remove.
     */
    private void removeRow(String item) {

        // If the item is not in the list, show alert, else remove the item and
        // refresh.
        if (!inList(item)) {
            JOptionPane.showMessageDialog(null,
                "Item not in list to remove.");
        } else {
            list.remove(item);
            txtInput.setText("");
            loadTable();
            saveList();
        }
    }

    /**
     * Check to see if the local list contains the item.
     * 
     * @param item,
     *            the item to check the list for.
     * @return returns true if the list contains the item.
     */
    private static boolean inList(String item) {
        return (list.contains("- " + item) || list.contains(item));
    }

    /**
     * Load the local list with the preferences.
     */
    public static void loadList() {
        // Create new temp byte array allow for empty data to be saved in Prefs
        byte[] temp = new byte[1014];
        // Saved existing Pref's value for the list, else load an empty list
        // into bytes.
        bytes = prefs.getByteArray(PREF_LIST, temp);
        // Load the byte array
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // Convert the byte array
        DataInputStream in = new DataInputStream(bais);
        // Store all the data from the byte array to the list
        try {
            while (in.available() > 0) {
                String element = in.readUTF();
                if (!inList(element)) {
                    list.add(element);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the local list to the preferences.
     */
    private void saveList() {
        // Prep for saving to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        // Load all the data to the DOS
        for (String element : list) {
            try {
                out.writeUTF(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Load byte array
        bytes = baos.toByteArray();
        // Save byte array
        prefs.putByteArray(PREF_LIST, bytes);
    }

    /**
     * Getter for list.
     * 
     * @return the local list.
     */
    public static List<String> getSavedList() {
        return list;
    }

}
