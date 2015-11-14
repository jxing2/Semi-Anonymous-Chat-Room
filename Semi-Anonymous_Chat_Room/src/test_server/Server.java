package test_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	private int port = 8000;
	private int server_num = 3;
	private static ServerSocket serverSocket;
	Socket socket;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(port, server_num);
		System.out.println("The server is starting");
		
	}
	public void service() {
		for(int i = 0 ; i < 12; ++i) {
			socket = null;
			try {
				socket = serverSocket.accept();
				System.out.println("new connection is completed " + socket.getInetAddress() + ":" + socket.getPort());
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if(socket != null) {
					try {
						socket.close();
						//serverSocket.close();
						//break;
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws IOException, InterruptedException {
		Server server = new Server();
		Thread.sleep(600*10);
		server.service();
	}
}
