package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import gui.Main;

/**
 * TableHelper.java is used to consolidate and D.R.Y. up code used to make
 * tables.
 * 
 * @author Austin Ayers
 *
 */
public class TableHelper {

    private static JTable table;
    private static DefaultTableModel model;
    private static JScrollPane sc;
    private static Preferences prefs =
        Preferences.userRoot().node("TableHelper");
    private final static String INCLUSION_LIST = "inclusion";
    private final static String EXCLUSION_LIST = "exclusion";

    /**
     * Load the table with a list of strings.
     * 
     * @return return a table of string values of one column.
     * @param input,
     *            List of strings.
     */
    public static JScrollPane loadTable(List<String> input) {
        // Create empty table model
        model = new DefaultTableModel(0, 1);
        String[] colHeadings = {"Programs in list"};
        model.setColumnIdentifiers(colHeadings);
        // Create the table from the model
        table = new JTable(model);
        // Remove empty string if present
        input.remove("");
        // Load the table
        for (String entry : input) {
            model.addRow(new Object[] {entry.substring(2, entry.length())});
        }
        // Allow for sorting
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        // Create and return the scroll pane
        sc = new JScrollPane(table);
        return sc;
    }

    /**
     * Retrieve ArrayList from prefs and convert from byte[]
     * 
     * @return Either the list of inclusions or list of exclusions, whichever
     *         the user has selected.
     * @param the
     *            string attached to the preference we are saving.
     * 
     */
    public static List<String> loadList(String prefString) {
        // Create empty array to display if no pref is saved
        byte[] temp = new byte[1024];
        byte[] bytes;

        // Get the byte array we need
        if (prefString.equals("inclusions")) {
            bytes = prefs.getByteArray(INCLUSION_LIST, temp);
        } else if (prefString.equals("exclusions")) {
            bytes = prefs.getByteArray(EXCLUSION_LIST, temp);
        }

        // Create a list to store the byte arrya into
        List<String> retList = new ArrayList<String>();
        bytes = prefs.getByteArray(prefString, temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream in = new DataInputStream(bais);
        try {
            while (in.available() > 0) {
                String element = in.readUTF();
                if (!inList(element, retList)) {
                    retList.add(element);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retList;
    }

    /**
     * Save ArrayList in prefs and convert to byte[]
     * 
     * @param input,
     *            list of strings
     * 
     * @param the
     *            string attached to the preference we are saving.
     */
    public static void saveList(List<String> input, String prefsString) {
        // Save the input to prefs with the pref name as the prefsString param
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : input) {
            try {
                out.writeUTF(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = baos.toByteArray();
        prefs.putByteArray(prefsString, bytes);
    }

    /**
     * Check to see if the item is in the list.
     * 
     * @param item
     *            to check
     * @param retList
     *            to check
     * @return true if the item is in the list.
     */
    private static boolean inList(String item, List<String> retList) {
        return (retList.contains("- " + item) || retList.contains(item));
    }

    /**
     * Clear the list, depending on what mode we are on.
     */
    public static void clearList() {
        if (Main.getMode() == 2) {
            prefs.remove(INCLUSION_LIST);
        } else if (Main.getMode() == 3) {
            prefs.remove(EXCLUSION_LIST);
        }
    }
}
