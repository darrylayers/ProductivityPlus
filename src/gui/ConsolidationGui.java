package gui;

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
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;

/**
 * Gui class for What To Track window.
 * 
 * @author Austin Ayers
 * 
 */
@SuppressWarnings("serial")
public class ConsolidationGui extends JDialog {

    private static JTable table;
    private static DefaultTableModel model;
    private static JScrollPane sc;
    private static JPanel mainPanel;
    private static JPanel tablePanel;
    private static byte[] bytes;
    private static Preferences prefs =
        Preferences.userRoot().node("WhatToTrackGui");

    private JTextField txtInput;
    private JLabel secretLabel;
    private JLabel lblProgsToCombine;

    public static List<String> list = new ArrayList<String>();

    /**
     * Launch the About pop up window.
     */
    public static void newWindow() {
        try {
            ConsolidationGui dialog = new ConsolidationGui();
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
    public ConsolidationGui() {
        setTitle("What to Track");
        setBounds(100, 100, 528, 399);

        // ************** Frame panels and panes ************** //

        getContentPane()
            .setLayout(new MigLayout("", "[grow]", "[305.00,grow]"));

        mainPanel = new JPanel();
        tablePanel = new JPanel();
        getContentPane().add(mainPanel, "cell 0 0,grow");
        mainPanel
            .setLayout(new MigLayout("", "[][grow][]", "[][fill][][][][]"));

        getContentPane().add(tablePanel, "cell 0 0,grow");
        tablePanel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        lblProgsToCombine = new JLabel("Programs to combine");
        mainPanel.add(lblProgsToCombine, "cell 1 0,growy");

        secretLabel = new JLabel("");
        mainPanel.add(secretLabel, "cell 2 1");

        // ************** Buttons ************** //

        JButton btnRemove = new JButton("Remove Program");
        btnRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                if (checkEmpty(txtInput.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                }
                else {
                    removeRow("- " + txtInput.getText());
                }
            }
        });
        mainPanel.add(btnRemove, "cell 1 4,growx");

        JButton btnEnterProgram = new JButton("Enter Program");
        btnEnterProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {

                System.out.println("inside enter");
                System.out.println(txtInput.getText());

                if (checkEmpty(txtInput.getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Input string empty.");
                }
                else {
                    addRow("- " + txtInput.getText());
                }
            }
        });
        mainPanel.add(btnEnterProgram, "cell 1 3,growx");

        // ************** Text input box ************** //

        txtInput = new JTextField();
        mainPanel.add(txtInput, "cell 1 2,growx");
        txtInput.setColumns(10);

        loadTable();
    }

    /**
     * Load the table in the consolidation GUI.
     */
    public void loadTable() {
        if (Main.toTrack) {
            tablePanel.remove(sc);
        }
        else {
            loadList();
            Main.toTrack = true;
        }

        model = new DefaultTableModel(0, 1);
        String[] colHeadings = {"Program in list"};
        model.setColumnIdentifiers(colHeadings);

        table = new JTable(model);

        // Load the able
        for (String entry : list) {
            model.addRow(new Object[] {entry.substring(2, entry.length())});
        }

        RowSorter<TableModel> sorter =
            new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);

        sc = new JScrollPane(table);
        tablePanel.add(sc, "cell 2 3");
        secretLabel.setText("  ");
        secretLabel.setText("");
    }

    /**
     * Add a row to the table.
     * 
     * @param item,
     *            the item to add.
     */
    public void addRow(String item) {
        if (inList(item)) {
            JOptionPane.showMessageDialog(null,
                "Item already in list, not added.");
        }
        else {
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
    public void removeRow(String item) {
        if (!inList(item)) {
            JOptionPane.showMessageDialog(null,
                "Item not in list to remove.");
        }
        else {
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
    public static boolean inList(String item) {
        if (list.contains("- " + item) || list.contains(item)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Load the local list with the preferences.
     */
    public static void loadList() {
        byte[] temp = {0};
        bytes = prefs.getByteArray("PREF_LIST", temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream in = new DataInputStream(bais);
        try {
            while (in.available() > 0) {
                String element = in.readUTF();
                if (!inList(element)) {
                    list.add(element);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the local list to the preferences.
     */
    public void saveList() {
        System.out.print("Saving list: " + list + "\n");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : list) {
            try {
                out.writeUTF(element);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        bytes = baos.toByteArray();
        prefs.putByteArray("PREF_LIST", bytes);
    }

    /**
     * Getter for list.
     * 
     * @return the local list.
     */
    public static List<String> getSavedList() {
        return list;
    }

    /**
     * Check to see if input string is empty.
     * 
     * @param str
     *            input string.
     * @return true if the input string is empty.
     */
    public boolean checkEmpty(String str) {
        System.out.print(str);
        if (str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

}
