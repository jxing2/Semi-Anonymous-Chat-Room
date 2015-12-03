package Client;

import java.io.*;
import java.net.*;

public class Send extends Thread {
	private Socket socket;
	private int filePort;
	private String filePath;
	private BufferedOutputStream outToServer;
	private String IP;
	FileInputStream fis;
	ObjectOutputStream os;
	BufferedInputStream bis;
	public Send (String filePath, int filePort, String IP){
		this.filePath = filePath;
		this.filePort = filePort;
		this.IP = IP;
	}

	
	public void run(){
		
		try {
			int bufferSize = 10240;
			byte[] buf = new byte[bufferSize];
			File file = new File(filePath);
			socket = new Socket(IP, filePort);
			os = new ObjectOutputStream(socket.getOutputStream());
			fis = new FileInputStream(file);
			int len = 0;  
			System.out.println("ready");
	        while ((len = fis.read(buf)) != -1) {  
	            os.write(buf, 0, len);  
	            System.out.println(len);
	            os.flush();
	        } 
	        //os.flush();
	        fis.close();
	        os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
