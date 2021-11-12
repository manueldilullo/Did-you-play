package it.uniroma2.pjdm.didyouplayed.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.StringTokenizer;

import jakarta.servlet.http.HttpServletRequest;

public class BasicAuth {

	public static String[] credentialsWithBasicAuthentication(HttpServletRequest req) {
		String[] credentials = null;
		
		String authHeader = req.getHeader("Authorization");
	    if (authHeader != null) {
	    	System.out.println("authHeader\t"+authHeader);
	    	
	        StringTokenizer st = new StringTokenizer(authHeader);
	        if (st.hasMoreTokens()) {
	            String basic = st.nextToken();

	            if (basic.equalsIgnoreCase("Basic")) {
	                try {
	                    String content = new String(Base64.getDecoder().decode(st.nextToken()), "UTF-8");
	                    System.out.println("Decode\t" + content);
	                    int p = content.indexOf(":");
	                    if (p != -1) {
	                        String login = content.substring(0, p).trim();
	                        String password = content.substring(p + 1).trim();

	                        System.out.println("Username\t" + login);
	                        System.out.println("Password\t" + password);
	                        
	                        credentials = new String[2];
	                        credentials[0] = login;
	                        credentials[1] = password;
	                    } else {
	                        System.err.println("Invalid authentication token");
	                        return null;
	                    }
	                } catch (UnsupportedEncodingException | NullPointerException e) {
	                	e.printStackTrace();
	                	System.err.println("Couldn't retrieve authentication");
	                	return null;
	                }
	            }
	        }
	    }
	    return credentials;
	}
	
	
	public static String EncryptMD5(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] messageDigest = md.digest(string.getBytes());
	        BigInteger number = new BigInteger(1, messageDigest);
	        String hashtext = number.toString(16);
		
	        return hashtext;
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
