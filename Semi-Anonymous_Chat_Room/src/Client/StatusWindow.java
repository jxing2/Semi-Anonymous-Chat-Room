package Client;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
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
	TableSendInfo upLoadTable;
	TableDownloadInfo downloadTable; 
	TableDoneInfo doneTable;
	JTable table_download, table_upload, table_done;
	public StatusWindow(ArrayList<Send> al_send, ArrayList<Download> al_download)
	{
		this.al_send = al_send;
		this.al_download = al_download;
		
		downloadTable = new TableDownloadInfo(al_download);
		table_download = new JTable(downloadTable);
		table_download.setFillsViewportHeight(true);
		table_download.setPreferredSize(new Dimension(400, 520));
		
		JComponent panel1 = makePanel();
		tabbedPane.addTab("Downloading", null, panel1,"");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		JScrollPane scrollPane_download = new JScrollPane(table_download);
		panel1.add(scrollPane_download,BorderLayout.CENTER);
		
		
		
		upLoadTable = new TableSendInfo(al_send);
		table_upload = new JTable(upLoadTable);
		table_upload.setFillsViewportHeight(true);
		table_upload.setPreferredSize(new Dimension(400, 520));
		
		JComponent panel2 = makePanel();
		tabbedPane.addTab("Uploading", null, panel2,"");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		JScrollPane scrollPane_send = new JScrollPane(table_upload);
		panel2.add(scrollPane_send,BorderLayout.CENTER);
		
		
		doneTable = new TableDoneInfo(al_send,al_download);
		table_done = new JTable(doneTable);
		table_done.setFillsViewportHeight(true);
		table_done.setPreferredSize(new Dimension(400, 520));
		
		JComponent panel3 = makePanel();
		tabbedPane.addTab("Finished", null, panel3,"");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		JScrollPane scrollPane_done = new JScrollPane(table_done);
		panel3.add(scrollPane_done,BorderLayout.CENTER);
		
		
		add(tabbedPane, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		setSize(400, 550);
		Client.centreWindow(this);
		setResizable(false);
		this.pack();
		this.setVisible(true);
	}
    protected JComponent makePanel() {
        JPanel panel = new JPanel(true);
        return panel;
    }
    protected void makeTable() {
    	//download 
    	//al_download
    	
    	
    	//upload
//    	String[] columnNames = {"File Name","","Process"};
//    	Object[][] data = new Object[al_send.size()][3];
//    	JTable table = new JTable(data, columnNames);
//    	table.setPreferredScrollableViewportSize(new Dimension(500, 70));
//      table.setFillsViewportHeight(true);

    	
        return;
    }
	protected void close() {
		// TODO Auto-generated method stub
		this.dispose();
	}

}





