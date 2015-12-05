package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Download extends Thread implements SendAndReceive{
	private String savePath;
	private Socket s;
	private int filePort;
	public String fileName;
	ObjectInputStream input;
	ObjectOutputStream output;
	public int getPercentage()
	{
		return (int)(((double)sendByte)/totalByte*100);
	}
	private long sendByte;
	private long totalByte;
	public Download(String savePath){
		this.savePath = savePath;
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
		setupStreams();
		
		try {
			FileOutputStream fos = new FileOutputStream(savePath);
			byte[] buf = new byte[10240];
			int len = 0;
			do{
			while ((len = input.read(buf)) != -1) {
				fos.write(buf, 0, len);
				System.out.println(len);
				sendByte+=len;
				fos.flush();
			}
			}while(sendByte < totalByte);
			fos.close();
			input.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	File sharedDir;
	File file;




	private void waitForConnection(ServerSocket fileServer) {
		try {
			s = fileServer.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



//	public void run() {
//		waitForConnection(fileServer);
//		setupStreams();
//
//		try {
//			FileOutputStream fos = new FileOutputStream(file.getPath());
//			byte[] buf = new byte[10240];
//			int len = 0;
//			do{
//			while ((len = input.read(buf)) != -1) {
//				fos.write(buf, 0, len);
//				System.out.println(len);
//				receivedSize+=len;
//				fos.flush();
//			}
//			}while(receivedSize<size);
//			fos.close();
//			input.close();
//			s.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
