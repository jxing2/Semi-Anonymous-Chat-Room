package Client;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import java.util.*;

public class TableInfo extends AbstractTableModel {

	private String[] columnNames = new String[] { "File Name", "Progress",
			"Speed(KB/s)" };
	// private Object[][] data = ...//same as before...
	ArrayList<ArrayList<Object>> data;
	int col = 3;

	public TableInfo(ArrayList<Send> al_sr) {
		data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> row;
		for (int i = 0; i < al_sr.size(); ++i) {
			row = new ArrayList<Object>();
			row.add(al_sr.get(i).fileName);
			row.add(new JProgressBar(0, 0, 100));
			row.add(0);
			data.add(row);
		}
		
	}
	public void update(ArrayList<Send> al_sr)
	{
		data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> row;
		for (int i = 0; i < al_sr.size(); ++i) {
			row = new ArrayList<Object>();
			row.add(al_sr.get(i).fileName);
			row.add(new JProgressBar(0, 0, 100));
			row.add(0);
			data.add(row);
		}
		fireTableDataChanged();
	}
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
	 public String getColumnName(int col) {
	      return columnNames[col];
	    }

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return col;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return data.get(rowIndex).get(columnIndex);
	}

	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		return false;
	}

}
