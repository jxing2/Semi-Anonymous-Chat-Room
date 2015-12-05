package Client;

import javax.swing.*;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
	public String getSavePath() {
		return savePath;
	}
	private String fileToSave;
	private int upperMargin=20;
	private int interval = 20;
	private int leftMargin=30;
	private int width = 340;
	private int sizeX = 400, sizeY = 424;//window size
	private JTree tree;
	private File folder;
	DefaultMutableTreeNode selectedNode;
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Share");
	public DownloadWindow(final Client parent) {
		//tree = new TreeViewer(fileDir);
		this.parent = parent;
//		for (int i = 0; i < listOfFiles.length; i++) {
//			top.add(new DefaultMutableTreeNode(listOfFiles[i].getName()));
//		}
		c = getContentPane();
		c.setLayout(null);
		tree = new JTree(top);
		jsp = new JScrollPane(tree);
		jsp.setBounds(leftMargin, upperMargin, width, 300);
		download = new JButton("Download");
		download.setBounds(leftMargin, upperMargin += interval + 300, 120, 30);
		cancel = new JButton("Cancel");
		cancel.setBounds(sizeX - leftMargin - 120 , upperMargin, 120, 30);

		c.add(download);
		c.add(cancel);
		c.add(jsp);
		//c.add(tree);
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
						//fileChooser.setMultiSelectionEnabled(false);
						int returnVal = fileChooser.showSaveDialog(null);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	savePath = fileChooser.getSelectedFile().getAbsolutePath();
					    	parent.sendMessage(fileToSave, CommandType.DownloadFileRequest);
					    	dispose();
					    	System.out.println(savePath);
					    }
					}
				});	
		
		cancel.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
		tree.addTreeSelectionListener(new treeSelectionListener());
	}
	public void reloadFileList(String[] fileList)
	{
		
		for (int i = 0; i < fileList.length; i++) {
			top.add(new DefaultMutableTreeNode(fileList[i]));
		}
		tree.expandRow(0);
		
	}
	class treeSelectionListener implements TreeSelectionListener
	{

		public void valueChanged(TreeSelectionEvent e) {
			// TODO Auto-generated method stub
			selectedNode = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
			fileToSave = selectedNode.getUserObject().toString();
			
		}
		
	}
}
