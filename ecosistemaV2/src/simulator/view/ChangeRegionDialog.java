package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JLabel;

import org.json.JSONObject;
import org.json.JSONArray;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> _regionsModel;
	private DefaultComboBoxModel<String> _fromRowModel;
	private DefaultComboBoxModel<String> _toRowModel;
	private DefaultComboBoxModel<String> _fromColModel;
	private DefaultComboBoxModel<String> _toColModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _regionsInfo;
	private String[] _headers = { "Key", "Value", "Description" };

	private JLabel _help_text;

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this._ctrl = ctrl;
		initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JPanel text_panel = new JPanel();
		mainPanel.add(text_panel);
		JPanel table_panel = new JPanel();
		mainPanel.add(table_panel);
		JPanel combobox_panel = new JPanel();
		mainPanel.add(combobox_panel);
		JPanel button_panel = new JPanel();
		mainPanel.add(button_panel);

		this._help_text = new JLabel(
				"<html><p>Select a region type, the rows/cols interval, and provide values for the parameters in the <b>Value Column</b> (default values are used for parameters with no value)</p></html>");
		this._help_text.setOpaque(false);
		this._help_text.setPreferredSize(new Dimension(675, 50));
		text_panel.add(this._help_text);

		_regionsInfo = Main._regions_factory.get_info();

		_dataTableModel = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {

				return column == 1;
			}

		};
		_dataTableModel.setColumnIdentifiers(_headers);

		JTable data = new JTable(this._dataTableModel);

		JScrollPane scroll = new JScrollPane(data, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(675, 300));
		table_panel.add(scroll);

		_regionsModel = new DefaultComboBoxModel<>();

		for (JSONObject r : this._regionsInfo) {
			String s = r.getString("type");
			this._regionsModel.addElement(s);
		}

		JLabel type_label = new JLabel(" Region type: ");
		combobox_panel.add(type_label);
		JComboBox<String> regions_box = new JComboBox<>(this._regionsModel);
		regions_box.addItemListener((e) -> {
			update(_regionsModel.getIndexOf(regions_box.getSelectedItem()));
		});
		combobox_panel.add(regions_box);

		this._fromColModel = new DefaultComboBoxModel<String>();
		this._toColModel = new DefaultComboBoxModel<String>();
		this._toRowModel = new DefaultComboBoxModel<String>();
		this._fromRowModel = new DefaultComboBoxModel<String>();

		JLabel row_label = new JLabel(" Row from/to: ");
		combobox_panel.add(row_label);
		JComboBox<String> fromRow_box = new JComboBox<>(this._fromRowModel);
		combobox_panel.add(fromRow_box);
		JComboBox<String> toRow_box = new JComboBox<>(this._toRowModel);
		combobox_panel.add(toRow_box);

		JLabel col_label = new JLabel(" Col from/to: ");
		combobox_panel.add(col_label);
		JComboBox<String> fromCol_box = new JComboBox<>(this._fromColModel);
		combobox_panel.add(fromCol_box);
		JComboBox<String> toCol_box = new JComboBox<>(this._toColModel);
		combobox_panel.add(toCol_box);

		JButton cancel_button = new JButton("Cancel");
		cancel_button.addActionListener((e) -> {
			ChangeRegionsDialog.this.setVisible(false);
		});
		button_panel.add(cancel_button);

		JButton ok_button = new JButton("OK");
		ok_button.addActionListener((e) -> {
			try {

				JSONObject region_spec = new JSONObject();
				JSONObject region_data = new JSONObject();
				for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
					region_data.put(_dataTableModel.getValueAt(i, 0).toString(), _dataTableModel.getValueAt(i, 1));
				}

				String region_type = regions_box.getSelectedItem().toString();
				region_spec.put("type", region_type);
				region_spec.put("data", region_data);

				JSONArray rows = new JSONArray();
				rows.put(fromRow_box.getSelectedItem());
				rows.put(toRow_box.getSelectedItem());

				JSONArray cols = new JSONArray();
				cols.put(fromCol_box.getSelectedItem());
				cols.put(toCol_box.getSelectedItem());

				JSONObject region = new JSONObject();
				region.put("row", rows);
				region.put("col", cols);
				region.put("spec", region_spec);

				JSONArray regions = new JSONArray();
				regions.put(region);
				_ctrl.set_regions(regions);
				ChangeRegionsDialog.this.setVisible(false);
			} catch (Exception ex) {
				ViewUtils.showErrorMsg(ex.getMessage());
			}

		});
		button_panel.add(ok_button);

		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open(Frame parent) {
		setLocation(//
				parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, //
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

		updateRowCols(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this._toRowModel.removeAllElements();
		this._fromRowModel.removeAllElements();
		this._toColModel.removeAllElements();
		this._fromColModel.removeAllElements();
		updateRowCols(map);
		this._dataTableModel.fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}

	private void update(int i) {

		int rows = this._dataTableModel.getRowCount();
		for (int j = rows - 1; j >= 0; j--) {
			this._dataTableModel.removeRow(j);
		}
		JSONObject info = this._regionsInfo.get(i);
		JSONObject data = info.getJSONObject("data");

		for (String s : data.keySet()) {
			this._dataTableModel.addRow(new Object[] { s, null, data.get(s) });

		}
		this._dataTableModel.fireTableDataChanged();
	}

	private void updateRowCols(MapInfo map) {
		for (int i = 0; i < map.get_cols(); i++) {
			_toColModel.addElement("" + i);
			_fromColModel.addElement("" + i);
		}
		for (int i = 0; i < map.get_rows(); i++) {
			_toRowModel.addElement("" + i);
			_fromRowModel.addElement("" + i);
		}
	}

}
