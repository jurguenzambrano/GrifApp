package pe.edu.upc.grifapp.activities;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.UsersApi;

public class UserUpdateActivity extends AppCompatActivity {

    private static String TAG = "GrifApp";

    private EditText txtName;
    private EditText txtLastName;
    private Button btnUpdate;
    private User user;
    private Login login;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        user = ((GrifApp)getApplication()).getGrifAppService().getLastUser();
        login = ((GrifApp)getApplication()).getGrifAppService().getLastLogin();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutUpdate);

        txtName = (EditText) findViewById(R.id.in_name);
        txtLastName = (EditText) findViewById(R.id.in_last_name);
        txtName.setText(user.getName());
        txtLastName.setText(user.getLastName());
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    public void updateUser() {
        // update date of user
        final String name = txtName.getText().toString();
        final String lastName = txtLastName.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(
                UserUpdateActivity.this,
                R.style.AppTheme_Dark_Dialog
        );

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Actualizando usuario...");
        progressDialog.show();

        JSONObject dataUpdateUserJson = new JSONObject();
        try {
            dataUpdateUserJson.put("name", txtName.getText().toString());
            dataUpdateUserJson.put("last_name", txtLastName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.put(UsersApi.USERS_URL +"/"+ user.getId())
                .addHeaders("token",login.getToken())
                .addJSONObjectBody(dataUpdateUserJson)
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = User.build(response.optJSONObject("data"));
                            user.update();
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout, response.getString("message"), Snackbar.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        String messageError = "Error en aplicativo";
                        try {
                            JSONObject jsonBody = new JSONObject(anError.getErrorBody());
                            messageError = jsonBody.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Snackbar.make(coordinatorLayout, messageError, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                });
    }
}
