package Client;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class StatusWindow extends JFrame {

	JTabbedPane tabbedPane = new JTabbedPane();
	ArrayList<JTable> al_table = new ArrayList<JTable>();
	ArrayList<Send> al_send;
	ArrayList<Download> al_download;
	TableSendInfo upLoadTable;
	TableDownloadInfo downloadTable;
	TableDoneInfo doneTable;
	JTable table_download, table_upload, table_done;
	ProgressBarWorker pbwUpload,pbwDownload,pbwDone;
	public StatusWindow(ArrayList<Send> al_send, ArrayList<Download> al_download) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
		}
		this.al_send = al_send;
		this.al_download = al_download;

		downloadTable = new TableDownloadInfo(al_download);
		table_download = new JTable(downloadTable);
		table_download.setFillsViewportHeight(true);
		table_download.setPreferredSize(new Dimension(400, 520));

		JComponent panel1 = makePanel();
		tabbedPane.addTab("Downloading", null, panel1, "");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		JScrollPane scrollPane_download = new JScrollPane(table_download);
		panel1.add(scrollPane_download, BorderLayout.CENTER);

		upLoadTable = new TableSendInfo(al_send);
		table_upload = new JTable(upLoadTable);
		table_upload.setFillsViewportHeight(true);
		table_upload.setPreferredSize(new Dimension(400, 520));

		JComponent panel2 = makePanel();
		tabbedPane.addTab("Uploading", null, panel2, "");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		JScrollPane scrollPane_send = new JScrollPane(table_upload);
		panel2.add(scrollPane_send, BorderLayout.CENTER);

		doneTable = new TableDoneInfo(al_send, al_download);
		table_done = new JTable(doneTable);
		table_done.setFillsViewportHeight(true);
		table_done.setPreferredSize(new Dimension(400, 520));

		JComponent panel3 = makePanel();
		tabbedPane.addTab("Finished", null, panel3, "");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		JScrollPane scrollPane_done = new JScrollPane(table_done);
		panel3.add(scrollPane_done, BorderLayout.CENTER);

		add(tabbedPane, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		table_download.getColumn("Progress").setCellRenderer(
				new ProgressCellRender());
		table_upload.getColumn("Progress").setCellRenderer(
				new ProgressCellRender());
		pbwUpload = new ProgressBarWorker(upLoadTable) ;
		pbwUpload.execute();
		pbwDownload = new ProgressBarWorker(downloadTable) ;
		pbwDownload.execute();
		pbwDone = new ProgressBarWorker(doneTable) ;
		pbwDone.execute();
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

	public class ProgressCellRender extends JProgressBar implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			int progress = 0;
			if (value instanceof Integer) {
				progress = (int) value;
				System.out.println(progress);
			}
			setValue(progress);
			return this;
		}
	}

	public class ProgressBarWorker extends SwingWorker<Integer, Integer> {

		AbstractTableModel atm;

		public ProgressBarWorker(AbstractTableModel atm) {
			this.atm = atm;
		}
		

		protected Integer doInBackground() throws Exception {
			// TODO Auto-generated method stub
			while (true) {
				if (atm instanceof TableDownloadInfo) {
					TableDownloadInfo tdi = (TableDownloadInfo) atm;
					tdi.update();
				}
				if (atm instanceof TableSendInfo) {
					TableSendInfo tsi = (TableSendInfo) atm;
					tsi.update();
				}
				if (atm instanceof TableDoneInfo) {
					TableDoneInfo tsi = (TableDoneInfo) atm;
					tsi.update();
				}
				Thread.sleep(500);
			}
		}
	}

	protected void close() {
		// TODO Auto-generated method stub
		pbwUpload.cancel(true);
		pbwDownload.cancel(true);
		pbwDone.cancel(true);
		this.dispose();
	}

}
