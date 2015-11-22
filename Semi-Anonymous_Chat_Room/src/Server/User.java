package Server;

public class User {
	public String name;
	public String password;
	public String new_password;
	public int type;

	public User(String name, String password, int type) {
		this.name = name;
		this.password = password;
		this.type = type;
	}

	public User(String name, String password, int type, String new_PWD) {
		this(name, password, type);
		this.new_password = new_PWD;
	}
}
