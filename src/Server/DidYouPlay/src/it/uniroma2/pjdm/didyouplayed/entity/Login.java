package it.uniroma2.pjdm.didyouplayed.entity;

public class Login {
	
	private String username;
	private String password;
	
	public Login() {	
	}
	
	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public boolean isValid(){
		if(username==null || password==null || password.length() < 8)
			return false;
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "Login [username=" + username + ", password=" + password + "]";
	}
}
