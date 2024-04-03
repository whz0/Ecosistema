package simulator.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
	
	private Controller _ctrl;
	private List<Map<String, List<Integer>>> _animals;
	private String[] _colNames;
	
	SpeciesTableModel(Controller ctrl) {
		this._ctrl = ctrl;
		this._animals = new ArrayList<>();
		int i = 0;
		this._colNames[i++] = "Species";
		for(Animal.State s: Animal.State.values()) {
			this._colNames[i++] = s.toString();
		}
	// TODO inicializar estructuras de datos correspondientes
	// TODO registrar this como observador
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
		
		Object s  = null;
		
		switch(columnIndex) {
		case 0: this._animals.get(rowIndex);
		break;
		case 1: s = num.get(columnIndex);
		}
		
		return s;
	}
	
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		
		for(AnimalInfo a: animals) {
			Map<AnimalInfo,String> type = new HashMap<>();
			type.put(a, a.get_genetic_code());
			this._animals.put(type, a.get_state());
		}
		fireTableDataChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		
		this._animals.clear();
		for(AnimalInfo a: animals) {
			Map<AnimalInfo,String> type = new HashMap<>();
			type.put(a, a.get_genetic_code());
			this._animals.put(type, a.get_state());
		}
		fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		
		this._animals = animals;
		for(Animal.State s: this._colNames) {
			if(s.equals(a.get_state()));
		}
		fireTableDataChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}
	
	}
