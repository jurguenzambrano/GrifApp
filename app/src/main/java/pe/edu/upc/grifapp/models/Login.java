package pe.edu.upc.grifapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class Login {
    String message;
    String code;
    String token;

    public String getMessage() {
        return message;
    }

    public Login setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Login setCode(String code) {
        this.code = code;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Login setToken(String token) {
        this.token = token;
        return this;
    }

    public static Login build(JSONObject jsonLogin) {
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
    }
}
