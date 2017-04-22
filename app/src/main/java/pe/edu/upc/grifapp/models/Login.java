package pe.edu.upc.grifapp.models;

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
}
