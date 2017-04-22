package pe.edu.upc.grifapp.activities;

import android.app.ProgressDialog;
import android.renderscript.RenderScript;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.UnsupportedEncodingException;

import javax.xml.transform.Source;

import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.UsersApi;

public class RegisterActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_register);

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
/*
        if (txtNombre.getText().toString().isEmpty()){
            txtNombre.setError(getString(R.string.error_field_required));
            txtNombre.requestFocus();
            return;
        }

        if (txtApellido.getText().toString().isEmpty()){
            txtApellido.setError(getString(R.string.error_field_required));
            txtApellido.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            txtEmail.requestFocus();
            return;
        } else if (!isEmailValid(email)) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            txtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.error_field_required));
            txtPassword.requestFocus();
            return;
        }
*/
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,R.style.AppTheme_Dark_Dialog);
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
                            if(response.getString("status").equalsIgnoreCase("error")) {
                                Log.d(TAG, response.getString("message"));
                                return;
                            }

                            login = User.build(response);

                            if (login.getCode().equals("0")) {
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
                        Log.d(TAG, anError.getLocalizedMessage());
                        progressDialog.dismiss();
                    }
                });
        /*
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("Apellidos", txtApellido.getText().toString());
            jsonParams.put("Celular", txtCelular.getText().toString());
            jsonParams.put("Direccion", txtDireccion.getText().toString());
            jsonParams.put("Dni", txtDni.getText().toString());
            jsonParams.put("Mail", txtEmail.getText().toString());
            jsonParams.put("Nombres", txtNombre.getText().toString());
            jsonParams.put("Clave", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        UsuarioRestClient.post(RegisterActivity.this, "usuarios", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                todoOk();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String mensaje = new String(responseBody, "UTF-8");
                    txtDni.requestFocus();
                    Snackbar.make(coordinatorLayout, mensaje, Snackbar.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        });
        */
    }

    private void maximoRegistro(){
        Snackbar.make(coordinatorLayout, "Se superó el máximo de cuentas por DNI (3)", Snackbar.LENGTH_LONG).show();
    }

    private void errorConexionRegistro(){
        Snackbar.make(coordinatorLayout, "Error al realizar conexión", Snackbar.LENGTH_LONG).show();
    }

    private void todoOk(){
        setResult(RESULT_OK);
        finish();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
