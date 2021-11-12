package it.uniroma2.pjdm.manueldilullo.didyouplayed.controller.HttpRequestsUtilities;

import java.util.Base64;

import it.uniroma2.pjdm.manueldilullo.didyouplayed.entity.Login;

public class BasicAuth {

    public static String basicAuthenticationToken(Login login){
        return BasicAuth.basicAuthenticationToken(login.getUsername(), login.getPassword());
    }

    public static String basicAuthenticationToken(String username, String password){
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
