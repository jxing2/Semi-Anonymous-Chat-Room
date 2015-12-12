package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileReceiver extends Thread {
	public String fileName;
	private int filePort;
	private ServerSocket fileServer;
	private Socket s;
	File sharedDir;
	File file;
	long size;
	long receivedSize;
	ObjectInputStream input;
	ObjectOutputStream output;

	public int getPercentage() {
		return (int) ((double) receivedSize / size * 100);
	}

	private Lock connectionLock;
	Waiter waiter;
	String message;
	ServerCommand command;

	public FileReceiver(String fileName, File sharedDir, long size,
			ServerSocket fileServer, Lock connectionLock, Waiter waiter) {
		this.fileName = fileName;
		this.sharedDir = sharedDir;
		this.size = size;
		this.fileServer = fileServer;
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

	private void setupStreams() {
		try {
			input = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMessage(String message, ServerCommand command) {
		this.message = message;
		this.command = command;
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
			FileOutputStream fos = new FileOutputStream(file.getPath());
			byte[] buf = new byte[2048];
			int len = 0;
			while ((len = input.read(buf)) != -1) {
				fos.write(buf, 0, len);
				receivedSize += len;
				fos.flush();
			}
			fos.close();
			input.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean test() {
		// TODO Auto-generated method stub
		int dot = fileName.lastIndexOf('.');
		String prefix, suffix;
		if (dot >= 0) {
			prefix = fileName.substring(0, dot);
			suffix = fileName.substring(dot + 1);
		} else {
			prefix = fileName;
			suffix = "";
		}
		try {
			File tmp;
			boolean exist;
			do {
				tmp = new File(sharedDir, prefix + "." + suffix);
				exist = tmp.exists();
				if (!exist) {
					tmp.createNewFile();
				} else {
					prefix += "-";
				}
			} while (exist);
			file = tmp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		fileName = prefix + "." + suffix;

		return true;
	}

	public void release() {
		// TODO Auto-generated method stub
		connectionLock.unlock();
	}

}
