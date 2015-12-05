package Client;

import java.io.*;

import Server.Waiter.UserType;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Client extends JFrame {

	private JTextField j_input;
	private JTextPane j_public;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket sock;
	//private String message = "";
	private String IP;// host
	private int port;
	private int filePort;
	private String realName;
	private Document doc;
	private UserType type;
	private ArrayList<Send> al_send; 
	private ArrayList<Download> al_download;
	//private ArrayList<Download>
	JMenuBar bar;
	Sign_Up sign_up;
	Sign_In login;
	StatusWindow statusWindow;
	Change_PWD chang_PWD;
	ChooserWindow chooserWindow;
	DownloadWindow downloadWindow;
	JScrollPane jsp;
	JMenuItem signIn;
	
	private boolean isLogined = false;
	Font inputFont ;
	private int fontSize = 19;
	public Client() {
		super("Client");
		readXML();
		al_send = new ArrayList<Send>();
		j_input = new JTextField();
		ableToType(false);
		j_input.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sendMessage(e.getActionCommand());
				j_input.setText("");
			}
		});
		inputFont = new Font("", Font.LAYOUT_LEFT_TO_RIGHT, fontSize);
		j_input.setFont(inputFont);
		add(j_input, BorderLayout.SOUTH);
		// ----------------------------------------

		j_public = new JTextPane();
		j_public.setEditable(false);
		add(jsp = new JScrollPane(j_public), BorderLayout.CENTER);
		j_public.setAutoscrolls(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		bar = new JMenuBar();
		signIn = new JMenuItem("Login");
		JMenuItem signUp = new JMenuItem("Sign up");
		JMenuItem changePWD = new JMenuItem("Change password");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem sendFile = new JMenuItem("Send file");
		JMenuItem downloadFile = new JMenuItem("Download file");
		JMenuItem status = new JMenuItem("Status");
		JMenu account = new JMenu("Account");
		JMenu file = new JMenu("File");
		signIn.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		exit.setAccelerator(KeyStroke.getKeyStroke('E', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		sendFile.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		downloadFile.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		bar.add(account);
		bar.add(file);
		account.add(signIn);
		account.add(signUp);
		account.add(changePWD);
		account.add(exit);
		file.add(sendFile);
		file.add(downloadFile);
		file.add(status);
		signIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (!isLogined)
					signIn();
				else
					JOptionPane.showMessageDialog(null,
							"You have already signed in.");
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
		status.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				status();
			}
		});
		setJMenuBar(bar);
		setSize(900, 700);
		centreWindow(this);
	}

	protected void status() {
		// TODO Auto-generated method stub
		statusWindow = new StatusWindow(al_send,al_download);
	}

	protected void changePWD() {
		// TODO Auto-generated method stub
		chang_PWD = new Change_PWD(this, realName);
	}

	protected void sendFile() {
		// TODO Auto-generated method stub
		// pop out a window to choose a file
		chooserWindow = new ChooserWindow(this);
	}

	private void downloadFile() {
		// TODO Auto-generated method stub
		downloadWindow = new DownloadWindow(this);
		sendMessage("request downloading",CommandType.FileListRequest);
		
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
						filePort = port -1;
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
	private void showMessage(final String txt, final UserType type) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showMessage(txt, Color.black, false, fontSize , type);
			}
		});
	}
	private void showMessage(final String txt) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showMessage(txt, Color.black, false, fontSize , type);
			}
		});
	}
	private void showMessage(final String txt, final Color color, final boolean bold, final int fontSize, final UserType type) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(type == UserType.Teacher)
					setDocs(txt, Color.red, bold, fontSize);
				else
					setDocs(txt, color, bold, fontSize);
			}
		});
	}
	private void showNotification(final String txt) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setDocs(txt, Color.gray, false, fontSize);
			}
		});
	}
	private void whileChatting() throws IOException {
		// TODO Auto-generated method stub
		String message = "";
		// ableToType(true);
		do {
			try {
				j_public.setCaretPosition(j_public.getDocument().getLength());
				message = String.valueOf(input.readObject());
				System.out.println(message);
				Message m = handleMessage(message);
				switch (m.type) {
				case 0:
					showMessage(m.message, UserType.Student);
					break;
				case 1:
					JOptionPane.showMessageDialog(sign_up, m.message);
					if (sign_up != null)
						sign_up.clear();
					break;
				case 2:
					JOptionPane.showMessageDialog(sign_up, m.message);
					if (sign_up != null)
						sign_up.close();
					type = Server.Waiter.UserType.Student;
					break;
				case 3:
					JOptionPane.showMessageDialog(login, m.message);
					if (login != null)
						login.clear();
					break;
				case 4:
					// JOptionPane.showMessageDialog(login, m.message);
					if (login != null)
						login.close();
					ableToType(true);
					signIn.setEnabled(false);
					isLogined = true;
					realName = login.getRealName();
					type = UserType.Student;
					break;
				case 5:
					JOptionPane.showMessageDialog(chang_PWD, m.message);
					if (chang_PWD != null)
						chang_PWD.clear();
					break;
				case 6:
					JOptionPane.showMessageDialog(chang_PWD, m.message);
					if (chang_PWD != null)
						chang_PWD.close();
					break;
				case 7: // Professor's message
					showMessage(m.message,Color.red, false, fontSize, type);
					break;
				case 8:
					if (login != null)
						login.close();
					ableToType(true);
					signIn.setEnabled(false);
					isLogined = true;
					realName = login.getRealName();
					type = UserType.Teacher;
					break;
				case 9:
					showNotification(m.message);
					break;
				case 10://SendRequestReply_Success
					
					SendFile(m.message, filePort, IP);
					break;
				case 11:
					break;
				case 12://download request reply success
					downloadFile(filePort, IP);
					break;
				case 13:
					handleList(m.message);
					break;
				default:
					return;
				}
			} catch (ClassNotFoundException cnfe) {
				showMessage("I dont know what user send.");
			}
		} while (true);
	}

	private void handleList(String message) {
		// TODO Auto-generated method stub
		String[] fileList = message.split("\n");
		for(int i = 0 ; i < fileList.length; ++i)
			System.out.println(fileList[i]);
		if(downloadWindow!=null)
		{
			downloadWindow.reloadFileList(fileList);
		}
	}

	private void downloadFile(int filePort, String IP) {
		// TODO Auto-generated method stub
		Download download = new Download(downloadWindow.getSavePath());
		download.start();
	}

	private void SendFile(String message, int filePort, String IP) {
		// TODO Auto-generated method stub
		String[] tmp = message.split("n");
		String filePath = tmp[0];
		long size = Integer.parseInt(tmp[1]);
		Send send = new Send(filePath, filePort, IP, size);
		send.start();
		al_send.add(send);
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
			case SendFileRequest:
				tmp = "@04"+ message;
				break;
			case DownloadFileRequest:
				tmp = "@05" + message;
				break;
			case FileListRequest:
				tmp = "@06" + message;
				break;
			default:
				return;// do nothing
			}
			output.writeObject(tmp);
			System.out.println(tmp);
			output.flush();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	private void ableToType(boolean b) {
		// TODO Auto-generated method stub
		j_input.setEditable(b);
	}

	private void setupStreams()  {
		// TODO Auto-generated method stub
		try{
		output = new ObjectOutputStream(sock.getOutputStream());
		output.flush();
		input = new ObjectInputStream(sock.getInputStream());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this, "Cannot connect to server!");
			System.exit(0);
		}
	}

	private void connect() {
		// TODO Auto-generated method stub
		try {
			sock = new Socket(IP, port);
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(this, "Cannot connect to server!");
			System.exit(0);
		}
	}

	public static void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	public void insert(String str, AttributeSet attrSet) {
		javax.swing.text.Document doc = j_public.getDocument();
		str = str +"\n" ;
		try {
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (BadLocationException e) {
			System.out.println("BadLocationException: " + e);
		}
	}

	public void setDocs(String str, Color col, boolean bold, int fontSize) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, col);
		// color
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}
		// font size
		StyleConstants.setFontSize(attrSet, fontSize);
		
		insert(str, attrSet);
	}
	public static void main(String[] args) {
		Client client = new Client();
		client.startRunning();
	}
}
