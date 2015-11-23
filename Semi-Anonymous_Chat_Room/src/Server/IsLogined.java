package Server;

import java.util.ArrayList;

public class IsLogined {
	private ArrayList<Waiter> al;
	private boolean isLogined = false;
	private String userName;
	public IsLogined(ArrayList<Waiter> al, String userName) {
		this.al = al;
		this.userName = userName;
	}
	public boolean run() {
		for (int i = 0; i < al.size(); i++) {
			if(al.get(i).realName!=null)
			if (al.get(i).realName.equals(userName)) {
				return true;
			}
		}
		return false;
	}
}