package com.example.jalgo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private String filtro = "todo";
    private boolean navegando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTodo = findViewById(R.id.btnTodo);
        Button btnSostenible = findViewById(R.id.btnSostenible);
        Button btnExplorar = findViewById(R.id.btnExplorar);
        Button btnMapa = findViewById(R.id.btnMapa);
        TextView linkConsejos = findViewById(R.id.linkConsejos);
        MaterialCardView cardConsejos = findViewById(R.id.cardConsejos);

        int colorActivo = ContextCompat.getColor(this, R.color.brand_primary);
        int colorInactivo = ContextCompat.getColor(this, R.color.brand_surface);
        int textoActivo = ContextCompat.getColor(this, R.color.brand_on);
        int textoInactivo = ContextCompat.getColor(this, R.color.brand_subtle);

        btnTodo.setBackgroundColor(colorActivo);
        btnTodo.setTextColor(textoActivo);
        btnSostenible.setBackgroundColor(colorInactivo);
        btnSostenible.setTextColor(textoInactivo);

        // Botón "Todo"
        btnTodo.setOnClickListener(v -> {
            filtro = "todo";
            btnTodo.setBackgroundColor(colorActivo);
            btnTodo.setTextColor(textoActivo);
            btnSostenible.setBackgroundColor(colorInactivo);
            btnSostenible.setTextColor(textoInactivo);
            Toast.makeText(this, "Modo: Todos los lugares", Toast.LENGTH_SHORT).show();
        });

        // Botón "Sostenible"
        btnSostenible.setOnClickListener(v -> {
            filtro = "sostenible";
            btnSostenible.setBackgroundColor(colorActivo);
            btnSostenible.setTextColor(textoActivo);
            btnTodo.setBackgroundColor(colorInactivo);
            btnTodo.setTextColor(textoInactivo);
            Toast.makeText(this, "Modo: Lugares sostenibles", Toast.LENGTH_SHORT).show();
        });

        // Botón "Explorar"
        btnExplorar.setOnClickListener(v -> {
            navegando = true;
            Intent intent = new Intent(MainActivity.this, ExplorarActivity.class);
            intent.putExtra("filtro", filtro);
            startActivity(intent);
            Toast.makeText(this, "Abriendo modo: " + filtro, Toast.LENGTH_SHORT).show();
        });

        // Botón "Mapa"
        btnMapa.setOnClickListener(v -> {
            navegando = true;
            Intent i = new Intent(MainActivity.this, MapaActivity.class);
            i.putExtra("filtro", filtro);
            startActivity(i);
            Toast.makeText(this, "Mostrando mapa de " + filtro, Toast.LENGTH_SHORT).show();
        });

        // Consejos
        linkConsejos.setOnClickListener(v -> {
            navegando = true;
            startActivity(new Intent(MainActivity.this, ConsejosActivity.class));
        });

        cardConsejos.setOnClickListener(v -> {
            navegando = true;
            startActivity(new Intent(MainActivity.this, ConsejosActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navegando = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!navegando) {
            getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply();
        }
    }
}
