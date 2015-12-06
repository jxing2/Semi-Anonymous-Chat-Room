package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
import java.net.Socket;

public class Download extends Thread{
	private String savePath;
	private Socket socket;
	private int filePort;
	private String IP;
	public String fileName;
	ObjectInputStream input;
	//ObjectOutputStream output;
	public int getPercentage()
	{
		if(totalByte == 0)
			return 100;
		return (int)(((double)sendByte)/totalByte*100);
	}
	private long lastSendByte;
	public long getLastSendByte() {
		return lastSendByte;
	}

	public void setLastSendByte(long lastSendByte) {
		this.lastSendByte = lastSendByte;
	}

	public long getSendByte() {
		return sendByte;
	}
	private long sendByte;
	private long totalByte;
	public Download(String savePath, String IP, int filePort, long totalByte){
		this.savePath = savePath;
		this.IP = IP;
		this.filePort = filePort;
		this.totalByte = totalByte;
	}
	
	private void setupStreams() {
		try {
			socket = new Socket (IP, filePort);
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		setupStreams();
		try {
			
			File file = new File(savePath);
			fileName = file.getName();
			FileOutputStream fos = new FileOutputStream(file.getPath());
			byte[] buf = new byte[2048];
			int len = 0;
			
			while ((len = input.read(buf)) != -1) {
				fos.write(buf, 0, len);
				sendByte += len;
				fos.flush();
			}
			
			fos.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
