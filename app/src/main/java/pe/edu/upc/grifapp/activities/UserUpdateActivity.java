package pe.edu.upc.grifapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.upc.grifapp.R;

public class UserUpdateActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellido;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        txtNombre = (EditText) findViewById(R.id.in_nombres);
        txtApellido = (EditText) findViewById(R.id.in_apellidos);
        //btnUpdate = (Button) findViewById(R.id.btn_update);
        /*
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
        */
    }
}
