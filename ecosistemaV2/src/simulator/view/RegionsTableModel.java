package simulator.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	private Controller _ctrl;
	private Map<MapInfo.RegionData, Map<Animal.Diet, Integer>> _regions;
	private List<String> _colNames;

	RegionsTableModel(Controller ctrl) {

		this._colNames = new ArrayList<>();
		this._colNames.add("Row");
		this._colNames.add("Col");
		this._colNames.add("Desc.");
		for (int i = 0; i < Animal.Diet.values().length; i++) {
			this._colNames.add(Animal.Diet.values()[i].toString());
		}
		this._regions = new TreeMap<>(new Comparator<>() {
			@Override
			public int compare(RegionData o1, RegionData o2) {
				if (o1.row() > o2.row() || (o1.row() == o2.row() && o1.col() > o2.col()))
					return 1;
				else if (o1.row() < o2.row() || (o1.row() == o2.row() && o1.col() < o2.col()))
					return -1;
				else
					return 0;
			}
		});
		this._ctrl = ctrl;
		this._ctrl.addObserver(this);

	}

	@Override
	public int getRowCount() {

		return this._regions.size();
	}

	@Override
	public int getColumnCount() {

		return this._colNames.size();
	}

	@Override
	public String getColumnName(int col) {
		return _colNames.get(col);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object s = null;
		List<MapInfo.RegionData> regions = new ArrayList<>(this._regions.keySet());
		MapInfo.RegionData r = regions.get(rowIndex);

		switch (columnIndex) {
		case 0:
			s = r.row();
			break;
		case 1:
			s = r.col();
			break;
		case 2:
			s = r.r().toString();
			break;
		default: {
			Animal.Diet a = Animal.Diet.valueOf(this._colNames.get(columnIndex));
			s = this._regions.get(r).get(a);
		}
		}

		return s;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

		Iterator<RegionData> i = map.iterator();
		while (i.hasNext()) {
			RegionData r = i.next();
			update(r);
		}
		fireTableDataChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._regions.clear();
		fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		Iterator<RegionData> i = map.iterator();
		while (i.hasNext()) {
			RegionData r = i.next();
			update(r);
		}
		fireTableDataChanged();
	}

	private void update(RegionData r) {

		if (this._regions.containsKey(r))
			this._regions.get(r).clear();
		Map<Animal.Diet, Integer> diets = new HashMap<>();
		for (Animal.Diet d : Animal.Diet.values())
			diets.put(d, 0);

		for (AnimalInfo a : r.r().getAnimalsInfo()) {
			int num = diets.get(a.get_diet());
			diets.replace(a.get_diet(), ++num);
		}

		this._regions.put(r, diets);
	}
}
