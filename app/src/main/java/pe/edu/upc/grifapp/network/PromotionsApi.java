package pe.edu.upc.grifapp.network;

import pe.edu.upc.grifapp.models.Promotion;

/**
 * Created by Nic Marcelo on 2/05/2017.
 */

public class PromotionsApi {
    public static String PROMOTIONS_URL = "http://upc-grifo.apphb.com/PromotionsServices.svc/promotions";
    private Promotion currentPromotion;

    public Promotion getCurrentPromotion() { return currentPromotion; }

    public void setCurrentPromotion(Promotion promotion) { this.currentPromotion = promotion; }
}
