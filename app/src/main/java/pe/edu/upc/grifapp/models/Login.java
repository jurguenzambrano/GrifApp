package pe.edu.upc.grifapp.models;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class Login extends SugarRecord{
    String token;
    Long id;

    public Login(){

    }

    public Login(Long id, String token){
        this.id = id;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Login setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Login build(JSONObject jsonLogin) {
        if(jsonLogin == null) return null;
        Login login = new Login();
        try {
            login.setToken(jsonLogin.getString("token")).setId(jsonLogin.getLong("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return login;
    }
}
