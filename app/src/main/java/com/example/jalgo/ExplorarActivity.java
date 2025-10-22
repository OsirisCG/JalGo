package com.example.jalgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExplorarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_explorar);

        String filtro = getIntent().getStringExtra("filtro"); // "todo" | "sostenible"
        if (filtro == null) filtro = "todo";

        LinearLayout lista = findViewById(R.id.lista);

        // 1) Cargar JSON
        JSONArray arr = loadArrayFromAssets("lugares_tlaquepaque.json");
        if (arr.length() == 0) { lista.addView(msg("No se pudo leer lugares_tlaquepaque.json")); return; }

        // 2) Agrupar por municipio con orden alfabético
        Map<String, List<JSONObject>> grupos = new TreeMap<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);

                // filtro sostenible
                String s = o.optString("sostenibilidad","").toLowerCase();
                boolean esSostenible = s.contains("sostenible");
                if ("sostenible".equals(filtro) && !esSostenible) continue;

                String muni = o.optString("municipio","").trim();
                if (muni.isEmpty()) muni = "Sin municipio";

                List<JSONObject> listaMuni = grupos.get(muni);
                if (listaMuni == null) {
                    listaMuni = new ArrayList<>();
                    grupos.put(muni, listaMuni);
                }
                listaMuni.add(o);

            } catch (Exception ignored) {}
        }

        // 3) Pintar: banner por municipio + tarjetas
        if (grupos.isEmpty()) { lista.addView(msg("Sin resultados con el filtro seleccionado")); return; }

        for (Map.Entry<String, List<JSONObject>> entry : grupos.entrySet()) {
            lista.addView(makeSection(entry.getKey()));           // banner "Municipio: X"
            for (JSONObject o : entry.getValue()) {
                lista.addView(makePreviewCard(o));                // card preview clickeable
            }
        }
    }

    /* ---------- Helpers UI ---------- */

    private View makeSection(String titulo){
        TextView v = new TextView(this);
        v.setText("Municipio: " + titulo);
        v.setTextSize(14);
        v.setTextColor(ContextCompat.getColor(this, R.color.brand_on));
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.brand_surface));
        int p = dp(10);
        v.setPadding(p,p,p,p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(16);
        v.setLayoutParams(lp);
        v.setElevation(dp(1));
        return v;
    }

    private View makePreviewCard(JSONObject o){
        MaterialCardView card = new MaterialCardView(this);
        card.setRadius(dp(16));
        card.setCardElevation(dp(2));
        card.setStrokeWidth(dp(1));
        card.setStrokeColor(ContextCompat.getColor(this, R.color.brand_stroke));
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.brand_surface));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(8);
        card.setLayoutParams(lp);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(12);
        box.setPadding(pad,pad,pad,pad);

        // imagen preview fija
        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(160)));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String nombreImg = o.optString("imagen","").replace(".webp","");
        if (!nombreImg.isEmpty()) {
            int resId = getResources().getIdentifier(nombreImg, "drawable", getPackageName());
            if (resId != 0) img.setImageResource(resId);
            else img.setBackgroundColor(ContextCompat.getColor(this, R.color.brand_stroke));
        } else {
            img.setBackgroundColor(ContextCompat.getColor(this, R.color.brand_stroke));
        }

        TextView tNombre = tv(o.optString("nombre",""), 16, R.color.brand_on, 0, dp(8), 0, 0);
        TextView tTipo   = tv(o.optString("tipo",""),   14, R.color.brand_subtle, 0, dp(2), 0, 0);

        box.addView(img);
        box.addView(tNombre);
        box.addView(tTipo);
        card.addView(box);

        card.setOnClickListener(v -> {
            Intent i = new Intent(ExplorarActivity.this, DetalleActivity.class);
            i.putExtra("lugar_json", o.toString());
            startActivity(i);
        });

        return card;
    }

    private TextView tv(String txt, int sp, int colorRes, int l,int t,int r,int b){
        TextView v = new TextView(this);
        v.setText(txt);
        v.setTextSize(sp);
        v.setTextColor(ContextCompat.getColor(this, colorRes));
        v.setPadding(l,t,r,b);
        return v;
    }

    private View msg(String m){
        return tv(m, 14, R.color.brand_subtle, dp(12), dp(12), dp(12), dp(12));
    }

    private int dp(int v){
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    /* ---------- Helpers JSON ---------- */

    private JSONArray loadArrayFromAssets(String filename) {
        try (InputStream is = getAssets().open(filename)) {
            byte[] buf = new byte[is.available()];
            is.read(buf);
            String raw = new String(buf, StandardCharsets.UTF_8)
                    .replace("\uFEFF","").trim(); // quita BOM
            if (!raw.startsWith("[")) throw new org.json.JSONException("JSON no es un array");
            return new JSONArray(raw);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
