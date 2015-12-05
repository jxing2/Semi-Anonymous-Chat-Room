package Client;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class TableInfo extends AbstractTableModel{

	private String[] columnNames = new String[]{"File Name","Progress", "Speed" };
	//private Object[][] data = ...//same as before...
	ArrayList<ArrayList<Object>> data ;	

	public TableInfo()
	{
		data = new ArrayList<ArrayList<Object>>();
		for(int i = 0 ; i < )
	}
	
	
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
