package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.ScrollPane;
import javax.swing.JFormattedTextField;

/**
 * Main class, currently holding all the GUI code.
 * 
 * @author Austin Ayers
 * @version 9/11/18
 */

public class Main {

	public static final int FRAME_SIZE = 300;
	public JFrame frame;
	public static Main gui;
	public static JLabel lblCurrentlyNotTracking = new JLabel("Currently not tracking");

	/**
	 * Main method that builds the GUI.
	 */
	public static void main(String[] args) {
		gui = new Main();
	}

	/**
	 * Constructor that builds the frame.
	 */
	public Main() {
		frame = new JFrame();
		frame.setLocation(100, 100);
		frame.setSize(533, 381);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ProductivityPlus");
		initializeComponents();
		frame.setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmItem = new JMenuItem("Item 1");
		mnFile.add(mntmItem);

		JMenuItem mntmItem_1 = new JMenuItem("Item 2");
		mnFile.add(mntmItem_1);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem mntmItem_2 = new JMenuItem("Item 1");
		mnEdit.add(mntmItem_2);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmItem_3 = new JMenuItem("Item 1");
		mnHelp.add(mntmItem_3);
		frame.getContentPane().setLayout(new MigLayout("", "[531.00px]", "[337.00px]"));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "cell 0 0,growx,aligny top");

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setToolTipText("");
		tabbedPane.addTab("Program Timer", null, panel, null);
		panel.setLayout(new MigLayout("", "[121.00,grow][224.00,grow]", "[35.00][200.00,grow]"));

		JLabel lblTimerControls = new JLabel("Timer Controls");
		lblTimerControls.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblTimerControls, "cell 0 0");

		// JLabel lblCurrentlyNotTracking = new JLabel("Currently not tracking");
		lblCurrentlyNotTracking.setFont(new Font("Verdana", Font.BOLD, 12));
		panel.add(lblCurrentlyNotTracking, "cell 1 0");

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		panel.add(panel_3, "cell 0 1,grow");
		panel_3.setLayout(new MigLayout("", "[100]", "[25][][][]"));

		JButton btnNewButton = new JButton("Start Timer");
		panel_3.add(btnNewButton, "cell 0 0");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ProgramTimer programTimer = new ProgramTimer();
				Thread t = new Thread(programTimer);
				t.start();
				lblCurrentlyNotTracking.setText("Currently tracking...");
			}
		});
		btnNewButton.setFont(new Font("Verdana", Font.PLAIN, 11));

		JButton btnNewButton_1 = new JButton("Stop Timer");
		panel_3.add(btnNewButton_1, "cell 0 1");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ProgramTimer.stop();
				setStopLabel();
				// lblCurrentlyNotTracking.setText("Currently not tracking.");
			}
		});
		btnNewButton_1.setFont(new Font("Verdana", Font.PLAIN, 11));

		JButton btnNewButton_2 = new JButton("Open Output");
		panel_3.add(btnNewButton_2, "cell 0 2");
		btnNewButton_2.setFont(new Font("Verdana", Font.PLAIN, 11));

		JButton btnNewButton_3 = new JButton("Explore Data");
		panel_3.add(btnNewButton_3, "cell 0 3");
		btnNewButton_3.setFont(new Font("Verdana", Font.PLAIN, 11));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 1 1,grow");

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel_4);
		panel_4.setLayout(new MigLayout("", "[258.00px]", "[300.00px]"));

		JTextArea txtrSampleData = new JTextArea();
		txtrSampleData.setEditable(false);
		txtrSampleData.setRows(20);
		txtrSampleData.setText("sample data");
		panel_4.add(txtrSampleData, "cell 0 0,growx,aligny top");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.PINK);
		tabbedPane.addTab("Pomodoro Timer", null, panel_1, null);

		JPanel panel_2 = new JPanel();
		panel_2.setToolTipText("");
		panel_2.setBackground(Color.GRAY);
		tabbedPane.addTab("Break Stopper", null, panel_2, null);
		panel_2.setLayout(new MigLayout("", "[]", "[343.00]"));
		frame.setVisible(true);

	}

	/**
	 * Method to initialize all the components onto the frame.
	 */
	public void initializeComponents() {
	}

	public static void setStopLabel() {
		lblCurrentlyNotTracking.setText("Currently not tracking.");
	}

}
