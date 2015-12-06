package Client;

import javax.swing.table.AbstractTableModel;

import java.util.*;

public class TableDownloadInfo extends AbstractTableModel {

	private String[] columnNames = new String[] { "File Name", "Progress",
			"Speed" };
	// private Object[][] data = ...//same as before...
	ArrayList<ArrayList<Object>> data;
	int col = 3;
	ArrayList<Download> al_download;
	Download download;
	public TableDownloadInfo(ArrayList<Download> al_download) {
		// TODO Auto-generated constructor stub
		data = new ArrayList<ArrayList<Object>>();
		this.al_download = al_download;
		loadDownload(al_download);
	}
	private void loadDownload(ArrayList<Download> al_sr)
	{
		data.clear();
		ArrayList<Object> row;
		for (int i = 0; i < al_sr.size(); ++i) {
			if(al_sr.get(i).getPercentage()==100)
				continue;
			row = new ArrayList<Object>();
			row.add(al_sr.get(i).fileName);
			row.add(al_sr.get(i).getPercentage());
			download = al_sr.get(i);
			long sendByte = download.getSendByte();
			long lastSendByte = download.getLastSendByte();
			download.setLastSendByte(sendByte);
			row.add((sendByte-lastSendByte)*2/1024);
			data.add(row);
		}
	}
	public void update()
	{
		loadDownload(al_download);
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
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		return false;
	}

}
