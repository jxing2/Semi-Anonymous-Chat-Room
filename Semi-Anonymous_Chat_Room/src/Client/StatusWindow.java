package Client;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class StatusWindow extends JFrame{

	JTabbedPane tabbedPane = new JTabbedPane();
	ArrayList<JTable> al_table = new ArrayList<JTable>();
	ArrayList<Send> al_send;
	ArrayList<Download> al_download;
	TableInfo downloadTable, upLoadTable, doneTable;
	public StatusWindow(ArrayList<Send> al_send, ArrayList<Download> al_download)
	{
		this.al_send = al_send;
		this.al_download = al_download;
		downloadTable = new TableInfo(al_send);
		JComponent panel1 = makePanel();
		tabbedPane.addTab("Downloading", null, panel1,
                "Does nothing");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		JComponent panel2 = makePanel();
		tabbedPane.addTab("Uploading", null, panel2,
                "Does nothing");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = makePanel();
		tabbedPane.addTab("Finished", null, panel3,
                "Does nothing");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		add(tabbedPane, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		setSize(400, 550);
		Client.centreWindow(this);
		setResizable(false);
		//this.pack();
		this.setVisible(true);
	}
    protected JComponent makePanel() {
        JPanel panel = new JPanel(true);
        return panel;
    }
    protected void makeTable() {
    	//download 
    	//al_download
    	JTable table1 = new JTable();
    	//upload
    	String[] columnNames = {"File Name","","Process"};
    	Object[][] data = new Object[al_send.size()][3];
    	JTable table = new JTable(data, columnNames);
    	table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

    	
        return;
    }
	protected void close() {
		// TODO Auto-generated method stub
		this.dispose();
	}
}





