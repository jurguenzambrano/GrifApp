package pe.edu.upc.grifapp;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import pe.edu.upc.grifapp.models.Location;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.Promotion;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.LoginApi;
import pe.edu.upc.grifapp.network.PromotionsApi;
import pe.edu.upc.grifapp.network.UsersApi;
import pe.edu.upc.grifapp.service.GrifAppService;

/**
 * Created by Jurguen Zambrano on 22/04/2017.
 */

public class GrifApp extends com.orm.SugarApp {
    // Singleton Pattern Implementation
    private static GrifApp instance;
    private UsersApi usersApi = new UsersApi();
    private LoginApi loginApi = new LoginApi();
    private PromotionsApi promotionsApi = new PromotionsApi();
    private GrifAppService grifAppService = new GrifAppService();

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
    public void setCurrentPromotion(Promotion promotion){ promotionsApi.setCurrentPromotion(promotion);}

    public Promotion getCurrentPromotion(){return promotionsApi.getCurrentPromotion();}

    public GrifAppService getGrifAppService() {return grifAppService;}

    public void setGrifAppService(GrifAppService grifAppService){ this.grifAppService = grifAppService; }

}
