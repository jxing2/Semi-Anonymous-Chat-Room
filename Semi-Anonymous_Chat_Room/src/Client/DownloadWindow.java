package Client;

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DownloadWindow extends JFrame {
	//private File fileDir;
	private JButton download;
	private JButton cancel;
	private JScrollPane jsp;
	private Container c;
	private Client parent;
	private String savePath;
	private String fileDir;
	private int upperMargin=20;
	private int interval = 20;
	private int leftMargin=30;
	private int width = 340;
	private int sizeX = 400, sizeY = 400;//window size
	private JTree tree;
	private File folder;
	public DownloadWindow(final Client parent, String fileDir) {
		//tree = new TreeViewer(fileDir);
		this.parent = parent;
		this.fileDir = fileDir;
		System.out.println("fileDir="+fileDir);
		
		folder = new File(fileDir);
		File[] listOfFiles = folder.listFiles();
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(fileDir);
		tree = new JTree(top);
//		for (int i = 0; i < listOfFiles.length; i++) {
//			top.add(new DefaultMutableTreeNode(listOfFiles[i].getName()));
//		}
		c = getContentPane();
		c.setLayout(null);
		
		jsp = new JScrollPane(tree);
		jsp.setBounds(leftMargin, upperMargin, width, 300);
		download = new JButton("Download");
		download.setBounds(leftMargin, upperMargin += interval + 300, 120, 30);
		cancel = new JButton("Cancel");
		cancel.setBounds(sizeX - leftMargin - 120 , upperMargin, 120, 30);

		c.add(download);
		c.add(cancel);
		c.add(jsp);
		setSize(sizeX,sizeY);
		setResizable(false);
		Client.centreWindow(this);
		setVisible(true);
		//System.out.println("Download");
		//JTree tree = new JTree (addNodes(null,fileDir)) ;
		
		download.addActionListener(//send file upload request
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
//						if (fileName_txt.getText().trim().equals("")){
//							return;
//						}
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Choose a directory");
						//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fileChooser.setVisible(true);
						int returnVal = fileChooser.showSaveDialog(null);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	savePath = fileChooser.getSelectedFile().getAbsolutePath();
					    	dispose();
					    	System.out.println(savePath);
					    	Download saving = new Download();
					    	saving.start();
					    }
					}
				});	
		
		cancel.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
	}
}
