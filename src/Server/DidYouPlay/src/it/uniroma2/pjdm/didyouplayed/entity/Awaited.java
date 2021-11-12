package it.uniroma2.pjdm.didyouplayed.entity;

public class Awaited {
	
	private String user;
	private int videogame;
	private String created_at;
	
	public Awaited() {
	}

	public Awaited(String user, int videogame, String created_at) {
		super();
		this.user = user;
		this.videogame = videogame;
		this.created_at = created_at;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getVideogame() {
		return videogame;
	}

	public void setVideogame(int videogame) {
		this.videogame = videogame;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "Awaited [user=" + user + ", videogame=" + videogame + ", created_at=" + created_at + "]";
	}
}
