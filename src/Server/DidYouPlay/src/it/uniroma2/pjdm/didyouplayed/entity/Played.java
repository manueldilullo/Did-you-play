package it.uniroma2.pjdm.didyouplayed.entity;

public class Played {

	private String user;
	private int videogame;
	private int score;

	public Played() {
	}

	public Played(String user, int videogame, int score) {
		super();
		this.user = user;
		this.videogame = videogame;
		this.score = score;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) throws OutOfRangeException {
		if (score <= 5 && score >= 0)
			this.score = score;
		else
			throw new OutOfRangeException("Score must be between 0 and 5");
	}

	@Override
	public String toString() {
		return "Played [user=" + user + ", videogame=" + videogame + ", score=" + score + "]";
	}
}
