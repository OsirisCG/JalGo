package com.example.jalgo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_register);
        db = new DBHelper(this);

        EditText inName  = findViewById(R.id.inName);
        EditText inUser  = findViewById(R.id.inUser);
        EditText inPass  = findViewById(R.id.inPass);
        Button btnReg    = findViewById(R.id.btnRegister);

        btnReg.setOnClickListener(v -> {
            String name = inName.getText().toString().trim();
            String user = inUser.getText().toString().trim();
            String pass = inPass.getText().toString();

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.register(name, user, pass);
            if (ok){
                Toast.makeText(this, "Cuenta creada. Inicia sesión.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ese usuario ya existe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
