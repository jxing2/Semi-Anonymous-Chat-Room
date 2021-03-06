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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server<ref> extends JFrame {

	private JTextArea j_public;
	private ServerSocket server;
	private ServerSocket fileServer;
	private ArrayList<Waiter> al;
	private ArrayList<FileReceiver> al_file;
	private int count; // count how many times people logged.
	private int port;
	private int filePort;
	KickDeadUser kick;
	private Document doc;
	private Document configDoc;

	private String logDir;// log directory
	private String shareFileDir;// restore the directory of shared file.
	private boolean allowRegister = true;
	File logFile;
	FileWriter log;
	File opLogFile;
	File SharedDirectory;
	FileWriter opLog;
	DateFormat dateFormat;
	DateFormat logdateFormat;
	Calendar cal;
	public static Lock opLogLock = new ReentrantLock();
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
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		logdateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		cal = Calendar.getInstance();
		String tmpDir1 = logDir + dateFormat.format(cal.getTime())
				+ "_Chat.log";
		String tmpDir2 = logDir + dateFormat.format(cal.getTime())
				+ "_Operation.log";
		logFile = new File(tmpDir1);
		opLogFile = new File(tmpDir2);
		SharedDirectory = new File(shareFileDir);
		if (!logFile.getParentFile().exists()) {
			// If directory does not exist --> create it.
			logFile.getParentFile().mkdirs();
		}

		if (!SharedDirectory.exists() && !SharedDirectory.isDirectory()) {
			
			SharedDirectory.mkdirs();
		}

		try {
			log = new FileWriter(logFile, true);
			opLog = new FileWriter(opLogFile, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			showMessage("log file cannot read or write!");
			e1.printStackTrace();
		}
		try {
			fileServer = new ServerSocket(filePort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void readXML() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse("./Server_Account.xml");
			DocumentBuilderFactory factory1 = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder1 = factory1.newDocumentBuilder();
			configDoc = builder1.parse("./Server_Config.xml");
		} catch (ParserConfigurationException e) {
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Missing XML files!");
			System.exit(-1);
		}
		NodeList socketList = configDoc.getElementsByTagName("Socket");
		for (int i = 0; i < socketList.getLength(); ++i) {
			port = Integer.parseInt(socketList.item(i).getAttributes()
					.getNamedItem("Port").getNodeValue().toString());
			filePort = port-1;
			break;
		}
		NodeList logDirList = configDoc.getElementsByTagName("LogDir");
		for (int i = 0; i < logDirList.getLength(); ++i) {
			logDir = logDirList.item(i).getAttributes().getNamedItem("Value")
					.getNodeValue().toString();
			break;
		}
		NodeList shareDirList = configDoc.getElementsByTagName("ShareFileDir");
		for (int i = 0; i < shareDirList.getLength(); ++i) {
			shareFileDir = shareDirList.item(i).getAttributes()
					.getNamedItem("Value").getNodeValue().toString();
			break;
		}
		NodeList registerDirList = configDoc.getElementsByTagName("Register");
		for (int i = 0; i < registerDirList.getLength(); ++i) {
			String tmp = registerDirList.item(i).getAttributes()
					.getNamedItem("Value").getNodeValue().toString();
			allowRegister = tmp.equals("0")? false:true;
			break;
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
					waiter = new Waiter(j_public, al, count, doc, log, opLog, SharedDirectory,fileServer,logDir,allowRegister);
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
			while (true) {
				for (int i = 0; i < al.size(); i++) {
					if (!al.get(i).isAlive()) {
						opLog(((al.get(i).realName == null) ? al.get(i).nickName
								: al.get(i).realName)
								+ " is Logging out. --"
								+ logdateFormat.format(cal.getTime()));
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
	
	private void opLog(String op) {
		opLogLock.lock();
		try {
			opLog.write(op + "\r\n");
			opLog.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			opLogLock.unlock();
		}
	}
	

}
