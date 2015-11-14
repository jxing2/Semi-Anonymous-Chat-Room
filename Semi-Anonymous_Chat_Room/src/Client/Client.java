package Client;
import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
public class Client extends JFrame{

	private JTextField j_input;
	private JTextArea j_public;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket sock;
	private String message = "";
	private String IP = "localhost";//host
	
	public Client()
	{
		super("Client");
		j_input = new JTextField();
		ableToType(false);
		j_input.addActionListener(
				new ActionListener(){
					public void actionPerformed(final ActionEvent e)
					{
						sendMessage(e.getActionCommand());
						j_input.setText("");
					}
		});
		
		add(j_input , BorderLayout.SOUTH);
		//----------------------------------------
		
		j_public = new JTextArea();
		j_public.setEditable(false);
		add(new JScrollPane(j_public),BorderLayout.CENTER);
		j_public.setAutoscrolls(true);
		
		addWindowListener
		(
		 new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 System.exit(0);
			 }
		 } );
		setSize(580,380);
		this.setVisible(true);
	}
	
	public void startRunning(){
		try{
			connect();
			setupStreams();
			whileChatting();
		}
		catch(EOFException eofe)
		{
			showMessage("Client terminated connection");
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
			showMessage("Server denied connection.");
		}
		finally{
			close();
		}
	}

	private void close() {
		// TODO Auto-generated method stub
		showMessage("Closing connection");
		ableToType(false);
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

	private void whileChatting() throws IOException {
		// TODO Auto-generated method stub
		String message="";
		ableToType(true);
		do{
			try{
				message = String.valueOf(input.readObject());
				showMessage(message);
			}
			catch(ClassNotFoundException cnfe)
			{
				showMessage("I dont know what user send");
			}
		}while(!message.equals("SERVER - END"));
	}

	private void sendMessage(String message) {
		// TODO Auto-generated method stub
		try{
			output.writeObject("Client - "+ message);
			output.flush();
			showMessage("Client - "+ message);
		}
		catch(IOException ie)
		{
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
		showMessage("Stream ready.");
	}

	private void connect() throws UnknownHostException, IOException, ConnectException {
		// TODO Auto-generated method stub
		showMessage("Attempt connecting...");
		sock = new Socket(IP,6789);
		showMessage("Connected.");
	}
	public static void main(String[] args)
	{
		Client client = new Client();
		client.startRunning();
	}
}
