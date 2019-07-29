package com.example.swii_proyectouber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Driver;

public class DriverRegisterActivity extends AppCompatActivity {

    private EditText mnombreCompleto, mEmail, mPassword, mcedula, mtelefono;
    private Button mRegistration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(DriverRegisterActivity.this, DriverMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mnombreCompleto = (EditText) findViewById(R.id.nombreCompleto);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mcedula = (EditText) findViewById(R.id.cedula);
        mtelefono = (EditText) findViewById(R.id.telefono);

        mRegistration = (Button) findViewById(R.id.registration);

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombreCompleto = mnombreCompleto.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String cedula = mcedula.getText().toString();
                final String telefono = mtelefono.getText().toString();


                if (nombreCompleto.isEmpty()) {
                    Toast.makeText(DriverRegisterActivity.this, "Campo nombre vacío", Toast.LENGTH_LONG).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(DriverRegisterActivity.this, "Campo email vacío", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(DriverRegisterActivity.this, "Campo password vacío", Toast.LENGTH_LONG).show();
                } else if (cedula.isEmpty()) {
                    Toast.makeText(DriverRegisterActivity.this, "Campo cédula vacío", Toast.LENGTH_LONG).show();
                } else if (telefono.isEmpty()) {
                    Toast.makeText(DriverRegisterActivity.this, "Campo teléfono vacío", Toast.LENGTH_LONG).show();
                } else if (cedula.length() != 10) {
                    Toast.makeText(DriverRegisterActivity.this, "Cédula no valida", Toast.LENGTH_LONG).show();
                } else {
                    Boolean validacion = valida(cedula);
                    if (!validacion) {
                        Toast.makeText(DriverRegisterActivity.this, "Cédula no valida", Toast.LENGTH_LONG).show();
                    } else {
                        //Conexión a la base de datos para registrarse
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(DriverRegisterActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                                } else {
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                                    current_user_db.setValue(true);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public static boolean valida(String x) {
        int suma = 0;
        if (x.length() != 10) {
            return false;
        } else {
            int a[] = new int[x.length() / 2];
            int b[] = new int[(x.length() / 2)];
            int c = 0;
            int d = 1;
            for (int i = 0; i < x.length() / 2; i++) {
                a[i] = Integer.parseInt(String.valueOf(x.charAt(c)));
                c = c + 2;
                if (i < (x.length() / 2) - 1) {
                    b[i] = Integer.parseInt(String.valueOf(x.charAt(d)));
                    d = d + 2;
                }
            }

            for (int i = 0; i < a.length; i++) {
                a[i] = a[i] * 2;
                if (a[i] > 9) {
                    a[i] = a[i] - 9;
                }
                suma = suma + a[i] + b[i];
            }
            int aux = suma / 10;
            int dec = (aux + 1) * 10;
            if ((dec - suma) == Integer.parseInt(String.valueOf(x.charAt(x.length() - 1))))
                return true;
            else if (suma % 10 == 0 && x.charAt(x.length() - 1) == '0') {
                return true;
            } else {
                return false;
            }

        }
    }
}