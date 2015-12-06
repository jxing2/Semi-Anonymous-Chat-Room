package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import Client.CommandType;

public class Waiter extends Thread {
	public ObjectOutputStream output;
	public ObjectInputStream input;
	public ServerSocket server;
	public ServerSocket fileServer;
	public Socket sock;
	private int filePort;
	private JTextArea j_public;
	private ArrayList<Waiter> al;
	private ArrayList<FileReceiver> al_send;
	public String nickName;
	public String realName;
	private String sharedFileDir;
	private boolean isLogined = false;
	String ClientIP;


	public enum UserType {
		Teacher, Student
	}

	private UserType type;
	public boolean flag;
	private Document doc;
	FileWriter chatLog;
	FileWriter opLog;
	IsLogined testLogined;
	private static Lock chatLogLock = new ReentrantLock();
	private static Lock writeAccountLock = new ReentrantLock();
	File sharedDir;
	File logDir;
	final private Lock connectionLock = new ReentrantLock();
	public Waiter(JTextArea j_public, ArrayList<Waiter> al, int count,
			Document doc, FileWriter chatLog, FileWriter opLog, File sharedDir,
			ServerSocket fileServer, String LogDir) {
		this.j_public = j_public;
		this.al = al;
		nickName = "User" + count;
		this.doc = doc;
		this.chatLog = chatLog;
		this.opLog = opLog;
		this.sharedDir = sharedDir;
		al_send = new ArrayList<FileReceiver>();
		this.fileServer = fileServer;
		this.logDir = new File(LogDir);
	}

