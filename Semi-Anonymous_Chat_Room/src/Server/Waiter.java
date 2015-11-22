package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

public class Waiter extends Thread {
	public ObjectOutputStream output;
	public ObjectInputStream input;
	public ServerSocket server;
	public Socket sock;
	private JTextArea j_public;
	private ArrayList<Waiter> al;
	public String nickName;
	public String realName;
	private enum Type{Teacher, Student}
	private Type type;
	public boolean flag;
	private Document doc;
	public Waiter(JTextArea j_public,ArrayList<Waiter> al, int count, Document doc)
	{
		this.j_public = j_public;
		this.al = al;
		nickName = "User"+count;
		this.doc = doc;
	}
	private void waitForConnection(ServerSocket server) {
		// TODO Auto-generated method stub
		try {
			sock = server.accept();
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
	
	private void sendMessage(String message,String nickName) {
		// TODO Auto-generated method stub
		try{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			output.writeObject(nickName + " - "+ dateFormat.format(cal.getTime())+"\n" + message );
			output.flush();
		}
		catch(IOException ie)
		{
			//j_public.append("Something WRONG");
			flag = false;
			ie.getStackTrace();
		}
	}
		
	private void sendMessage(String message) {
		// TODO Auto-generated method stub
		try{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			output.writeObject(nickName + " - "+ dateFormat.format(cal.getTime())+"\n" + message );
			output.flush();
			showMessage(nickName + " - "+ dateFormat.format(cal.getTime())+"\n" + message );
		}
		catch(IOException ie)
		{
			//j_public.append("Something WRONG");
			flag = false;
			ie.getStackTrace();
		}
	}
	private void SendToOthers(String message) {
		// TODO Auto-generated method stub
		Waiter waiter;
		for(int i = 0 ; i < al.size(); ++i)
		{
			waiter = al.get(i);
			if(!this.equals(waiter))
			{
				waiter.sendMessage(message,nickName);
			}
		}
	}
	private void showMessage(final String txt) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						j_public.append(txt+"\n");
					}
				}
				);
	}
	public void close() {
		// TODO Auto-generated method stub
		showMessage("Closing connection");
		try {
			if(output!=null)
				output.close();
			if(input!=null)
				input.close();
			if(sock!=null)
				sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {  
		flag = true;
		String message = nickName + " connected";
		sendMessage(message);
		SendToOthers(message);
		do{
			try{
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
				Calendar cal = Calendar.getInstance();
				message = String.valueOf(input.readObject());
				Message m = handleMessage(message);
				System.out.println("hello4"+m.type);
				switch(m.type)
				{
				case 0:// PlainText
					showMessage(nickName + " - "+ dateFormat.format(cal.getTime())+"\n" + m.message );
					break;
				case 1:
					System.out.println("hello");
					register(m.message);
					showMessage(nickName + " - "+ dateFormat.format(cal.getTime())+"\n" + "Register");
					break;
				case 2:
					break;
				case 3:
					break;
				}
				
				
				//System.out.println(message);
				SendToOthers(message);
			}
			catch(ClassNotFoundException cnfe)
			{
				flag = false;
				showMessage("I dont know what user send");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//close();
				flag = false;
			}
		}while(flag);
	}
	private void register(String message) {
		// TODO Auto-generated method stub
		User user = GetUserInfo(message);
		Element e = doc.createElement("User");
		e.setAttribute("ID",user.name);
		e.setAttribute("PWD",user.password);
		e.setAttribute("Type","1");
		
		NodeList root = doc.getChildNodes();
		// System.out.println(root);
		Node nodes = root.item(0);
		NodeList info = nodes.getChildNodes();
		System.out.println("Name: " + user.name+ " PWD:"+ user.password);
		for(int j = 0 ; j < info.getLength(); ++j)
		{
			// read user info.
			if(info.item(j).getNodeName().equals("UserList"))
			{
				info.item(j).appendChild(e);
				break;
			}
		}
		doc2XmlFile(doc,"./Server_Account.xml");
	}
    public boolean doc2XmlFile(Document document, String filename) {  
      boolean flag = true;  
      try {  
            TransformerFactory tFactory = TransformerFactory.newInstance();  
           Transformer transformer = tFactory.newTransformer();  
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
            DOMSource source = new DOMSource(document);  
           StreamResult result = new StreamResult(new File(filename));  
            transformer.transform(source, result);  
       } catch (Exception ex) {  
            flag = false;
            ex.printStackTrace();  
       }  
        return flag;  
    }  

	private User GetUserInfo(String message) {
		// TODO Auto-generated method stub
		String[] info = message.split("\n");
		return new User(info[0],info[1],0);
	}
	private Message handleMessage(String message) {
		// TODO Auto-generated method stub
		int type;
		if(message.substring(0, 1).equals("@"))
		{
			type = Integer.parseInt(message.substring(1,2));
			if(type<0)
				return new Message("",-1);// error
			return new Message(message.substring(3,message.length()-1),type);// right
		}
		else
			return new Message("",-1);// error
	}  

}
