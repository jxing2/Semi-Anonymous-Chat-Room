package Client;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;


import java.io.File;
import java.util.*;

public class TableSendInfo extends AbstractTableModel {

	private String[] columnNames = new String[] { "File Name", "Progress",
			"Speed" };
	// private Object[][] data = ...//same as before...
	ArrayList<ArrayList<Object>> data;
	int col = 3;
	ArrayList<Send> al_sr;
	Send send;
	public TableSendInfo(ArrayList<Send> al_sr) {
		data = new ArrayList<ArrayList<Object>>();
		this.al_sr = al_sr;
		loadSend(al_sr);
	}
	private void loadSend(ArrayList<Send> al_sr)
	{
		//data = new ArrayList<ArrayList<Object>>();
		data.clear();
		ArrayList<Object> row;
		for (int i = 0; i < al_sr.size(); ++i) {
			if(al_sr.get(i).getPercentage()==100)
				continue;
			send = al_sr.get(i);
			row = new ArrayList<Object>();
			row.add(send.fileName);
			//row.add(new JProgressBar(0, 0, 100));
			row.add(send.getPercentage());
			long sendByte = send.getSendByte();
			long lastSendByte = send.getLastSentByte();
			send.setLastSentByte(sendByte);
			row.add((sendByte-lastSendByte)*2/1024);
			data.add(row);
		}
	}
	public void update()
	{
		loadSend(al_sr);
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
		if(columnIndex==1)
			return data.get(rowIndex).get(columnIndex);
		if(columnIndex==2)
			return data.get(rowIndex).get(columnIndex)+" KB/s";
		return data.get(rowIndex).get(columnIndex);
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

}
