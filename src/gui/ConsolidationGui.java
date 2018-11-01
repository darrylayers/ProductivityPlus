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
 * @version 10/29/18
 * 
 */
public class ConsolidationGui extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static List<String> list = new ArrayList<String>();
    private JTextField txtInput;
    private static JTable table;
    private static DefaultTableModel model;
    private static JScrollPane sc;
    private static JPanel panel;
    private static JPanel panel2;
    private JLabel secretLabel;
    private JLabel lblNewLabel;
    private static byte[] bytes;
    private static Preferences prefs =
        Preferences.userRoot().node("WhatToTrackGui");
    private JButton btnPrintList;

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
        getContentPane()
            .setLayout(new MigLayout("", "[grow]", "[305.00,grow]"));

        panel = new JPanel();
        panel2 = new JPanel();
        getContentPane().add(panel, "cell 0 0,grow");
        panel.setLayout(new MigLayout("", "[][grow][]", "[][fill][][][][]"));

        getContentPane().add(panel2, "cell 0 0,grow");
        panel2.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        lblNewLabel = new JLabel("Programs to combine");
        panel.add(lblNewLabel, "cell 1 0,growy");

        secretLabel = new JLabel("");
        panel.add(secretLabel, "cell 2 1");

        JButton btnPrintArraylist = new JButton("Remove Program");
        btnPrintArraylist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                removeRow("- " + txtInput.getText());
            }
        });

        JButton btnEnterProgram = new JButton("Enter Program");
        btnEnterProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                addRow("- " + txtInput.getText());
            }
        });

        txtInput = new JTextField();
        panel.add(txtInput, "cell 1 2,growx");
        txtInput.setColumns(10);
        panel.add(btnEnterProgram, "cell 1 3,growx");
        panel.add(btnPrintArraylist, "cell 1 4,growx");

        btnPrintList = new JButton("Print list");
        btnPrintList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                System.out.println(list);
            }
        });
        // panel.add(btnPrintList, "cell 1 5,growx");

        // ************** Frame panels and panes ************** //
        loadTable(true);
    }

    public void loadTable(boolean init) {
        if (Main.toTrack) {
            panel2.remove(sc);
        }
        else {
            getList();
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
        panel2.add(sc, "cell 2 3");
        secretLabel.setText("  ");
        secretLabel.setText("");
    }

    public void addRow(String item) {
        if (inList(item)) {
            JOptionPane.showMessageDialog(null,
                "Item already in list, not added.");
        }
        else {
            list.add(item);
            txtInput.setText("");
            loadTable(false);
            saveList();
        }
    }

    public void removeRow(String item) {
        if (!inList(item)) {
            JOptionPane.showMessageDialog(null,
                "Item not in list to remove.");
        }
        else {
            list.remove(item);
            txtInput.setText("");
            loadTable(false);
            saveList();
        }
    }

    public static boolean inList(String item) {
        if (list.contains("- " + item) || list.contains(item)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void getList() {
        loadList();
    }

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

    public static List<String> getSavedList() {
        return list;
    }
}
