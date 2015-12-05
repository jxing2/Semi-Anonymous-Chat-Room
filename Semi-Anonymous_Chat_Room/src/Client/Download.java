package Client;

public class Download extends Thread{
	private String savePath;
	public String fileName;
	public int getPercentage()
	{
		return (int)(((double)sendByte)/totalByte*100);
	}
	private long sendByte;
	private long totalByte;
	public Download(String savePath){
		this.savePath = savePath;
	}
}
