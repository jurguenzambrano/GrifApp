package pe.edu.upc.grifapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juan on 1/05/2017.
 */

public class Fuel {

    String id;
    String name;
    String latitude;
    String altitude;
    String address;
    String type;
    private List<String> sortBysAvailable;


    public String getId(){
        return id;
    }

    public Fuel setId(String id){
        this.id = id;
        return this;
    }

    public String getName(){return name;}

    public Fuel setName(String name){
        this.name = name;
        return this;
    }

    public String getLatitude(){return latitude;}

    public Fuel setLatitude(String latitude){
        this.latitude = latitude;
        return this;
    }

    public String getAltitude(){return altitude;}

    public Fuel setAltitude(String altitude){
        this.altitude = altitude;
        return this;
    }

    public String getAddress(){return address;}

    public Fuel setAddress(String address){
        this.address = address;
        return this;
    }

    public String getType(){return type;}

    public Fuel setType(String type){
        this.type = type;
        return this;
    }

    public List<String> getSortBysAvailable() {
        return sortBysAvailable;
    }

    public Fuel setSortBysAvailable(List<String> sortBysAvailable) {
        this.sortBysAvailable = sortBysAvailable;
        return this;
    }

    public static Fuel build(JSONObject jsonFuel) {
        if(jsonFuel == null) return null;
        Fuel source = new Fuel();
        try {
            int length = jsonFuel.getJSONArray("sortBysAvailable").length();
            List<String> sortBysAvailable = new ArrayList<>();
            for(int i = 0; i < length; i++) {
                sortBysAvailable.add(
                        jsonFuel.getJSONArray("sortBysAvailable")
                                .getString(i));
            }
            source.setId(jsonFuel.getString("id"))
                    .setName(jsonFuel.getString("name"))
                    .setLatitude(jsonFuel.getString("latitude"))
                    .setAltitude(jsonFuel.getString("altitude"))
                    .setAddress(jsonFuel.getString("address"))
                    .setType(jsonFuel.getString("type"))
                    .setSortBysAvailable(sortBysAvailable);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return source;
    }


    public static List<Fuel> build(JSONArray jsonFuels) {
        if(jsonFuels == null) return null;

        int length = jsonFuels.length();
        List<Fuel> fuels = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            try {
                fuels.add(Fuel.build(jsonFuels.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return fuels;
    }

}
