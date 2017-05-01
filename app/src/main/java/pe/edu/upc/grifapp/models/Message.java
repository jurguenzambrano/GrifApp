package pe.edu.upc.grifapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jurguen Zambrano on 30/04/2017.
 */

public class Message {
    String code;
    String message;

    public String getCode() {
        return code;
    }

    public Message setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public static Message build(JSONObject jsonLogin) {
        if(jsonLogin == null) return null;
        Message message = new Message();
        try {
            message.setCode(jsonLogin.getString("code"))
                    .setMessage(jsonLogin.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
