package Client;

import java.io.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Server.Message;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client extends JFrame {

	private JTextField j_input;
	private JTextArea j_public;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket sock;
	private String message = "";
	private String IP;// host
	private int port;
	private String realName;
	private Document doc;
	JMenuBar bar;
	Sign_Up sign_up;
	Sign_In login;
	JScrollPane jsp;
	private boolean isLogined = false;
	
	public Client() {
		super("Client");
		readXML();
		j_input = new JTextField();
		ableToType(false);
		j_input.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sendMessage(e.getActionCommand());
				j_input.setText("");
			}
		});

		add(j_input, BorderLayout.SOUTH);
		// ----------------------------------------

		j_public = new JTextArea();
		j_public.setEditable(false);
		add(jsp = new JScrollPane(j_public), BorderLayout.CENTER);
		j_public.setAutoscrolls(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		bar = new JMenuBar();
		JMenuItem signIn = new JMenuItem("Login");
		JMenuItem signUp = new JMenuItem("Sign up");
		JMenuItem changePWD = new JMenuItem("Change password");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem sendFile = new JMenuItem("Send file");
		JMenuItem downloadFile = new JMenuItem("Download file");
		JMenu account = new JMenu("Account");
		JMenu file = new JMenu("File");

		bar.add(account);
		bar.add(file);
		account.add(signIn);
		account.add(signUp);
		account.add(changePWD);
		account.add(exit);
		file.add(sendFile);
		file.add(downloadFile);
		signIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if(!isLogined)
					signIn();
				else
					JOptionPane.showMessageDialog(null,"You have already signed in.");
			}
		});
		signUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				signUp();
			}
		});
		changePWD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				changePWD();
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.exit(0);
			}
		});
		sendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				sendFile();
			}
		});
		downloadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				downloadFile();
			}
		});
		setJMenuBar(bar);
		setSize(580, 380);
		centreWindow(this);
	}

	protected void changePWD() {
		// TODO Auto-generated method stub
		
	}

	protected void sendFile() {
		// TODO Auto-generated method stub
		// pop out a window to choose a file
		ChooserWindow chooserwindow = new ChooserWindow();
	}

	private void downloadFile() {
		// TODO Auto-generated method stub

	}

	private void signIn() {
		// TODO Auto-generated method stub
		login = new Sign_In(this);
	}

	private void signUp() {
		// TODO Auto-generated method stub
		sign_up = new Sign_Up(this);
	}

	private void readXML() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse("./Client_Configuration.xml");
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

		for (int j = 0; j < info.getLength(); ++j) {
			// System.out.println(info.item(j).getNodeName());
			if (info.item(j).getNodeName().equals("Configuration")) {
				NodeList socketInfo = info.item(j).getChildNodes();
				for (int i = 0; i < socketInfo.getLength(); ++i) {
					if (socketInfo.item(i).getNodeName().equals("Socket")) {
						IP = socketInfo.item(i).getAttributes().item(0)
								.getNodeValue().toString();
						port = Integer.parseInt(socketInfo.item(i)
								.getAttributes().item(1).getNodeValue()
								.toString());
						break;
					}
				}
			}
		}
	}

	public void startRunning() {
		try {
			connect();
			setupStreams();
			this.setVisible(true);
			whileChatting();
		} catch (EOFException eofe) {
			showMessage("connection terminated!");
		} catch (IOException ie) {
			ie.printStackTrace();
			showMessage("Server denied connection.");
		} finally {
			close();
		}
	}

	private void close() {
		// TODO Auto-generated method stub
		showMessage("Closing connection");
		ableToType(false);
		try {
			if (output != null)
				output.close();
			if (input != null)
				input.close();
			if (sock != null)
				sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private void whileChatting() throws IOException {
		// TODO Auto-generated method stub
		String message = "";
		//ableToType(true);
		do {
			try {
				j_public.setCaretPosition(j_public.getDocument().getLength());
				message = String.valueOf(input.readObject());
				System.out.println(message);
				Message m = handleMessage(message);
				switch (m.type) {
				case 0:
					showMessage(m.message);
					break;
				case 1:
					JOptionPane.showMessageDialog(sign_up, m.message);
					if(sign_up!=null)
						sign_up.clear();
					break;
				case 2:
					JOptionPane.showMessageDialog(sign_up, m.message);
					if(sign_up!=null)
						sign_up.close();
					break;
				case 3:
					JOptionPane.showMessageDialog(login, m.message);
					if(login!=null)
						login.clear();
					break;
				case 4:
					//JOptionPane.showMessageDialog(login, m.message);
					if(login!=null)
						login.close();
					ableToType(true);
					isLogined = true;
					break;
				default:
					return;
				}
			} catch (ClassNotFoundException cnfe) {
				showMessage("I dont know what user send.");
			}
		} while (true);
	}

	private Message handleMessage(String message) {
		// TODO Auto-generated method stub
		int type;
		if (message.substring(0, 1).equals("@")) {
			type = Integer.parseInt(message.substring(1, 3));
			if (type < 0)
				return new Message("", -1);// error
			return new Message(message.substring(3, message.length()), type);// right
		} else
			return new Message("", -1);// error
	}

	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		sendMessage(message, CommandType.PlainText);
	}

	public void sendMessage(String message, CommandType type) {
		String tmp = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			switch (type) {
			case PlainText:
				tmp = "@00" + message;
				showMessage(realName + " - " + dateFormat.format(cal.getTime())
						+ "\n" + message);
				break;
			case Register:
				tmp = "@01" + message;
				break;
			case Login:
				tmp = "@02" + message;
				break;
			case EditProfile:
				tmp = "@03" + message;
				break;
			default:
				return;// do nothing
			}
			System.out.println(tmp);
			output.writeObject(tmp);
			output.flush();
		} catch (IOException ie) {
			j_public.append("Something WRONG");
		}
	}

	private void ableToType(boolean b) {
		// TODO Auto-generated method stub
		j_input.setEditable(b);
	}

	private void setupStreams() throws IOException {
		// TODO Auto-generated method stub
		output = new ObjectOutputStream(sock.getOutputStream());
		output.flush();
		input = new ObjectInputStream(sock.getInputStream());
	}

	private void connect() throws UnknownHostException, ConnectException {
		// TODO Auto-generated method stub
		try {
			sock = new Socket(IP, 6789);
		} catch (IOException ie) {

		}
	}

	public static void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.startRunning();
	}
}
