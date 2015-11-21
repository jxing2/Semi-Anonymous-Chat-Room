package Server;

import java.io.*;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

public class Server<ref> extends JFrame {

	private JTextArea j_public;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket sock;
	private ArrayList<Waiter> al;
	private int count; // count how many times people logged.
	private String IP;
	private int port;
	KickDeadUser kick ;
	private Document doc;

	public Server() {
		super("Server");
		al = new ArrayList<Waiter>();
		j_public = new JTextArea();
		j_public.setAutoscrolls(true);
		j_public.setEditable(false);
		
		add(new JScrollPane(j_public));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(580, 380);
		this.setVisible(true);
		readXML();
	}

	private void readXML() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse("./Server_Account.xml");
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList root = doc.getChildNodes();
		// System.out.println(root);
		Node nodes = root.item(0);
		NodeList info = nodes.getChildNodes();
		
		for(int j = 0 ; j < info.getLength(); ++j)
		{
			//System.out.println(info.item(j).getNodeName());
			if(info.item(j).getNodeName().equals("Configuration"))
			{
				NodeList socketInfo = info.item(j).getChildNodes();
				for(int i = 0 ; i < socketInfo.getLength();++i)
				{
					if(socketInfo.item(i).getNodeName().equals("Socket"))
					{
						IP = socketInfo.item(i).getAttributes().item(0).getNodeValue().toString();
						port = Integer.parseInt(socketInfo.item(i).getAttributes().item(1).getNodeValue().toString());
						break;
					}
				}
			}
			// read user info.
//			if(info.item(j).getNodeName().equals("UserList"))
//			{
//				NodeList userInfo = info.item(j).getChildNodes();
//				for(int i = 0 ; i < userInfo.getLength();++i)
//				{
//					if(userInfo.item(i).getNodeName().equals("User"))
//					{
//						//System.out.println("Found Socket");
//					}
//				}
//			}
		}
	}

	public void startRunning() {
		try {
			server = new ServerSocket(port, 999);
			Waiter waiter;
			kick = new KickDeadUser(); 
			kick.start();
			while (true) {
				try {
					waiter = new Waiter(j_public, al, count);
					waiter.serve(server);
					waiter.start();
					al.add(waiter);
					count++;
				} catch (Exception eofe) {
					showMessage("Server Ended Connection");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void showMessage(final String txt) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				j_public.append(txt + "\n");
			}
		});
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.startRunning();
	}
	
	
	private class KickDeadUser extends Thread {
		public void run() {  
			while(true)
			{
				for(int i = 0 ; i < al.size(); i++)
				{
					if(!al.get(i).isAlive())
					{
						System.out.println(al.get(i).nickName+ " is removed");
						al.remove(i);
						i--;
						
					}
				}
				try {
					Thread.sleep(90);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
