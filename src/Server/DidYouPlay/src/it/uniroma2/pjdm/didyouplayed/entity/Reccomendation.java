package it.uniroma2.pjdm.didyouplayed.entity;

public class Reccomendation {
	
	private String sender;
	private String receiver;
	private int videogame;
	private String created_at;
	
	public Reccomendation() {
	}

	public Reccomendation(String sender, String receiver, int videogame, String created_at) {
		this.sender = sender;
		this.receiver = receiver;
		this.videogame = videogame;
		this.setCreatedAt(created_at);
	}
	
	public Reccomendation(String sender, String receiver, int videogame) {
		this.sender = sender;
		this.receiver = receiver;
		this.videogame = videogame;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getVideogame() {
		return videogame;
	}

	public void setVideogame(int videogame) {
		this.videogame = videogame;
	}

	@Override
	public String toString() {
		return "Reccomendation [sender=" + sender + ", receiver=" + receiver + ", videogame=" + videogame + "]";
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}
	
}
