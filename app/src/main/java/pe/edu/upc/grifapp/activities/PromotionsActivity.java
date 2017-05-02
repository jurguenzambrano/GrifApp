package pe.edu.upc.grifapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.adapters.PromotionsAdapter;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.Promotion;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.PromotionsApi;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class PromotionsActivity extends AppCompatActivity {

    private User user;
    private Login login;
    private RecyclerView promotionsRecyclerView;
    private PromotionsAdapter promotionsAdapter;
    private RecyclerView.LayoutManager  articlesLayoutManager;
    private List<Promotion> promotions;
    //Source                      source;
    int                         spanCount;
    private static String       TAG = "GrifApp";
    private SwipeRefreshLayout sfiIndicadorRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        sfiIndicadorRefresh = (SwipeRefreshLayout) findViewById(R.id.sfiIndicadorRefresh);
        sfiIndicadorRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listPromotions();
            }
        });

        promotionsRecyclerView = (RecyclerView) findViewById(R.id.promotionsRecyclerView);
        promotionsAdapter = new PromotionsAdapter();
        promotions = new ArrayList<>();
        promotionsAdapter.setPromotions(promotions);
        spanCount =
                getResources().getConfiguration().orientation ==
                        ORIENTATION_LANDSCAPE ? 3 : 2;
        articlesLayoutManager = new GridLayoutManager(this, spanCount);
        promotionsRecyclerView.setAdapter(promotionsAdapter);
        promotionsRecyclerView.setLayoutManager(articlesLayoutManager);

        listPromotions();
    }

    private void listPromotions() {
        AndroidNetworking.get(PromotionsApi.PROMOTIONS_URL)
                .addHeaders("token",login.getToken())
                .addHeaders("id",user.getId())
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        promotions = Promotion.build(response);
                        promotionsAdapter.setPromotions(promotions);
                        promotionsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getLocalizedMessage());

                    }
                });
    }
}
