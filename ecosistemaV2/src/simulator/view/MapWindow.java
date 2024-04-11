package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class MapWindow extends JFrame implements EcoSysObserver {

	private Controller _ctrl;
	private AbstractMapViewer _viewer;
	private Frame _parent;

	MapWindow(Frame parent, Controller ctrl) {
		super("[MAP VIEWER]");
		_ctrl = ctrl;
		_parent = parent;
		this._ctrl.addObserver(this);
		intiGUI();
	}

	private void intiGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this._viewer = new MapViewer();
		this._viewer.setAlignmentY(CENTER_ALIGNMENT);
		mainPanel.add(this._viewer);

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				_ctrl.removeObserver(MapWindow.this);

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

		this.add(mainPanel);
		if (_parent != null)
			setLocation(_parent.getLocation().x + _parent.getWidth() / 2 - getWidth() / 2,
					_parent.getLocation().y + _parent.getHeight() / 2 - getHeight() / 2);
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> {
			_viewer.reset(time, map, animals);
			pack();
		});
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> {
			_viewer.reset(time, map, animals);
			pack();
		});
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		SwingUtilities.invokeLater(() -> {
			_viewer.update(animals, time);
		});
	}
}
