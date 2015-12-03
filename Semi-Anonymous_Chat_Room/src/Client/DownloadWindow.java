package Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DownloadWindow extends JFrame {
	private File fileDir;
	private JButton download;
	private JButton cancel;
	private JScrollPane jsp;
	private JTextField checkField;
	private Container c;
	private Client parent;
	public DownloadWindow(final Client parent) {
		//this.fileDir = fileDir;
		this.parent = parent;
		c = getContentPane();
		c.setLayout(null);
		checkField = new JTextField();
		checkField.setEditable(false);
		checkField.setBounds(25, 390, 300, 30);
		jsp = new JScrollPane();
		jsp.setBounds(25, 25, 300, 350);
		download = new JButton("Download");
		download.setBounds(25, 425, 120, 30);
		cancel = new JButton("Cancel");
		cancel.setBounds(205, 425, 120, 30);
		c.add(checkField);
		c.add(jsp);
		c.add(download);
		c.add(cancel);
		setSize(350,490);
		setResizable(false);
		Client.centreWindow(this);
		setVisible(true);
		//System.out.println("Download");
		//JTree tree = new JTree (addNodes(null,fileDir)) ;
	}
}
