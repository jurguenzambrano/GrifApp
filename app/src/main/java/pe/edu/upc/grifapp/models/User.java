package pe.edu.upc.grifapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class User {
    String email;
    String password;
    String name;
    String lastName;

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public static Login build(JSONObject jsonArticle) {
        if(jsonArticle == null) return null;
        Login login = new Login();
        try {
            login.setCode(jsonArticle.getString("code"))
                    .setMessage(jsonArticle.getString("message"))
                    .setToken(jsonArticle.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return login;
    }
}
