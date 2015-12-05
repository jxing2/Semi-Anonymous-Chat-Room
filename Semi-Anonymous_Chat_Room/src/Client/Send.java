package Client;

import java.io.*;
import java.net.*;

public class Send extends Thread{
	private Socket socket;
	private int filePort;
	private String filePath;
	//private BufferedOutputStream outToServer;
	private String IP;
	FileInputStream fis;
	ObjectOutputStream os;
	BufferedInputStream bis;
	private long sendByte,totalByte;
	public String fileName;
	public Send (String filePath, int filePort, String IP, long totalByte){
		File file = new File(filePath);
		fileName = file.getName();
		this.filePath = filePath;
		this.filePort = filePort;
		this.IP = IP;
		this.totalByte = totalByte;
	}
	
	public int getPercentage()
	{
		return (int)(((double)sendByte)/totalByte*100);
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
	        while ((len = fis.read(buf)) != -1) {
	            os.write(buf, 0, len);
	            sendByte+=len;
	            os.flush();
	        }
	        fis.close();
	        os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
