package pe.edu.upc.grifapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class Login {
    String token;
    String id;

    public String getToken() {
        return token;
    }

    public Login setToken(String token) {
        this.token = token;
        return this;
    }

    public String getId() {
        return id;
    }

    public Login setId(String id) {
        this.id = id;
        return this;
    }

    public static Login build(JSONObject jsonLogin) {
        if(jsonLogin == null) return null;
        Login login = new Login();
        try {
            login.setToken(jsonLogin.getString("token")).setId(jsonLogin.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return login;
    }
}
