package it.uniroma2.pjdm.didyouplayed.entity;

public class User {
	
	private String name;
	private String surname;
	private String username;
	private String email;
	private String birthdate;
	private String gender;
	
	public User() {
	}
	
	public User(String name, String surname, String username, String email, String birthdate, String gender) {
		super();
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		setBirthdate(birthdate);
		setGender(gender);
	}
	
	public boolean isValid() {
		if(name==null || surname==null || email==null || username==null)
			return false;
		return true;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		if(birthdate!=null)
			this.birthdate = birthdate;
		else
			this.birthdate = "1900-01-01";
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		if(gender!=null)
			this.gender = gender;
		else
			this.gender = "other";
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", surname=" + surname + ", username=" + username + ", email=" + email
				+ ", birthdate=" + birthdate + ", gender=" + gender + "]";
	}
}
