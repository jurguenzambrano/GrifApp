package pe.edu.upc.grifapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Fuel;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.service.GrifAppService;

public class WelcomeActivity extends AppCompatActivity {

    private TextView mHello;
    private TextView mWelcome;
    private ImageView imageViewPromotions;
    private ImageView imageViewLocations;
    private User user;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mHello = (TextView) findViewById(R.id.mHello);
        mWelcome = (TextView) findViewById(R.id.mWelcome);
        imageViewPromotions = (ImageView) findViewById(R.id.imageViewPromotions);
        imageViewLocations = (ImageView) findViewById(R.id.imageViewLocations);

        user = ((GrifApp)getApplication()).getGrifAppService().getLastUser();
        login = ((GrifApp)getApplication()).getGrifAppService().getLastLogin();

        actualizaDashboard();

    }

    private void actualizaDashboard() {
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
}
