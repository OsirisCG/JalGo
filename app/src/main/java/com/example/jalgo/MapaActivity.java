package com.example.jalgo;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        WebView web = findViewById(R.id.webMap);
        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);

        String filtro = getIntent().getStringExtra("filtro");
        if (filtro == null) filtro = "todo";

        JSONArray data = loadArray("lugares_tlaquepaque.json");

        StringBuilder markers = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject o = data.getJSONObject(i);
                if (!o.has("lat") || !o.has("lng")) continue;

                String s = o.optString("sostenibilidad", "").toLowerCase();
                boolean sost = s.contains("sostenible");

                if ("sostenible".equals(filtro) && !sost) continue;

                if (!first) markers.append(",");
                String nombre = o.optString("nombre", "Lugar").replace("\"", "'");
                markers.append("{\"nombre\":\"").append(nombre)
                        .append("\",\"lat\":").append(o.getDouble("lat"))
                        .append(",\"lng\":").append(o.getDouble("lng"))
                        .append(",\"sost\":").append(sost).append("}");
                first = false;
            } catch (Exception ignored) {}
        }
        markers.append("]");

        String html = "<!doctype html><html><head>"
                + "<meta name='viewport' content='width=device-width,initial-scale=1'/>"
                + "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>"
                + "<style>html,body,#map{height:100%;margin:0} .popup{font:14px/1.3 sans-serif}</style>"
                + "</head><body><div id='map'></div>"
                + "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>"
                + "<script>const markers=" + markers + ";"
                + "const map=L.map('map');"
                + "let center=[20.6736,-103.344]; if(markers.length){center=[markers[0].lat,markers[0].lng];}"
                + "map.setView(center, 13);"
                + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{maxZoom:19}).addTo(map);"
                + "function iconFor(sost){"
                + " const base='https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/';"
                + " const url=sost?base+'marker-icon-2x-green.png':base+'marker-icon-2x-grey.png';"
                + " return L.icon({"
                + "   iconUrl:url, shadowUrl:base+'marker-shadow.png',"
                + "   iconSize:[25,41], iconAnchor:[12,41], popupAnchor:[1,-34], shadowSize:[41,41]"
                + " });"
                + "}"
                + "const group=[];"
                + "markers.forEach(m=>{"
                + " const mk=L.marker([m.lat,m.lng],{icon:iconFor(m.sost)}).addTo(map);"
                + " mk.bindPopup(`<div class='popup'><b>${m.nombre}</b>${m.sost?' <span style=\\'color:#22c55e\\'>(sostenible)</span>':''}</div>`);"
                + " group.push(mk);"
                + "});"
                + "if(group.length){const g=L.featureGroup(group);map.fitBounds(g.getBounds(),{padding:[20,20]});}"
                + "</script></body></html>";

        web.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    private JSONArray loadArray(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            return new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
