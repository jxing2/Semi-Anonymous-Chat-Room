package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

public class Sign_Up extends JFrame{
	private JLabel name ;
	private JTextField name_txt;
	private JLabel pwd ;
	private JTextField pwd_txt;
	private JLabel again_pwd ;
	private JTextField again_pwd_txt;
	private JButton confirm;
	private Container pane;
	private int upperMargin=20;
	private int interval = 40;
	private int leftMargin=30;
	private int width = 241;
	public Sign_Up()
	{
		pane = getContentPane();
		pane.setLayout(null);
		name = new JLabel("Name :");
		name.setBounds(leftMargin,upperMargin , width, 30);
		name_txt = new JTextField();
		name_txt.setBounds(leftMargin, upperMargin+=interval, width, 30);
		pwd = new JLabel("Password :");
		pwd.setBounds(leftMargin,upperMargin+=interval,  width, 30);
		pwd_txt = new JTextField();
		pwd_txt.setBounds(leftMargin,upperMargin+=interval,  width, 30);
		again_pwd = new JLabel("Confirm your password :");
		again_pwd.setBounds( leftMargin,upperMargin+=interval, width, 30);
		again_pwd_txt = new JTextField();
		again_pwd_txt.setBounds(leftMargin,upperMargin+=interval,  width, 30);
		confirm = new JButton("Sign Up!");
		confirm.setBounds( leftMargin,upperMargin+=interval+42, width, 30);
		
		pane.add(name);
		pane.add(name_txt);
		pane.add(pwd);
		pane.add(pwd_txt);
		pane.add(again_pwd);
		pane.add(again_pwd_txt);
		pane.add(confirm);
		
		confirm.addActionListener(
				new ActionListener(){
					public void actionPerformed(final ActionEvent e)
					{
						confirm();
					}
		});
		
		
		setSize(300,400);
		Client.centreWindow(this);
		setResizable(false);
		this.setVisible(true);
	}
	protected void confirm() {
		// TODO Auto-generated method stub
		if(!pwd_txt.getText().equals(again_pwd.getText()))
		{
			
		}
	}
}
