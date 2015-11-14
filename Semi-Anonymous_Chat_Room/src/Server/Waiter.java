package Server;

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

public class Waiter extends Thread {
	public ObjectOutputStream output;
	public ObjectInputStream input;
	public ServerSocket server;
	public Socket sock;
	private JTextArea j_public;
	private ArrayList<Waiter> al;
	private String nickName;
	private String realName;
	public Waiter(JTextArea j_public,ArrayList<Waiter> al, int count)
	{
		this.j_public = j_public;
		this.al = al;
		nickName = "User"+count;
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
			j_public.append("Something WRONG");
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
			j_public.append("Something WRONG");
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
		String message = nickName + " connected";
		sendMessage(message);
		SendToOthers(message);
		do{
			try{
				message = String.valueOf(input.readObject());
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
				Calendar cal = Calendar.getInstance();
				showMessage(nickName + " - "+ dateFormat.format(cal.getTime())+"\n" + message );
				System.out.println(message);
				SendToOthers(message);
			}
			catch(ClassNotFoundException cnfe)
			{
				showMessage("I dont know what user send");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//close();
			}
		}while(!message.equals("SERVER - END"));
	}  

}
