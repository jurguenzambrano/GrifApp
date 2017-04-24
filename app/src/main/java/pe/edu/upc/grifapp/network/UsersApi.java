package pe.edu.upc.grifapp.network;

import pe.edu.upc.grifapp.models.User;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class UsersApi {
    public static String USERS_URL = "http://api.fuel.maraquya.com/users";
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}
