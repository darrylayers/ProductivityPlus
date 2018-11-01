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

public class TableHelper {

	private static JTable table;
	private static DefaultTableModel model;
	private static JScrollPane sc;
	private static Preferences prefs = Preferences.userRoot().node("TableHelper");
	private final static String INCLUSION_LIST = "inclusion";
	private final static String EXCLUSION_LIST = "exclusion";

	public static JScrollPane loadTable(List<String> input) {

		model = new DefaultTableModel(0, 1);
		String[] colHeadings = { "Programs in list" };
		model.setColumnIdentifiers(colHeadings);
		List<String> list = new ArrayList<String>(input);
		table = new JTable(model);

		// Load the able
		for (String entry : list) {
			model.addRow(new Object[] { entry.substring(2, entry.length()) });
		}

		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);

		sc = new JScrollPane(table);

		return sc;

	}

	/*
	 * Retrieve ArrayList from prefs and convert from byte[]
	 */
	public static List<String> loadList(String prefString) {
		byte[] temp = { 0 };
		byte[] bytes;
		
		if(prefString.equals("inclusions")) {
			bytes = prefs.getByteArray(INCLUSION_LIST, temp);
		} else if (prefString.equals("exclusions")) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}

	/*
	 * Save ArrayList in prefs and convert to byte[]
	 */
	public static void saveList(List<String> input, String prefsString) {

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

	public static boolean inList(String item, ArrayList<String> list) {
		if (list.contains("- " + item) || list.contains(item)) {
			return true;
		} else {
			return false;
		}
	}

	public byte[] getInclusions() {
		byte[] temp = {0};
		return prefs.getByteArray(INCLUSION_LIST, temp);
	}

	public void setInclusions(byte[] bytes) {
		prefs.putByteArray(INCLUSION_LIST, bytes);
	}

	public byte[] getExclusions() {
		byte[] temp = {0};
		return prefs.getByteArray(EXCLUSION_LIST, temp);
	}

	public void setExclusions(byte[] bytes) {
		prefs.putByteArray(EXCLUSION_LIST, bytes);
	}
	
	
}
