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

        model = new DefaultTableModel(0, 1);
        String[] colHeadings = {"Programs in list"};
        model.setColumnIdentifiers(colHeadings);
        table = new JTable(model);
        input.remove("");

        // Load the able
        for (String entry : input) {
            model.addRow(new Object[] {entry.substring(2, entry.length())});
        }

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);

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
        byte[] temp = new byte[1024];
        byte[] bytes;

        if (prefString.equals("inclusions")) {
            bytes = prefs.getByteArray(INCLUSION_LIST, temp);
        }
        else if (prefString.equals("exclusions")) {
            bytes = prefs.getByteArray(EXCLUSION_LIST, temp);
        }

        ArrayList<String> retList = new ArrayList<String>();
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
        }
        catch (IOException e) {
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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : input) {
            try {
                out.writeUTF(element);
            }
            catch (IOException e) {
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
     * @param list
     *            to check
     * @return true if the item is in the list.
     */
    public static boolean inList(String item, ArrayList<String> list) {
        if (list.contains("- " + item) || list.contains(item)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Get the inclusions.
     * 
     * @return byte[] of inclusions.
     */
    public byte[] getInclusions() {
        byte[] temp = {0};
        return prefs.getByteArray(INCLUSION_LIST, temp);
    }

    /**
     * Set the inclusions preferences.
     * 
     * @param bytes[]
     *            of bytes
     */
    public void setInclusions(byte[] bytes) {
        prefs.putByteArray(INCLUSION_LIST, bytes);
    }

    /**
     * Set the exclusions preferences.
     * 
     * @return array of bytes.
     */
    public byte[] getExclusions() {
        byte[] temp = {0};
        return prefs.getByteArray(EXCLUSION_LIST, temp);
    }

    /**
     * Set the exclusions preferences.
     * 
     * @param arraylist
     *            of bytes
     */
    public void setExclusions(byte[] bytes) {
        prefs.putByteArray(EXCLUSION_LIST, bytes);
    }

    /**
     * Load and return whichever list the user has selected. Either inclusions
     * or exclusions.
     * 
     * @return return either inclusions or exclusions.
     */
    public static List<String> getSavedList() {
        List<String> list = new ArrayList<String>();
        List<String> inclusions = new ArrayList<>();
        inclusions = TableHelper.loadList("inclusion");
        List<String> exclusions = new ArrayList<>();
        exclusions = TableHelper.loadList("exclusion");
        if (Main.getMode() == 2) {
            return inclusions;
        }
        else if (Main.getMode() == 3) {
            return exclusions;
        }
        else {
            return list;
        }
    }
}
