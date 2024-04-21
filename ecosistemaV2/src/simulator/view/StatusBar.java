package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JLabel _time_label;
	private JLabel _num_animals_label;
	private JLabel _dimension_label;

	// TODO Añadir los atributos necesarios.
	StatusBar(Controller ctrl) {
		this._ctrl = ctrl;
		this._num_animals_label = new JLabel("Total Animals: ");
		this._time_label = new JLabel("Time: ");
		this._dimension_label = new JLabel("Dimension: ");
		this._ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));

		this.add(_time_label);
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1);
		this.add(_num_animals_label);
		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(10, 20));
		this.add(s2);
		this.add(_dimension_label);

	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		update(time, map, animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		update(time, map, animals);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		update(time, map, animals);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		update(time, map, animals);
	}

	private void update(double time, MapInfo map, List<AnimalInfo> animals) {
		this._time_label.setText("Time: " + String.format("%.3f", time));
		this._num_animals_label.setText("Total Animals: " + animals.size());
		this._dimension_label.setText(
				"Dimension: " + map.get_width() + "x" + map.get_height() + " " + map.get_cols() + "x" + map.get_rows());
	}
}
