package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChooserWindow extends JFrame{
	//pop out a window to choose a file
	private JTextField filename;//display the file you choose
	private JButton choose;
	private JButton send;//pop out chooser window when clicked
	private JButton cancle;
	private Container pane;
	private String filePath;
	public ChooserWindow(){
		pane = getContentPane();
		pane.setLayout(null);
		//chooserWindow = new JFrame();
		filename= new JTextField();
		filename.setBounds(20,30,200,30);
		filename.setEditable(false);
		choose = new JButton("Choose");
		choose.setBounds(240,30,100,30);
		send = new JButton("Send");
		send.setBounds(70,75,100,30);
		cancle = new JButton("Cancle");
		cancle.setBounds(190,75,100,30);
		
		pane.add(filename);
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
					    	filename.setText(fileChooser.getSelectedFile().getName());
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
		System.out.println(filePath);
	}
}

