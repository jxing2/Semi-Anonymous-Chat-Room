package Server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileSender extends Thread {
	private String filePath;
	private int fileport;
	private ServerSocket fileServer;
	private Socket s;
	FileInputStream fis;
	ObjectOutputStream output;
	ObjectInputStream input;
	File fileToSend;
	long sendByte, totalByte;
	private Lock connectionLock;

	String message;
	ServerCommand command;
	Waiter waiter;

	public FileSender(String filePath, long totalByte, int fileport,
			ServerSocket fileServer, Lock connectionLock, Waiter waiter) {
		this.filePath = filePath;
		this.fileport = fileport;
		this.fileServer = fileServer;
		this.totalByte = totalByte;
		this.connectionLock = connectionLock;
		this.waiter = waiter;
	}

	private void waitForConnection(ServerSocket fileServer) {
		try {
			s = fileServer.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public void setMessage(String message, ServerCommand command) {
		this.message = message;
		this.command = command;
	}

	private void setupStreams() {
		try {
			output = new ObjectOutputStream(s.getOutputStream());
			output.flush();
			// input = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			this.connectionLock.lock();
			waiter.sendMessage(message, command);
			waitForConnection(fileServer);
		} catch (Exception e) {
		} finally {
			this.connectionLock.unlock();
		}
		setupStreams();

		try {
			int bufferSize = 2048;
			byte[] buf = new byte[bufferSize];
			fileToSend = new File(filePath);
			System.out.println(filePath);
			fis = new FileInputStream(fileToSend);
			int len = 0;
			while ((len = fis.read(buf)) != -1) {
				output.write(buf, 0, len);
				sendByte += len;
				output.flush();
			}
			// os.flush();
			fis.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
