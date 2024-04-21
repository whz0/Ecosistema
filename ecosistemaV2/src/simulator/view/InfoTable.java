package simulator.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.JTable;

public class InfoTable extends JPanel {

	private static final long serialVersionUID = 1L;
	String _title;
	TableModel _tableModel;

	InfoTable(String title, TableModel tableModel) {
		_title = title;
		_tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {

		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(_title));
		JTable table = new JTable(this._tableModel);
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(scroll);
		this.setVisible(true);
	}
}
