package pe.edu.upc.grifapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Promotion;

public class PromotionActivity extends AppCompatActivity {

    private ANImageView pictureANImageView;
    private TextView titleTextView;
    private TextView    descriptionTextView;
    private Promotion promotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        promotion = GrifApp.getInstance().getCurrentPromotion();
        titleTextView = (TextView) findViewById(R.id.nameTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        pictureANImageView = (ANImageView) findViewById(R.id.pictureANImageView);
        pictureANImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        pictureANImageView.setErrorImageResId(R.mipmap.ic_launcher);
        pictureANImageView.setImageUrl(promotion.getPathImage());
        titleTextView.setText(promotion.getName());
        descriptionTextView.setText(promotion.getDescription());
    }

}
