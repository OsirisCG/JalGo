package com.example.jalgo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private String filtro = "todo"; // valor inicial

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

        // Colores base
        int colorActivo = ContextCompat.getColor(this, R.color.brand_primary);
        int colorInactivo = ContextCompat.getColor(this, R.color.brand_surface);
        int textoActivo = ContextCompat.getColor(this, R.color.brand_on);
        int textoInactivo = ContextCompat.getColor(this, R.color.brand_subtle);

        // Estado inicial
        btnTodo.setBackgroundColor(colorActivo);
        btnTodo.setTextColor(textoActivo);
        btnSostenible.setBackgroundColor(colorInactivo);
        btnSostenible.setTextColor(textoInactivo);

        // Listener "Todo"
        btnTodo.setOnClickListener(v -> {
            filtro = "todo";
            btnTodo.setBackgroundColor(colorActivo);
            btnTodo.setTextColor(textoActivo);
            btnSostenible.setBackgroundColor(colorInactivo);
            btnSostenible.setTextColor(textoInactivo);
        });

        // Listener "Sostenible"
        btnSostenible.setOnClickListener(v -> {
            filtro = "sostenible";
            btnSostenible.setBackgroundColor(colorActivo);
            btnSostenible.setTextColor(textoActivo);
            btnTodo.setBackgroundColor(colorInactivo);
            btnTodo.setTextColor(textoInactivo);
        });

        // Listener "Explorar"
        btnExplorar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExplorarActivity.class);
            intent.putExtra("filtro", filtro);
            startActivity(intent);
        });

        // Listener "Mapa"
        btnMapa.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, MapaActivity.class);
            i.putExtra("filtro", filtro); // "todo" o "sostenible"
            startActivity(i);
        });


        // Listener "Consejos sostenibles"
        linkConsejos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConsejosActivity.class);
            startActivity(intent);
        });

        // También hace clic al tocar toda la tarjeta
        cardConsejos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConsejosActivity.class);
            startActivity(intent);
        });
    }
}
