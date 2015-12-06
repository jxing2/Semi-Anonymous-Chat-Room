package Client;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import java.util.*;

public class TableDoneInfo extends AbstractTableModel {

	private String[] columnNames = new String[] { "File Name" };
	// private Object[][] data = ...//same as before...
	ArrayList<Object> data;
	int col = 1;
	ArrayList<Send> al_sr;
	ArrayList<Download> al_download;
	public TableDoneInfo(ArrayList<Send> al_sr, ArrayList<Download> al_download) {
		data = new ArrayList<Object>();
		this.al_download = al_download;
		this.al_sr = al_sr;
		load(al_sr, al_download);
	}

	private void load(ArrayList<Send> al_sr, ArrayList<Download> al_download) {
		data.clear();
		for (int i = 0; i < al_sr.size(); ++i) {
			if (al_sr.get(i).getPercentage() != 100)
				continue;
			data.add(al_sr.get(i).fileName);
		}
		for (int i = 0; i < al_download.size(); ++i) {
			if (al_download.get(i).getPercentage() != 100)
				continue;
			data.add(al_download.get(i).fileName);
		}
	}

	public void update() {
		load(al_sr, al_download);
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
		return data.get(rowIndex);
	}

	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		return false;
	}

}
