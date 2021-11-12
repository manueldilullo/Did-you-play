package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.videogame;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities.HttpResponseHandler;

public interface VideogameActivityController {
    public void addDetailsRows();
    public void addToAwaited();
    public void addToPlayed(int score);
    public void share(String receiver_username);
}
