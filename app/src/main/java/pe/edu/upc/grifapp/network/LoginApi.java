package pe.edu.upc.grifapp.network;

import pe.edu.upc.grifapp.models.Login;

/**
 * Created by Jurguen Zambrano on 23/04/2017.
 */

public class LoginApi {
    public static String LOGIN_URL = "http://api.fuel.maraquya.com/login";
    public Login login;

    public Login getCurrentLogin() {
        return login;
    }

    public void setCurrentLogin(Login login) {
        this.login = login;
    }
}
