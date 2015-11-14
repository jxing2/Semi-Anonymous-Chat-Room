package Server;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;
public class Server extends JFrame{

		private JTextField j_input;
		private JTextArea j_public;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private ServerSocket server;
		private Socket sock;
		private ArrayList al;
		
		public Server()
		{
			super("Server");
			j_input = new JTextField();
			ableToType(false);
			j_input.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							sendMessage(e.getActionCommand());
							j_input.setText("");
						}
			});
			
			add(j_input , BorderLayout.SOUTH);
			//----------------------------------------
			
			j_public = new JTextArea();
			
			add(new JScrollPane(j_public));
			
			
			
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
				server = new ServerSocket(6789, 3);
				while(true)
				{
					try{
						waitForConnection();
						setupStreams();
						whileChatting();
					}
					catch(Exception eofe)
					{
						showMessage("Server Ended Connection");
					}
					finally{
						close();
					}
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
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

		private void ableToType(boolean b) {
			// TODO Auto-generated method stub
			j_input.setEditable(b);
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
			String message = "connected\n";
			sendMessage(message);
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

		private void setupStreams() throws IOException {
			// TODO Auto-generated method stub
			output = new ObjectOutputStream(sock.getOutputStream());
			output.flush();
			input = new ObjectInputStream(sock.getInputStream());
		}

		private void waitForConnection() {
			// TODO Auto-generated method stub
			try {
				sock = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		public static void main(String[] args)
		{
			Server server = new Server();
			server.startRunning();
		}
}
