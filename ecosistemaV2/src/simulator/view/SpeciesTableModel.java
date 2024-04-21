package simulator.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private Map<String, Map<Animal.State, Integer>> _animals;
	private List<String> _colNames;

	SpeciesTableModel(Controller ctrl) {
		this._animals = new HashMap<>();
		this._colNames = new ArrayList<>();
		this._colNames.add("Species");
		for (int i = 0; i < Animal.State.values().length; i++) {
			this._colNames.add(Animal.State.values()[i].toString());
		}
		this._ctrl = ctrl;
		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {

		return this._animals.size();
	}

	@Override
	public int getColumnCount() {

		return this._colNames.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object s = null;
		List<String> keys = new ArrayList<>(this._animals.keySet());
		String type = keys.get(rowIndex);

		switch (columnIndex) {
		case 0:
			s = type;
			break;
		default:
			Animal.State state = Animal.State.valueOf(this._colNames.get(columnIndex));
			s = this._animals.get(type).get(state);
			if (s == null)
				s = 0;
		}

		return s;
	}

	@Override
	public String getColumnName(int col) {
		return this._colNames.get(col);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

		for (AnimalInfo a : animals) {
			update(a);
		}
		fireTableDataChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {

		this._animals.clear();
		fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {

		update(a);
		fireTableDataChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {

		this._animals.clear();
		for (AnimalInfo a : animals) {
			update(a);
		}
		fireTableDataChanged();
	}

	private void update(AnimalInfo a) {

		Map<Animal.State, Integer> state = new HashMap<>();

		for (Animal.State s : Animal.State.values()) {
			state.put(s, 0);
		}
		if (this._animals.containsKey(a.get_genetic_code()))
			state = this._animals.get(a.get_genetic_code());

		int num = state.get(a.get_state());
		state.put(a.get_state(), ++num);

		this._animals.put(a.get_genetic_code(), state);
	}

}
