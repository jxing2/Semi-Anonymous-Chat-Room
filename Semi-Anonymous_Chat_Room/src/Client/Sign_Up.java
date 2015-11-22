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
	private JPasswordField pwd_txt;
	private JLabel again_pwd ;
	private JPasswordField again_pwd_txt;
	private JButton confirm;
	private Container pane;
	private int upperMargin=20;
	private int interval = 40;
	private int leftMargin=30;
	private int width = 241;
	Client parent;
	public Sign_Up(Client parent)
	{
		this.parent = parent;
		pane = getContentPane();
		pane.setLayout(null);
		name = new JLabel("Name :");
		name.setBounds(leftMargin,upperMargin , width, 30);
		name_txt = new JTextField();
		name_txt.setBounds(leftMargin, upperMargin+=interval, width, 30);
		pwd = new JLabel("Password :");
		pwd.setBounds(leftMargin,upperMargin+=interval,  width, 30);
		pwd_txt = new JPasswordField();
		pwd_txt.setBounds(leftMargin,upperMargin+=interval,  width, 30);
		again_pwd = new JLabel("Confirm your password :");
		again_pwd.setBounds( leftMargin,upperMargin+=interval, width, 30);
		again_pwd_txt = new JPasswordField();
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
		
		name_txt.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				confirm();
			}
		});
		pwd_txt.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				confirm();
			}
		});
		again_pwd_txt.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
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
		if(name_txt.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this, "Name cannot be empty!");
			return;
		}
		if(String.valueOf(pwd_txt.getPassword()).equals(""))
		{
			JOptionPane.showMessageDialog(this, "Password cannot be empty!");
			return;
		}
		if(String.valueOf(again_pwd_txt.getPassword()).equals(""))
		{
			JOptionPane.showMessageDialog(this, "Please confirm your password!");
			return;
		}
		if(!String.valueOf(pwd_txt.getPassword()).equals(String.valueOf(again_pwd_txt.getPassword())))
		{
			JOptionPane.showMessageDialog(this, "Password does not match!");
			return;
		}
		parent.sendMessage(name_txt.getText().trim()+"\n"+String.valueOf(pwd_txt.getPassword()),CommandType.Register);
	}
	public void clear() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				name_txt.setText("");
				pwd_txt.setText("");
				again_pwd_txt.setText("");
			}
		});
	}
	public void close() {
		// TODO Auto-generated method stub
		this.dispose();
	}
}
