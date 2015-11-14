package Server;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;
public class Server<ref> extends JFrame{

		private JTextArea j_public;
		private ObjectOutputStream output;
		private ObjectInputStream input;
		private ServerSocket server;
		private Socket sock;
		private ArrayList<Waiter> al;
		
		public Server()
		{
			super("Server");
			
			j_public = new JTextArea();
			j_public.setAutoscrolls(true);
			j_public.setEditable(false);
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
				server = new ServerSocket(6789, 999);
				while(true)
				{
					try{
						Waiter waiter = new Waiter(j_public);
						waiter.serve(server);
						waiter.start();
						al.add(waiter);
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

		public static void main(String[] args)
		{
			Server server = new Server();
			server.startRunning();
		}
}
