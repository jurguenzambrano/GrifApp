package pe.edu.upc.grifapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.User;

public class WelcomeActivity extends AppCompatActivity {

    TextView mHello;
    TextView mWelcome;
    ImageView imageViewPromotions;
    ImageView imageViewLocations;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mHello = (TextView) findViewById(R.id.mHello);
        mWelcome = (TextView) findViewById(R.id.mWelcome);
        imageViewPromotions = (ImageView) findViewById(R.id.imageViewPromotions);
        imageViewLocations = (ImageView) findViewById(R.id.imageViewLocations);

        user = GrifApp.getInstance().getCurrentUser();

        mHello.setText(getString(R.string.mHello) + " " + user.getName());
        mWelcome.setText(getString(R.string.mWelcome) + " " + getString(R.string.app_name));

        

    }
}
