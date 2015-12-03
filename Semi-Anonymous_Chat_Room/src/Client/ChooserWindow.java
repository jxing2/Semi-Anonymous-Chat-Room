package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChooserWindow extends JFrame{
	//pop out a window to choose a file
	private JTextField checkField;//display the file you choose
	private JButton choose;
	private JButton send;//pop out chooser window when clicked
	private JButton cancel;
	private Container pane;
	private String filePath;
	private String fileName;
	//private BufferedInputStream fileBufferIn;
	//private BufferedOutputStream fileBufferOut;
	//private FileInputStream fileIn;
	//private int filePort;
	private Client parent;
	private long size;
	public ChooserWindow(final Client parent){
		pane = getContentPane();
		this.parent = parent;
		pane.setLayout(null);
		//chooserWindow = new JFrame();
		checkField= new JTextField();
		checkField.setBounds(20,30,200,30);
		checkField.setEditable(false);
		choose = new JButton("Choose");
		choose.setBounds(240,30,100,30);
		send = new JButton("Send");
		send.setBounds(70,75,100,30);
		cancel = new JButton("Cancel");
		cancel.setBounds(190,75,100,30);
		
		pane.add(checkField);
		pane.add(choose);
		pane.add(send);
		pane.add(cancel);
		setSize(360,150);
		setResizable(false);
		Client.centreWindow(this);
		this.setVisible(true);
		
		choose.addActionListener(//show file name in text field and get the absolute path
				new ActionListener(){
					public void actionPerformed(final ActionEvent e){
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Choose a file");
						fileChooser.setVisible(true);
						int returnVal = fileChooser.showOpenDialog(null);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	fileName = fileChooser.getSelectedFile().getName();
					    	
					    	filePath = fileChooser.getSelectedFile().getAbsolutePath();
					    	checkField.setText(filePath);
					    }
					}
			
		});
		
		cancel.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
		
		send.addActionListener(//send file upload request
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if (checkField.getText().trim().equals("")){
							return;
						}
						File file = new File(filePath);
						size = file.length();
						parent.sendMessage(
								fileName+"\n"+size+"\n"+filePath,
								CommandType.SendFileRequest);
						dispose();
					}
				});		
	}
}

