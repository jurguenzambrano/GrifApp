package pe.edu.upc.grifapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

    final static int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private String locationProvider = LocationManager.GPS_PROVIDER;
    private boolean locationPermissionGranted = false;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station);

        fuels = new ArrayList<Fuel>();

        user = GrifApp.getInstance().getCurrentUser();
        login = GrifApp.getInstance().getCurrentLogin();

        setupLocationUpdates();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        // Obtenemos todas las estaciones de servicio
        AndroidNetworking.get(FuelStationApi.FUEL_URL)
                .addHeaders("token",login.getToken())
                .addHeaders("id",user.getId())
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
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

    private void setupLocationUpdates() {
        validatePermissions();
        if (locationPermissionGranted) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    refreshCurrentLocation(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(locationProvider);
            refreshCurrentLocation(location);
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
        String locationDescription = "======================> Latitude: " + String.valueOf(location.getLatitude()) + " Longitude: " + String.valueOf(location.getLongitude());
        Log.d(TAG, locationDescription);

        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(currentLocation)
                .zoom(16)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

}
