package pe.edu.upc.grifapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Fuel;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.Message;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.models.Weather;
import pe.edu.upc.grifapp.network.CountryApi;
import pe.edu.upc.grifapp.network.OpenWeatherMapApi;
import pe.edu.upc.grifapp.network.UsersApi;
import pe.edu.upc.grifapp.service.GrifAppService;

public class WelcomeActivity extends AppCompatActivity implements LocationListener {

    private TextView mHello;
    private TextView mWelcome;
    private ImageView imageViewPromotions;
    private ImageView imageViewLocations;
    private User user;
    private Login login;

    private TextView countryTextView;
    private TextView wheaterTextView;
    private TextView tempTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView tempMinTextView;
    private TextView tempMaxTextView;
    private TextView ciyTextView;
    private ANImageView weatherImageView;

    final static int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private String locationProvider = LocationManager.GPS_PROVIDER;
    private boolean locationPermissionGranted = false;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;

    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        locationListener = this;

        mHello = (TextView) findViewById(R.id.mHello);
        mWelcome = (TextView) findViewById(R.id.mWelcome);
        imageViewPromotions = (ImageView) findViewById(R.id.imageViewPromotions);
        imageViewLocations = (ImageView) findViewById(R.id.imageViewLocations);

        countryTextView = (TextView) findViewById(R.id.countryTextView);
        wheaterTextView = (TextView) findViewById(R.id.wheaterTextView);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        humidityTextView = (TextView) findViewById(R.id.humidityTextView);
        pressureTextView = (TextView) findViewById(R.id.pressureTextView);
        tempMinTextView = (TextView) findViewById(R.id.tempMinTextView);
        tempMaxTextView = (TextView) findViewById(R.id.tempMaxTextView);
        ciyTextView = (TextView) findViewById(R.id.ciyTextView);
        weatherImageView = (ANImageView) findViewById(R.id.weatherImageView);

        user = ((GrifApp)getApplication()).getGrifAppService().getLastUser();
        login = ((GrifApp)getApplication()).getGrifAppService().getLastLogin();

        actualizaDashboard();

    }

    private void actualizaDashboard() {
        setupLocationUpdates();

        String url_weather = OpenWeatherMapApi.URL_OPENWEATHERMAP;

        url_weather = url_weather.replace("LONGITUD",String.valueOf(location.getLongitude()));
        url_weather = url_weather.replace("LATITUD",String.valueOf(location.getLatitude()));
        Log.d("Test", "============== >" + url_weather);
        // Obtenemos los datos del clima
        AndroidNetworking.get(url_weather)
                .setPriority(Priority.HIGH)
                .setTag("GrifApp")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultsArray = response.getJSONArray("weather");
                            weather = new Weather();
                            JSONObject json = resultsArray.getJSONObject(0);
                            weather.setId(json.optInt("id"));
                            weather.setMain(json.getString("main"));
                            weather.setDescription(json.getString("description"));
                            weather.setIcon(json.getString("icon"));

                            json = response.getJSONObject("main");

                            weather.setTemp(json.getString("temp"));
                            weather.setPressure(json.getString("pressure"));
                            weather.setHumidity(json.getString("humidity"));
                            weather.setTemp_min(json.getString("temp_min"));
                            weather.setTemp_max(json.getString("temp_max"));

                            json = response.getJSONObject("sys");

                            weather.setCountry(json.getString("country"));

                            weather.setName(response.getString("name"));

                            searchCountry(weather.getCountry());

                            ciyTextView.setText(weather.getName());
                            wheaterTextView.setText(weather.getDescription());
                            tempTextView.setText(weather.getTemp());
                            humidityTextView.setText(weather.getHumidity());
                            pressureTextView.setText(weather.getPressure());
                            tempMinTextView.setText(weather.getTemp_min());
                            tempMaxTextView.setText(weather.getTemp_max());
                            weatherImageView.setImageUrl(OpenWeatherMapApi.URL_ICON + weather.getIcon() + ".png");
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

        mHello.setText(getString(R.string.mHello) + " " + user.getName());
        mWelcome.setText(getString(R.string.mWelcome) + " " + getString(R.string.app_name));

        imageViewLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewMap();
            }
        });

        imageViewPromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewPromotions();
            }
        });
    }

    public void searchCountry(String country) {
        // Obtenemos los datos del clima
        AndroidNetworking.get(CountryApi.URL_COUNTRY + country)
                .setPriority(Priority.HIGH)
                .setTag("GrifApp")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject json = response.getJSONObject("translations");

                            weather.setCountryName(json.getString("es"));
                            countryTextView.setText(weather.getCountryName());
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

    public void onViewMap(){
        Intent intent = new Intent(this, FuelStationActivity.class);
        startActivity(intent);
    }

    public void onViewPromotions(){
        Intent intent = new Intent(this, PromotionsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.opcionEditarCuenta:
                intent = new Intent(this, UserUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.opcionCerrarSesion:
                login.delete();
                user.delete();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        actualizaDashboard();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void setupLocationUpdates() {
        validatePermissions();
        if (locationPermissionGranted) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(locationProvider);
            if (location == null){
                Toast.makeText(this, "Error al obtener datos del GPS", Toast.LENGTH_LONG).show();
            }else {
                refreshCurrentLocation(location);
            }
        }
    }

    private void validatePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_LOCATION);
            }
        } else {
            locationPermissionGranted = true;
        }
    }

    private void refreshCurrentLocation(Location location) {
        if (location == null) {
        }else{
            String locationDescription = "======================> Latitude: " + String.valueOf(location.getLatitude()) + " Longitude: " + String.valueOf(location.getLongitude());
            Log.d("GrifApp", locationDescription);

            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        }
    }
}