package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChooserWindow extends JFrame{
	//pop out a window to choose a file
	private JFrame chooserWindow;
	private JTextField filename;//display the file you choose
	private JButton choose;
	private JButton send;//pop out chooser window when clicked
	private JButton cancle;
	private Container pane;
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
		
		choose.addActionListener(
				new ActionListener(){
					public void actionPerformed(final ActionEvent e){
						JFileChooser fileChooser = new JFileChooser();
						//pane.add(fileChooser);
						chooserWindow = new JFrame(); 
						chooserWindow.getContentPane().add(fileChooser);
						chooserWindow.pack();
						Client.centreWindow(chooserWindow);
						chooserWindow.setVisible(true);
						fileChooser.setDialogTitle("Choose a file");
						fileChooser.setVisible(true);
						System.out.println("works");
					}
			
		});
		
		cancle.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
		
		
	}
}

