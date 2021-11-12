package it.uniroma2.pjdm.manueldilullo.didyouplayed.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegister extends User {

    private String password;
    private String confirmPassword;

    public UserRegister(String name, String surname, String username, String email, String birthdate, String gender, String password, String confirmPassword) {
        super(name, surname, username, email, birthdate, gender);
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword(){
        return password;
    }

    public String getConfirmPassword(){
        return confirmPassword;
    }

    @Override
    public String toString() {
        return "UserRegister{" +
                "password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", gender='" + gender + '\'' +
                "} ";
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jo = super.toJSON();
        try {
            jo.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
