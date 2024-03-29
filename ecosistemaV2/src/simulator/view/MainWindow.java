package simulator.view;

import java.awt.BorderLayout;

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
// TODO crear StatusBar y añadirlo en PAGE_END de mainPanel

// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
// TODO crear la tabla de especies y añadirla a contentPanel.
// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
// TODO crear la tabla de regiones.
// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
// TODO llama a ViewUtils.quit(MainWindow.this) en el método windowClosing
		addWindowListener(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
