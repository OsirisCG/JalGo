package com.example.jalgo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class DetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_detalle);

        ImageView imgHeader = findViewById(R.id.imgHeader);
        ImageView imgExtra1 = findViewById(R.id.imgExtra1);
        ImageView imgExtra2 = findViewById(R.id.imgExtra2);

        TextView  txtNombre = findViewById(R.id.txtNombre);
        TextView  txtTipo   = findViewById(R.id.txtTipo);
        TextView  txtDir    = findViewById(R.id.txtDireccion);
        TextView  txtHor    = findViewById(R.id.txtHorario);
        TextView  txtRec    = findViewById(R.id.txtRecom);

        String raw = getIntent().getStringExtra("lugar_json");
        if (raw == null) { finish(); return; }

        try {
            JSONObject o = new JSONObject(raw.replace("\uFEFF","").trim());

            // Texto
            String nombre = o.optString("nombre","");
            setTitle(nombre.isEmpty() ? "Detalle" : nombre);
            txtNombre.setText(nombre);
            txtTipo.setText(o.optString("tipo",""));
            txtDir.setText(o.optString("direccion",""));
            String hor = o.optString("horario","");
            txtHor.setText(hor.isEmpty() ? "" : "Horario: " + hor);
            txtRec.setText(o.optString("recomendaciones",""));

            // Imagen principal (usa campo "imagen" si existe)
            String base = o.optString("imagen","").replace(".webp","").trim();
            if (base.isEmpty()) {
                // fallback: nombre del lugar normalizado
                base = nombre.toLowerCase()
                        .replace("á","a").replace("é","e").replace("í","i").replace("ó","o").replace("ú","u")
                        .replace("ñ","n").replaceAll("[^a-z0-9]+","_")
                        .replaceAll("^_|_$","");
            }

            // Carga header
            int idHeader = getResId(base);
            if (idHeader != 0) imgHeader.setImageResource(idHeader);

            // Carga extras: base_1, base_2 si existen; si no, oculta
            int id1 = getResId(base + "_1");
            int id2 = getResId(base + "_2");

            if (id1 != 0) { imgExtra1.setImageResource(id1); imgExtra1.setVisibility(View.VISIBLE); }
            else { imgExtra1.setVisibility(View.GONE); }

            if (id2 != 0) { imgExtra2.setImageResource(id2); imgExtra2.setVisibility(View.VISIBLE); }
            else { imgExtra2.setVisibility(View.GONE); }

            // Si ambas faltan, oculta el contenedor horizontal
            if (id1 == 0 && id2 == 0) {
                View layoutFotos = findViewById(R.id.layoutFotos);
                layoutFotos.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private int getResId(String name){
        if (name == null || name.isEmpty()) return 0;
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }
}
