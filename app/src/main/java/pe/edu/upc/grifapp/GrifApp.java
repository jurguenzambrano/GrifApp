package pe.edu.upc.grifapp;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.UsersApi;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class GrifApp extends Application {
    // Singleton Pattern Implementation
    private static GrifApp instance;
    UsersApi usersApi = new UsersApi();

    public GrifApp() {
        super();
        instance = this;
    }

    public static GrifApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }

    // Delegate Pattern Implementation

    public void setCurrentUser(User User) {
        usersApi.setCurrentUser(User);
    }

    public User getCurrentUser() {
        return usersApi.getCurrentUser();
    }

//    public void setCurrentArticle(Article article) {
//        newsApi.setCurrentArticle(article);
//    }
//
//    public Article getCurrentArticle() {
//        return newsApi.getCurrentArticle();
//    }

}
