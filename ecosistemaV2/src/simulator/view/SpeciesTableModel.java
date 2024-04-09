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

	private Controller _ctrl;
	private Map<String, Map<Animal.State, Integer>> _animals;
	private String[] _colNames = { "Species", Animal.State.values().toString() };

	SpeciesTableModel(Controller ctrl) {
		this._ctrl = ctrl;
		this._animals = new HashMap<>();
		this._ctrl.addObserver(this);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public int getRowCount() {

		return this._animals.size();
	}

	@Override
	public int getColumnCount() {

		return this._colNames.length;
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
			Animal.State state = Animal.State.valueOf(this._colNames[columnIndex]);
			s = this._animals.get(type).get(state);
		}

		return s;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
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

		for (AnimalInfo a : animals) {
			update(a);
		}
		fireTableDataChanged();
	}

	private void update(AnimalInfo a) {

		Map<Animal.State, Integer> state = new HashMap<>();
		if (this._animals.containsKey(a.get_genetic_code())) {
			state = this._animals.get(a.get_genetic_code());
			if (state.containsKey(a.get_state())) {
				int n = state.get(a.get_state());
				state.replace(a.get_state(), ++n);
			} else {
				state.put(a.get_state(), 1);
			}
			this._animals.replace(a.get_genetic_code(), state);
		} else {
			state.put(a.get_state(), 1);
			this._animals.put(a.get_genetic_code(), state);
		}

	}

}
