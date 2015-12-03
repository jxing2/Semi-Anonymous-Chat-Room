package Client;

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DownloadWindow extends JFrame {
	//private File fileDir;
	private JLabel path;
	private JLabel fileName;
	private JButton download;
	private JButton cancel;
	private JButton choosePath;
	private JButton chooseFile;
	private JScrollPane jsp;
	private JTextField path_txt;
	private JTextField fileName_txt;
	private Container c;
	private Client parent;
	private String savePath;
	private String fileDir;
	private int upperMargin=20;
	private int interval = 40;
	private int leftMargin=30;
	private int width = 240;
	private int sizeX = 400, sizeY = 280;//window size
	TreeViewer tree;
	public DownloadWindow(final Client parent, String fileDir) {
		tree = new TreeViewer(fileDir);
		this.parent = parent;
		this.fileDir = fileDir; 
		c = getContentPane();
		c.setLayout(null);
		fileName = new JLabel("File :");
		fileName.setBounds(leftMargin,upperMargin , width, 30);
		fileName_txt = new JTextField();
		fileName_txt.setEditable(false);
		fileName_txt.setBounds(leftMargin, upperMargin += interval, width, 30);
		chooseFile = new JButton("Choose");
		chooseFile.setBounds(leftMargin+width+20,upperMargin , 80, 30);
		
		path = new JLabel("Save to:");
		path.setBounds(leftMargin, upperMargin += interval , width, 30);
		path_txt = new JTextField();
		path_txt.setEditable(false);
		path_txt.setBounds(leftMargin, upperMargin += interval, width, 30);
		choosePath = new JButton("Choose");
		choosePath.setBounds(leftMargin+width+20,upperMargin , 80, 30);
		
		download = new JButton("Download");
		download.setBounds(leftMargin, upperMargin += interval*1.5, 120, 30);
		cancel = new JButton("Cancel");
		cancel.setBounds(sizeX - leftMargin - 120 , upperMargin, 120, 30);
		c.add(fileName);
		c.add(fileName_txt);
		c.add(chooseFile);
		c.add(path);
		c.add(path_txt);
		c.add(choosePath);
		c.add(download);
		c.add(cancel);
		setSize(sizeX,sizeY);
		setResizable(false);
		Client.centreWindow(this);
		setVisible(true);
		//System.out.println("Download");
		//JTree tree = new JTree (addNodes(null,fileDir)) ;
		chooseFile.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
		choosePath.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Choose a directory");
						//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fileChooser.setVisible(true);
						int returnVal = fileChooser.showSaveDialog(null);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	//fileName = fileChooser.getSelectedFile().getName();
					    	
					    	savePath = fileChooser.getSelectedFile().getAbsolutePath();
					    	path_txt.setText(savePath);
					    }
					}
				});
		
		download.addActionListener(//send file upload request
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if (fileName_txt.getText().trim().equals("")){
							return;
						}
						//parent.sendMessage(message,CommandType.DownloadFileRequest);
//						File file = new File(filePath);
//						size = file.length();
//						parent.sendMessage(
//								fileName+"\n"+size+"\n"+filePath,
//								CommandType.SendFileRequest);
						dispose();
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
