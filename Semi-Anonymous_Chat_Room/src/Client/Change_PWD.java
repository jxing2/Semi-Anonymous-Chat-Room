package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

public class Change_PWD extends JFrame {
	private JLabel name;
	private JTextField name_txt;
	private JLabel pwd;
	private JPasswordField pwd_txt;
	private JLabel again_pwd;
	private JPasswordField again_pwd_txt;
	private JLabel new_pwd;
	private JPasswordField new_pwd_txt;
	private JButton confirm;
	private Container pane;
	private int upperMargin = 20;
	private int interval = 40;
	private int leftMargin = 30;
	private int width = 241;
	private String realName;
	Client parent;

	public Change_PWD(Client parent, String realName) {
		this.realName = realName;
		this.parent = parent;
		pane = getContentPane();
		pane.setLayout(null);
		name = new JLabel("Name :");
		name.setBounds(leftMargin, upperMargin, width, 30);
		name_txt = new JTextField();
		name_txt.setBounds(leftMargin, upperMargin += interval, width, 30);
		name_txt.setText(realName);

		pwd = new JLabel("Password :");
		pwd.setBounds(leftMargin, upperMargin += interval, width, 30);
		pwd_txt = new JPasswordField();
		pwd_txt.setBounds(leftMargin, upperMargin += interval, width, 30);

		new_pwd = new JLabel("New password :");
		new_pwd.setBounds(leftMargin, upperMargin += interval, width, 30);
		new_pwd_txt = new JPasswordField();
		new_pwd_txt.setBounds(leftMargin, upperMargin += interval, width, 30);

		again_pwd = new JLabel("Confirm your new password :");
		again_pwd.setBounds(leftMargin, upperMargin += interval, width, 30);
		again_pwd_txt = new JPasswordField();
		again_pwd_txt.setBounds(leftMargin, upperMargin += interval, width, 30);
		confirm = new JButton("Confirm!");
		confirm.setBounds(leftMargin, upperMargin += interval + 42, width, 30);

		pane.add(name);
		pane.add(name_txt);
		pane.add(pwd);
		pane.add(pwd_txt);
		pane.add(new_pwd);
		pane.add(new_pwd_txt);
		pane.add(again_pwd);
		pane.add(again_pwd_txt);
		pane.add(confirm);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
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
		new_pwd_txt.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				confirm();
			}
		});
		again_pwd_txt.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				confirm();
			}
		});
		setSize(300, 500);
		Client.centreWindow(this);
		setResizable(false);
		this.setVisible(true);
	}

	protected void confirm() {
		// TODO Auto-generated method stub
		if (name_txt.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Name cannot be empty!");
			return;
		}
		if (String.valueOf(pwd_txt.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(this, "Password cannot be empty!");
			return;
		}
		if (String.valueOf(new_pwd_txt.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(this,
					"Please type in your new password!");
			return;
		}
		if (String.valueOf(again_pwd_txt.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(this,
					"Please confirm your new password!");
			return;
		}
		if (!String.valueOf(new_pwd_txt.getPassword()).equals(
				String.valueOf(again_pwd_txt.getPassword()))) {
			JOptionPane.showMessageDialog(this, "New password does not match!");
			return;
		}
		if (String.valueOf(pwd_txt.getPassword()).equals(
				String.valueOf(new_pwd_txt.getPassword()))) {
			JOptionPane.showMessageDialog(this, "New password can't be equal to old password!");
			return;
		}
		parent.sendMessage(
				name_txt.getText().trim() + "\n"
						+ String.valueOf(pwd_txt.getPassword()) + "\n"
						+ String.valueOf(new_pwd_txt.getPassword()),
				CommandType.EditProfile);
	}

	public void clear() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pwd_txt.setText("");
				new_pwd_txt.setText("");
				again_pwd_txt.setText("");
			}
		});
	}

	public void close() {
		// TODO Auto-generated method stub
		this.dispose();
	}
}
