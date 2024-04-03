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
	private int _time;
	private int _num_animales;
	private Dimension _dimension;

	// TODO Añadir los atributos necesarios.
	StatusBar(Controller ctrl) {
		initGUI();
		this._ctrl = ctrl;
		this._num_animales = 0;
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
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		JLabel time_label = new JLabel("time");
		JLabel num_animal_label = new JLabel("num_animals");
		JLabel dimension_label = new JLabel("dimesion");
		this.add(dimension_label);
		this.add(num_animal_label);
		this.add(time_label);
	}
	// TODO el resto de métodos van aquí…

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub

	}
}
