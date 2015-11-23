package Client;

import java.io.*;
//import java.net.*;
//import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ChooserWindow extends JFrame{
	//pop out a window to choose a file
	private JTextField checkField;//display the file you choose
	private JButton choose;
	private JButton send;//pop out chooser window when clicked
	private JButton cancle;
	private Container pane;
	private String filePath;
	private String filename;
	private BufferedInputStream fileBufferIn;
	private BufferedOutputStream fileBufferOut;
	private FileInputStream fileIn;
	
	public ChooserWindow(){
		pane = getContentPane();
		pane.setLayout(null);
		//chooserWindow = new JFrame();
		checkField= new JTextField();
		checkField.setBounds(20,30,200,30);
		checkField.setEditable(false);
		choose = new JButton("Choose");
		choose.setBounds(240,30,100,30);
		send = new JButton("Send");
		send.setBounds(70,75,100,30);
		cancle = new JButton("Cancle");
		cancle.setBounds(190,75,100,30);
		
		pane.add(checkField);
		pane.add(choose);
		pane.add(send);
		pane.add(cancle);
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
					    	filename = fileChooser.getSelectedFile().getName();
					    	checkField.setText(filename);
					    	filePath = fileChooser.getSelectedFile().getAbsolutePath();
					    }
					}
			
		});
		
		cancle.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
		
		send.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						send();
					}
				});
		
		
	}
	public void send(){
		if (checkField.getText().trim().equals("")){
			return;
		}
		try{
			File file = new File(filename);
			fileIn = new FileInputStream(file);
			fileBufferIn = new BufferedInputStream(fileIn);
			byte[] buff = new byte[2048];
			int num = fileBufferIn.read(buff);
			while( num != -1 ){
				fileBufferOut.write(buff, 0, num);
				fileBufferOut.flush();
				num = fileBufferIn.read(buff);
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}
}

