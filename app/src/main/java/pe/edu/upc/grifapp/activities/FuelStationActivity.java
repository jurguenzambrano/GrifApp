package pe.edu.upc.grifapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.FuelStationApi;

public class FuelStationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static String TAG = "GrifApp";
    private User user;
    private Login login;
    List<Fuel> fuels;

    final static int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private String locationProvider = LocationManager.GPS_PROVIDER;
    private boolean locationPermissionGranted = false;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private GoogleMap map;
    private Marker mCurrLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        locationListener = this;
        fuels = new ArrayList<Fuel>();

        user = ((GrifApp)getApplication()).getGrifAppService().getLastUser();
        login = ((GrifApp)getApplication()).getGrifAppService().getLastLogin();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
                    //removeLocationUpdates(mGoogleApiClient, locationListener);
        }
        */
    }

    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        final ProgressDialog progressDialog = new ProgressDialog(FuelStationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Obteniendo estaciones de servicio...");
        progressDialog.show();

        // Obtenemos todas las estaciones de servicio
        AndroidNetworking.get(FuelStationApi.FUEL_URL)
                .addHeaders("token",login.getToken())
                .addHeaders("id",user.getId().toString())
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
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_grifapp))
                                    .title(fuels.get(i).getName()));
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        String messageError = "Error en aplicativo";
                        progressDialog.dismiss();
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
        setupLocationUpdates();
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
            Log.d(TAG, locationDescription);

            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_position));
            mCurrLocationMarker = map.addMarker(markerOptions);
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(currentLocation)
                    .zoom(16)
                    .build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        refreshCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCurrentLocation(location);
    }

}
