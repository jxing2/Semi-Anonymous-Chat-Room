package Client;

public class Download extends Thread{
	private String savePath;
	public Download(String savePath){
		this.savePath = savePath;
	}
}
