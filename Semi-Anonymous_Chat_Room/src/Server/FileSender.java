package Server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender extends Thread {
	private String filePath;
	private int fileport;
	private ServerSocket fileServer;
	private Socket s;
	FileInputStream fis;
	//ObjectOutputStream os;
	ObjectOutputStream output;
	ObjectInputStream input;
	BufferedInputStream bis;
	File fileToSend;
	long size;
	public FileSender(String filePath,long size, int fileport, ServerSocket fileServer){
		this.filePath = filePath;
		this.fileport = fileport;
		this.fileServer = fileServer;
		this.size = size;
	}
	private void waitForConnection(ServerSocket fileServer) {
		try {
			s = fileServer.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setupStreams() {
		try {
			output = new ObjectOutputStream(s.getOutputStream());
			output.flush();
			input = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		waitForConnection(fileServer);
		setupStreams();
		
		try {
			int bufferSize = 10240;
			byte[] buf = new byte[bufferSize];
			fileToSend = new File(filePath);
			//socket = new Socket(IP, filePort);
			//output = new ObjectOutputStream(s.getOutputStream());
			fis = new FileInputStream(fileToSend);
			int len = 0;  
			System.out.println("ready");
	        while ((len = fis.read(buf)) != -1) {  
	            output.write(buf, 0, len);  
	            System.out.println(len);
	            output.flush();
	        } 
	        //os.flush();
	        fis.close();
	        output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
