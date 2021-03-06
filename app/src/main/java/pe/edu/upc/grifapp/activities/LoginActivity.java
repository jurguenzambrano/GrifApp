package pe.edu.upc.grifapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.grifapp.GrifApp;
import pe.edu.upc.grifapp.R;
import pe.edu.upc.grifapp.models.Login;
import pe.edu.upc.grifapp.models.Message;
import pe.edu.upc.grifapp.models.User;
import pe.edu.upc.grifapp.network.*;
import pe.edu.upc.grifapp.service.GrifAppService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextInputLayout textInputLayout;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout linearLayout;
    private static String TAG = "GrifApp";
    private Login login;
    private User user;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        // Valida si el usuario ya se encuentra conectado
        login = ((GrifApp)getApplication()).getGrifAppService().getLastLogin();

        if (login == null){
            linearLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);

            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            textInputLayout = (TextInputLayout) findViewById(R.id.tilInputPassword);
            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.password || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
        }else {
            onLoginSuccess();
        }

    }

    public void registrarUsuario(View v) {
        Intent intent = new Intent(this, UserRegisterActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    private void attemptLogin() {

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        //loginCorrecto = false;
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("email", email);
            loginJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(LoginApi.LOGIN_URL)
                .addJSONObjectBody(loginJson)
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getString("code").equals("0")) {
                                Snackbar.make(linearLayout, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            message = Message.build(response);
                            login = Login.build(response.getJSONObject("data"));

                            progressDialog.dismiss();
                            if (message.getCode().equals("0")) {
                                onLoginSuccess();
                            }else{
                                Snackbar.make(linearLayout, message.getMessage(), Snackbar.LENGTH_LONG).show();
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
                        Snackbar.make(linearLayout, messageError, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(linearLayout, "Usuario registrado", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void onLoginSuccess() {

        // Obtenemos los datos del usuario
        AndroidNetworking.get(UsersApi.USERS_URL + "/" + login.getId())
                .addHeaders("token",login.getToken())
                .setPriority(Priority.HIGH)
                .setTag(TAG)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getString("code").equals("0")) {
                                Snackbar.make(linearLayout, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            message = Message.build(response);
                            user = User.build(response.getJSONObject("data"));

                            if (message.getCode().equals("0")) {
                                login.save();
                                //User userFind = User.findById(user.getId());
                                user.update();
                                // Si todo esta ok, abrimos la pantalla de Bienvenida
                                Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
                                //intent.putExtra("token",login.getToken());
                                startActivity(intent);
                                finish();
                            }else{
                                Snackbar.make(linearLayout, message.getMessage(), Snackbar.LENGTH_LONG).show();
                                return;
                            }
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
                        Snackbar.make(linearLayout, messageError, Snackbar.LENGTH_LONG).show();
                        return;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refreshDashboard();
    }

}