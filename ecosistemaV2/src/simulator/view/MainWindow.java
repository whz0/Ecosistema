package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {

	private Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("[ECOSYSTEM SIMULATOR]");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		ControlPanel panel_control = new ControlPanel(this._ctrl);
		mainPanel.add(panel_control, BorderLayout.PAGE_START);

		StatusBar status_bar = new StatusBar(_ctrl);
		mainPanel.add(status_bar, BorderLayout.PAGE_END);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		InfoTable species_table = new InfoTable("Species", new SpeciesTableModel(_ctrl));
		species_table.setPreferredSize(new Dimension(500, 250));
		species_table.setVisible(true);
		contentPanel.add(species_table);

		InfoTable regions_table = new InfoTable("Regions", new RegionsTableModel(_ctrl));
		regions_table.setPreferredSize(new Dimension(500, 250));
		regions_table.setVisible(true);
		contentPanel.add(regions_table);
		
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				ViewUtils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
