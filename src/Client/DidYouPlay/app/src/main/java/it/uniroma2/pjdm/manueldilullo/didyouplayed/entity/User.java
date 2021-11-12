package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    protected String name;
    protected String surname;
    protected String username;
    protected String email;
    protected String birthdate;
    protected String gender;

    public User(String name, String surname, String username, String email, String birthdate, String gender) {
        super();
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        setBirthdate(birthdate);
        setGender(gender);
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

    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", name);
            jo.put("surname", surname);
            jo.put("username", username);
            jo.put("email", email);
            jo.put("birtdate", birthdate);
            jo.put("gender", gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
