package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Waiter extends Thread {
	public ObjectOutputStream output;
	public ObjectInputStream input;
	public ServerSocket server;
	public Socket sock;
	private JTextArea j_public;
	public Waiter(JTextArea j_public)
	{
		this.j_public = j_public;
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
	
	private void sendMessage(String message) {
		// TODO Auto-generated method stub
		try{
			output.writeObject("Client - "+ message);
			output.flush();
			showMessage("Client - "+ message );
		}
		catch(IOException ie)
		{
			j_public.append("Something WRONG");
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
	
	public void run() {  
		String message = "connected\n";
		sendMessage(message);
		do{
			try{
				message = String.valueOf(input.readObject());
				showMessage(message);
			}
			catch(ClassNotFoundException cnfe)
			{
				showMessage("I dont know what user send");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}while(!message.equals("SERVER - END"));
	}  

}
