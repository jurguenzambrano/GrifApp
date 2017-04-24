package pe.edu.upc.grifapp.activities;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.UsersApi;

public class UserRegisterActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSignup;
    private CoordinatorLayout coordinatorLayout;
    private static String TAG = "GrifApp";
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutRegister);

        txtNombre = (EditText) findViewById(R.id.in_nombres);
        txtApellido = (EditText) findViewById(R.id.in_apellidos);
        txtEmail = (EditText) findViewById(R.id.in_email);
        txtPassword = (EditText) findViewById(R.id.in_password);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        txtNombre.setError(null);
        txtEmail.setError(null);

        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(UserRegisterActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        JSONObject userJson = new JSONObject();
        try {
            userJson.put("last_name", txtApellido.getText().toString());
            userJson.put("email", txtEmail.getText().toString());
            userJson.put("name", txtNombre.getText().toString());
            userJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(UsersApi.USERS_URL)
                .addJSONObjectBody(userJson)
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getString("code").equals("0")) {
                                Snackbar.make(coordinatorLayout, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            login = User.build(response);

                            if (login.getCode().equals("0")) {
                                progressDialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }else{
                                Snackbar.make(coordinatorLayout, login.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                            // sourcesAdapter.setSources(sources);
                            // sourcesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        progressDialog.dismiss();
                        Snackbar.make(coordinatorLayout, messageError, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void todoOk(){
        setResult(RESULT_OK);
        finish();
    }

}
