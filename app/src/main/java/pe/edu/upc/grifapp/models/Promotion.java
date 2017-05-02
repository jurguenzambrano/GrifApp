package pe.edu.upc.grifapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nic Marcelo on 2/05/2017.
 */

public class Promotion {
    private long id;
    private String code;
    private String name;
    private String description;
    private String pathImage;
    private long state;

    public long getId() {
        return id;
    }

    public Promotion setId(long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Promotion setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Promotion setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Promotion setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPathImage() {
        return pathImage;
    }

    public Promotion setPathImage(String pathImage) {
        this.pathImage = pathImage;
        return this;
    }

    public long getState() {
        return state;
    }

    public Promotion setState(long state) {
        this.state = state;
        return this;
    }

    public static Promotion build(JSONObject jsonFuel) {
        if(jsonFuel == null) return null;
        Promotion promotion = new Promotion();
        try {/*
            int length = jsonFuel.getJSONArray("sortBysAvailable").length();
            List<String> sortBysAvailable = new ArrayList<>();
            for(int i = 0; i < length; i++) {
                sortBysAvailable.add(
                        jsonFuel.getJSONArray("sortBysAvailable")
                                .getString(i));
            }*/
            promotion.setId(jsonFuel.getLong("Id"))
                    .setName(jsonFuel.getString("Name"))
                    .setCode(jsonFuel.getString("Code"))
                    .setDescription(jsonFuel.getString("Description"))
                    .setPathImage(jsonFuel.getString("PathImage"))
                    .setState(jsonFuel.getLong("State"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return promotion;
    }

    public static List<Promotion> build(JSONArray jsonPromotions) {
        if(jsonPromotions == null) return null;

        int length = jsonPromotions.length();
        List<Promotion> promotions = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            try {
                promotions.add(Promotion.build(jsonPromotions.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return promotions;
    }
}
