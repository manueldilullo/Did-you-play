package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

public class Reccomendation {

    private Videogame videogame;
    private String sender;
    private String receiver;
    private String created_at;

    public Reccomendation(Videogame videogame, String sender, String receiver, String created_at) {
        this.videogame = videogame;
        this.sender = sender;
        this.receiver = receiver;
        this.created_at = created_at;
    }

    public Videogame getVideogame() {
        return videogame;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getCreated_at() {
        return created_at;
    }

    @Override
    public String toString() {
        return "Reccomendation{" +
                "videogame=" + videogame +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