	private void waitForConnection(ServerSocket server) {
		// TODO Auto-generated method stub
		try {
			sock = server.accept();
			ClientIP = sock.getRemoteSocketAddress().toString();
			ClientIP = ClientIP.substring(1, ClientIP.indexOf(":"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setupStreams() throws IOException {
		// TODO Auto-generated method stub
		output = new ObjectOutputStream(sock.getOutputStream());
		output.flush();
		input = new ObjectInputStream(sock.getInputStream());
	}

	public void serve(ServerSocket server) throws IOException {
		// TODO Auto-generated method stub
		waitForConnection(server);
		setupStreams();
	}

	// only used to send message to other users.
	private void SendToOthers(String message, String nickName, String realName,
			boolean isTrim, UserType type) {
		// TODO Auto-generated method stub
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			if (type == UserType.Teacher) {
				output.writeObject("@07"
						+ realName
						+ " - "
						+ dateFormat.format(cal.getTime())
						+ "\n"
						+ (isTrim ? message.substring(3, message.length())
								: message));
			} else {
				output.writeObject("@00"
						+ nickName
						+ " - "
						+ dateFormat.format(cal.getTime())
						+ "\n"
						+ (isTrim ? message.substring(3, message.length())
								: message));
			}
			output.flush();
		} catch (IOException ie) {
			// j_public.append("Something WRONG");
			flag = false;
			ie.getStackTrace();
		}
	}

	private void SendToOthers(String message, boolean isTrim) {
		// TODO Auto-generated method stub
		Waiter waiter;
		for (int i = 0; i < al.size(); ++i) {
			waiter = al.get(i);
			if (!this.equals(waiter)) {
				waiter.SendToOthers(message, nickName, realName, isTrim, type);
			}
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

	private void sendNotification(String message) {
		try {
			output.writeObject("@09" + message);
			output.flush();
		} catch (IOException ie) {
			flag = false;
			ie.getStackTrace();
		}
	}

	private void SendNotificationToOthers(String message) {
		// TODO Auto-generated method stub
		Waiter waiter;
		for (int i = 0; i < al.size(); ++i) {
			waiter = al.get(i);
			if (!this.equals(waiter)) {
				waiter.sendNotification(message);
			}
		}
	}

	public void sendMessage(String message, ServerCommand cmd) {
		// TODO Auto-generated method stub
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			switch (cmd) {
			case PlainText:
				output.writeObject("@00" + nickName + " - "
						+ dateFormat.format(cal.getTime()) + "\n" + message);
				break;
			case Register_Alert:
				output.writeObject("@01" + message);
				// showMessage(nickName + " - " +
				// dateFormat.format(cal.getTime())
				// + "\n" + message);
				break;
			case Register_Success:
				output.writeObject("@02" + message);
				showMessage(nickName + " - " + dateFormat.format(cal.getTime())
						+ "\n" + message);
				break;
			case Login_Alert:
				output.writeObject("@03" + message);
				// showMessage(nickName + " - " +
				// dateFormat.format(cal.getTime())
				// + "\n" + message);
				break;
			case Login_Success:
				if (type == UserType.Teacher)
					output.writeObject("@08" + message);
				else
					output.writeObject("@04" + message);
				showMessage(nickName + " - " + dateFormat.format(cal.getTime())
						+ "\n" + message);
				isLogined = true;
				break;
			case EditProfile_Alert:
				output.writeObject("@05" + message);
				break;
			case EditProfile_Success:
				output.writeObject("@06" + message);
				showMessage(nickName + " - " + dateFormat.format(cal.getTime())
						+ "\n" + message);
				break;
			case SendRequestReply_Success:
				output.writeObject("@10" + message);
				break;
			case SendRequestReply_Alert:
				output.writeObject("@11" + message);
				break;
			case DownloadRequestReply_Success:
				output.writeObject("@12" + message);
				break;
			case FileListRequestReply:
				output.writeObject("@13" + message);
				break;
			case LogListRequestAlert:
				output.writeObject("@14" + message);
				break;
			case LogListRequestSuccess:
				output.writeObject("@15" + message);
				break;
			default:
				return;
			}
			System.out.println(message);
			output.flush();
		} catch (IOException ie) {
			flag = false;
			ie.getStackTrace();
		}
	}

	public void close() {
		// TODO Auto-generated method stub
		showMessage("Closing connection");
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

	public void run() {
		flag = true;
		String message = "";
		// String message = nickName + " connected";
		// sendNotification(message);
		// SendToOthers(message, false);
		do {
			try {
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd hh:mm:ss");
				Calendar cal = Calendar.getInstance();
				message = String.valueOf(input.readObject());
				for (int i = 0; i < al_send.size(); i++) {
					System.out.println(al_send.get(i));
				}
				// System.out.println(message);
				Message m = handleMessage(message);
				switch (m.type) {
				case 0:// PlainText
					showMessage(nickName + " - "
							+ dateFormat.format(cal.getTime()) + "\n"
							+ m.message);
					chatLog(realName + " - " + dateFormat.format(cal.getTime())
							+ "\r\n" + m.message);
					SendToOthers(message, true);
					break;
				case 1:
					if (register(m.message)) {
						opLog(GetUserInfo(m.message).name + " - "
								+ dateFormat.format(cal.getTime()) + "--"
								+ "Registered. IP:" + ClientIP);
					} else {
						opLog(GetUserInfo(m.message).name + " - "
								+ dateFormat.format(cal.getTime()) + "--"
								+ "Register failed. IP:" + ClientIP);
					}
					break;
				case 2:
					if (!isLogined)
						if (login(m.message)) {
							String noticeToSelf = realName + " connected";
							String noticeToOther = nickName + " connected";
							sendNotification(noticeToSelf);
							SendNotificationToOthers(noticeToOther);
							opLog(realName + " - "
									+ dateFormat.format(cal.getTime()) + "--"
									+ "Logined. IP:" + ClientIP);
						} else {
							opLog(GetUserInfo(m.message).name + " - "
									+ dateFormat.format(cal.getTime()) + "--"
									+ "Login failed. IP:" + ClientIP);
						}
					break;
				case 3:
					if (editProfile(m.message)) {
						opLog(GetUserInfo(m.message).name + " - "
								+ dateFormat.format(cal.getTime()) + "--"
								+ "Changed password. IP:" + ClientIP);
					} else {
						opLog(GetUserInfo(m.message).name + " - "
								+ dateFormat.format(cal.getTime()) + "--"
								+ "Change password failed. IP:" + ClientIP);
					}
					break;
				case 4:
					validate(m.message);
					break;
				case 5:// download file request
					locateFile(m.message);
					// sendMessage(sharedFileDir,ServerCommand.DownloadRequestReply_Success);
					break;
				case 6:// File list request
					sharedFileDir = sharedDir.getName();
					File[] fileList = sharedDir.listFiles();
					String tmp = "";
					for (int i = 0; i < fileList.length; i++) {
						if (fileList[i].isFile())
							tmp += fileList[i].getName();
						else
							continue;
						if (i < fileList.length - 1)
							tmp += "\n";
					}
					sendMessage(tmp, ServerCommand.FileListRequestReply);
					break;
				case 7:
					if (this.type == UserType.Teacher) {
						File[] logFiles = logDir.listFiles();
						String mes = "";
						for (int i = 0; i < logFiles.length; ++i) {
							if (logFiles[i].isFile())
								mes += logFiles[i].getName();
							else
								continue;
							if (i < logFiles.length - 1)
								mes += "\n";
						}
						sendMessage(mes, ServerCommand.LogListRequestSuccess);
					} else {
						sendMessage("Students are not allowed to download Log",
								ServerCommand.LogListRequestAlert);
					}
					break;
				}
				// System.out.println(message);
			} catch (ClassNotFoundException cnfe) {
				flag = false;
				showMessage("I dont know what user send");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// close();
				flag = false;
			}
		} while (flag);
	}

	private void locateFile(String message) {
		// TODO Auto-generated method stub
		String[] tmp = message.split("\n");
		String downloadType = tmp[0];
		String fileName = tmp[1];
		String savePath = tmp[2];
		File[] filelist;
		if (downloadType.equals("Share")) {
			filelist = sharedDir.listFiles();

			for (int i = 0; i < filelist.length; i++) {

				if (fileName.equals(filelist[i].getName())) {
					File fileToSend = new File(filelist[i].getPath());
					long size = fileToSend.length();
					FileSender fs = new FileSender(filelist[i].getPath(), size,
							filePort, fileServer, connectionLock, this);
					fs.setMessage(savePath + "\n" + size, ServerCommand.DownloadRequestReply_Success);
					fs.start();
					break;
				}
			}
		}
		else // download log file
		{
			filelist = logDir.listFiles();
			for (int i = 0; i < filelist.length; i++) {

				if (fileName.equals(filelist[i].getName())) {
					File fileToSend = new File(filelist[i].getPath());
					long size = fileToSend.length();
					FileSender fs = new FileSender(filelist[i].getPath(), size,
							filePort, fileServer, connectionLock, this);
					fs.setMessage(savePath + "\n" + size, ServerCommand.DownloadRequestReply_Success);
					fs.start();
					break;
				}
			}
		}
	}

	private void validate(String message) {
		String[] tmp = message.split("\n");
		String fileName = tmp[0];
		long size = Integer.parseInt(tmp[1]);
		String filePath = tmp[2];
		FileReceiver fr = new FileReceiver(fileName, sharedDir, size,
				fileServer , connectionLock, this);
		if (fr.test()) {
			fr.setMessage(filePath + "\n" + size, ServerCommand.SendRequestReply_Success);
			fr.start();
		} else {
			sendMessage("Not ready!", ServerCommand.SendRequestReply_Alert);
			return;
		}
		al_send.add(fr);
	}

	private boolean editProfile(String message) {
		// TODO Auto-generated method stub
		User user = GetUserInfo(message);
		// test if ID exist
		NodeList allUser = doc.getElementsByTagName("User");
		for (int i = 0; i < allUser.getLength(); ++i) {
			if (allUser.item(i).getAttributes().getNamedItem("ID")
					.getNodeValue().equals(user.name)
					&& allUser.item(i).getAttributes().getNamedItem("PWD")
							.getNodeValue().equals(user.password)) {
				allUser.item(i).getAttributes().getNamedItem("PWD")
						.setNodeValue(user.new_password);
				boolean result = doc2XmlFile(doc, "./Server_Account.xml");
				if (result) {
					sendMessage("New password set!",
							ServerCommand.EditProfile_Success);
					return true;
				} else {
					sendMessage("Failed: Server data file is wrong!",
							ServerCommand.EditProfile_Alert);
					return false;
				}
			}
		}
		sendMessage("Failed: Name and password does not match!",
				ServerCommand.EditProfile_Alert);
		return false;
	}

	private boolean login(String message) {
		// TODO Auto-generated method stub
		User user = GetUserInfo(message);

		testLogined = new IsLogined(al, user.name);
		if (testLogined.run()) {
			sendMessage("Failed: This account already signed in!",
					ServerCommand.Login_Alert);
			return false;
		}

		NodeList allUser = doc.getElementsByTagName("User");

		for (int i = 0; i < allUser.getLength(); ++i) {
			if (allUser.item(i).getAttributes().getNamedItem("ID")
					.getNodeValue().equals(user.name)
					&& allUser.item(i).getAttributes().getNamedItem("PWD")
							.getNodeValue().equals(user.password)) {
				realName = user.name;
				type = (allUser.item(i).getAttributes().getNamedItem("Type")
						.getNodeValue().equals("1")) ? UserType.Student
						: UserType.Teacher;
				System.out.println(type);
				sendMessage("Success!", ServerCommand.Login_Success);
				return true;
			}
		}
		sendMessage("Failed: Name and password does not match!",
				ServerCommand.Login_Alert);
		return false;
	}

	private boolean register(String message) {
		// TODO Auto-generated method stub
		User user = GetUserInfo(message);
		Element e = doc.createElement("User");
		e.setAttribute("ID", user.name);
		e.setAttribute("PWD", user.password);
		e.setAttribute("Type", "1");

		NodeList root = doc.getChildNodes();
		// System.out.println(root);
		Node nodes = root.item(0);
		NodeList info = nodes.getChildNodes();

		// test if ID exist
		NodeList allUser = doc.getElementsByTagName("User");
		for (int i = 0; i < allUser.getLength(); ++i) {
			if (allUser.item(i).getAttributes().getNamedItem("ID")
					.getNodeValue().equals(user.name)) {
				sendMessage("Failed: Name already exist!",
						ServerCommand.Register_Alert);
				return false;
			}
		}
		for (int j = 0; j < info.getLength(); ++j) {
			// read user info.
			if (info.item(j).getNodeName().equals("UserList")) {
				info.item(j).appendChild(e);
				break;
			}
		}
		boolean result = doc2XmlFile(doc, "./Server_Account.xml");
		if (result) {
			sendMessage("Success!", ServerCommand.Register_Success);
			return true;
		} else {
			sendMessage("Failed: Server data file is wrong!",
					ServerCommand.Register_Alert);
			return false;
		}
	}

	public boolean doc2XmlFile(Document document, String filename) {
		boolean flag = true;
		writeAccountLock.lock();
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
			// reload doc
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.doc = builder.parse("./Server_Account.xml");
		} catch (Exception ex) {
			flag = false;
			ex.printStackTrace();
		} finally {
			writeAccountLock.unlock();
		}
		return flag;
	}

	private User GetUserInfo(String message) {
		// TODO Auto-generated method stub
		String[] info = message.split("\n");
		if (info.length == 2)
			return new User(info[0], info[1], 0);
		else
			return new User(info[0], info[1], 0, info[2]);
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

	private void chatLog(String chat) {
		chatLogLock.lock();
		try {
			chatLog.write(chat + "\r\n");
			chatLog.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			chatLogLock.unlock();
		}
	}

	private void opLog(String op) {
		Server.opLogLock.lock();
		try {
			opLog.write(op + "\r\n");
			opLog.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Server.opLogLock.unlock();
		}
	}

	public String getClientIP() {
		return ClientIP;
	}
}
