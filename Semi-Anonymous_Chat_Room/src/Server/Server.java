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
		private int count ; // count how many times people logged.
		public Server()
		{
			super("Server");
			al = new ArrayList<Waiter>();
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
				Waiter waiter ;
				while(true)
				{
					try{
						waiter = new Waiter(j_public,al,count);
						waiter.serve(server);
						waiter.start();
						al.add(waiter);
						count++;
					}
					catch(Exception eofe)
					{
						showMessage("Server Ended Connection");
					}
//					finally{
//						for(int i = 0 ; i < al.size(); ++i)
//						{
//							al.get(i).close();
//						}
//					}
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
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
