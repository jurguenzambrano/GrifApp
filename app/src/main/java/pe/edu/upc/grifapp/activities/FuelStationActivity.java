package pe.edu.upc.grifapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Fuel;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.Message;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.FuelStationApi;
import pe.edu.upc.grifapp.network.UsersApi;

public class FuelStationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static String TAG = "GrifApp";
    User user;
    Login login;
    List<Fuel> fuels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        fuels = new ArrayList<Fuel>();
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        user = GrifApp.getInstance().getCurrentUser();
        login = GrifApp.getInstance().getCurrentLogin();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Obtenemos todas las estaciones de servicio
        AndroidNetworking.get(FuelStationApi.FUEL_URL)
                .addHeaders("token",login.getToken())
                .addHeaders("id",user.getId())
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                //.getAsJSONObject(new JSONObjectRequestListener() {
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //message = Message.build(response);
                        fuels = Fuel.build(response);

                        int length = fuels.size();
                        for(int i = 0; i < length; i++) {
                            LatLng estacion = new LatLng(Double.parseDouble(fuels.get(i).getAltitude()),Double.parseDouble(fuels.get(i).getLatitude()));
                            googleMap.addMarker(new MarkerOptions()
                                    .position(estacion)
                                    .title(fuels.get(i).getName()));
                        }

                        LatLng upc = new LatLng(-12.0919393,-76.9675561);
                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(upc)
                                .zoom(16)
                                .build();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                    @Override
                    public void onError(ANError anError) {
                        String messageError = "Error en aplicativo";
                        try {
                            JSONObject jsonBody = new JSONObject(anError.getErrorBody());
                            messageError = jsonBody.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Snackbar.make(linearLayout, messageError, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                });

    }
}
