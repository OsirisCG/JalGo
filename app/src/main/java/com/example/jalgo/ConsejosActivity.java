package com.example.jalgo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ConsejosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejos);

        ListView lista = findViewById(R.id.listaConsejos);
        ArrayList<String> datos = new ArrayList<>();

        try {
            InputStream is = getAssets().open("consejos.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                datos.add("• " + o.getString("titulo") + "\n" + o.getString("detalle"));
            }

        } catch (Exception e) {
            datos.add("Error al cargar consejos: " + e.getMessage());
        }

        // Adaptador con ícono y estilo visual
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_activated_1, datos) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_info_details, 0, 0, 0);
                view.setCompoundDrawablePadding(16);
                view.setTextColor(ContextCompat.getColor(ConsejosActivity.this, R.color.brand_on));
                view.setTextSize(16);
                return view;
            }
        };

        lista.setAdapter(adaptador);
    }
}
