package pe.edu.upc.grifapp.service;

import android.database.sqlite.SQLiteDatabase;

import com.orm.SugarContext;
import com.orm.SugarDb;

import java.lang.reflect.Field;

import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.User;

/**
 * Created by Jurguen Zambrano on 2/05/2017.
 */

public class GrifAppService {
    public boolean addLogin(Long id, String token){
        Login login = new Login();
        login.setId(id);
        login.setToken(token);
        return login.save() > 0;
    }

    public long getTotalUser(){
        return User.count(User.class);
    }

    public Login getLastLogin() {
        Login lastEntry = Login.last(Login.class);
        if(lastEntry != null) {
            return lastEntry;
        }
        return null;
    }

    public User getLastUser(){
        User lastEntry = User.last(User.class);
        if(lastEntry != null) {
            return lastEntry;
        }
        return null;
    }

    private SQLiteDatabase getDatabase() {
        try {
            Field f = SugarContext.getSugarContext().getClass().getDeclaredField("grifapp.db");
            f.setAccessible(true);
            return ((SugarDb) f.get(SugarContext.getSugarContext())).getDB();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
