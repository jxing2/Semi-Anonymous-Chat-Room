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
		return (int)(((double)sendByte)/totalByte*100);
	}
	private long sendByte;
	private long totalByte;
	public Download(String savePath, String IP, int filePort){
		this.savePath = savePath;
		this.IP = IP;
		this.filePort = filePort;
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
			
			FileOutputStream fos = new FileOutputStream(file.getPath());
			byte[] buf = new byte[10240];
			int len = 0;
			
			while ((len = input.read(buf)) != -1) {
				fos.write(buf, 0, len);
				System.out.println(len);
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
