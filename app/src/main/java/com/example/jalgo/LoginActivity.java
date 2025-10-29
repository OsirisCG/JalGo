package com.example.jalgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    DBHelper db;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_login);
        db = new DBHelper(this);

        EditText inUser = findViewById(R.id.inUser);
        EditText inPass  = findViewById(R.id.inPass);
        Button btnLogin  = findViewById(R.id.btnLogin);
        Button btnGoReg  = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> {
            long uid = db.login(inUser.getText().toString(), inPass.getText().toString());
            if (uid > 0){
                saveSession(uid);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Correo/contraseña inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoReg.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void saveSession(long id){
        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        sp.edit().putLong("user_id", id).apply();
    }
}
