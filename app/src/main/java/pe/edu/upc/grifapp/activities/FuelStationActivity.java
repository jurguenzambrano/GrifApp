package pe.edu.upc.grifapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

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

public class FuelStationActivity extends AppCompatActivity {

    private static String TAG = "GrifApp";
    User user;
    Login login;
    List<Fuel> fuels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // Obtenemos los datos del usuario
        AndroidNetworking.get(FuelStationApi.FUEL_URL)
                .addHeaders("token",login.getToken())
                .addHeaders("id",user.getId())
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getString("code").equals("0")) {
                                //Snackbar.make(linearLayout, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            //message = Message.build(response);
                            fuels = Fuel.build(response.optJSONArray("data"));

                            //if (message.getCode().equals("0")) {
                                // Si todo esta ok, mostramos mapa de estaciones de servicio cercanos
                                /*
                            }else{
                                //Snackbar.make(linearLayout, message.getMessage(), Snackbar.LENGTH_LONG).show();
                                return;
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
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
