package pe.edu.upc.grifapp.models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class User extends SugarRecord{
    Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


/*    public static Login builNewUser(JSONObject jsonLogin) {
        if(jsonLogin == null) return null;
        Login login = new Login();
        try {
            login.setCode(jsonLogin.getString("code"))
                    .setMessage(jsonLogin.getString("message"));
            JSONObject uniObject = jsonLogin.getJSONObject("data");
            login.setToken(uniObject.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return login;
    }*/

    public static User build(JSONObject jsonUser) {
        try {
            if(jsonUser == null) {
                return null;
            }

            User user = new User();
            user.setEmail(jsonUser.getString("email"))
                .setName(jsonUser.getString("name"))
                .setLastName(jsonUser.getString("last_name"))
                .setId(jsonUser.getLong("id"));

            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
/*
    public static User user(JSONObject jsonUser) {
        try {
            if(jsonUser == null || jsonUser.getInt("code") != 0) {
                return null;
            }

            User user = new User();
            user.setEmail(jsonUser.getJSONObject("data").getString("email"))
                    .setName(jsonUser.getJSONObject("data").getString("name"))
                    .setLastName(jsonUser.getJSONObject("data").getString("last_name"));

            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    */
}
