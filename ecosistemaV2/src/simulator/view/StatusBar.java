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

	private Controller _ctrl;
	private double _time;
	private int _num_animales;
	private int[] _dimension = { 0, 0, 0, 0 };

	// TODO Añadir los atributos necesarios.
	StatusBar(Controller ctrl) {
		this._ctrl = ctrl;
		this._num_animales = 0;
		this._time = 0.0;
		initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		// TODO Crear varios JLabel para el tiempo, el número de animales, y la
		// dimensión y añadirlos al panel. Puedes utilizar el siguiente código
		// para añadir un separador vertical:
		//
		// JSeparator s = new JSeparator(JSeparator.VERTICAL);
		// s.setPreferredSize(new Dimension(10, 20));
		// this.add(s);
		JLabel time_label = new JLabel("Time: " + this._time);
		JLabel num_animal_label = new JLabel("Total Animals: " + this._num_animales);
		JLabel dimension_label = new JLabel("Dimesion: " + this._dimension[0] + "x" + this._dimension[1] + " "
				+ this._dimension[2] + "x" + this._dimension[3]);
		this.add(time_label);
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1);
		this.add(num_animal_label);
		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(10, 20));
		this.add(s2);
		this.add(dimension_label);

	}
	// TODO el resto de métodos van aquí…

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this._time = time;
		this._num_animales = animals.size();
		this._dimension = new int[] { map.get_width(), map.get_height(), map.get_rows(), map.get_cols() };
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._num_animales = 0;
		this._time = 0.0;
		this._dimension = new int[] { 0, 0, 0, 0 };
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		this._num_animales++;
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}
}
