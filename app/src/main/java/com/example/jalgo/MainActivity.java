package com.example.jalgo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {

    private MaterialButtonToggleGroup toggleFiltro;
    private MaterialButton btnTodo, btnSostenible, btnExplorar, btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleFiltro   = findViewById(R.id.toggleFiltro);
        btnTodo        = findViewById(R.id.btnTodo);
        btnSostenible  = findViewById(R.id.btnSostenible);
        btnExplorar    = findViewById(R.id.btnExplorar);
        btnMapa        = findViewById(R.id.btnMapa);
        TextView link  = findViewById(R.id.linkConsejos);

        // Preselección: "Todo"
        toggleFiltro.check(btnTodo.getId());

        btnExplorar.setOnClickListener(v -> {
            boolean soloSostenible = toggleFiltro.getCheckedButtonId() == btnSostenible.getId();
            Intent i = new Intent(this, ExplorarActivity.class);
            i.putExtra("solo_sostenible", soloSostenible);
            startActivity(i);
        });

        btnMapa.setOnClickListener(v -> {
            boolean soloSostenible = toggleFiltro.getCheckedButtonId() == btnSostenible.getId();
            Intent i = new Intent(this, MapaActivity.class);
            i.putExtra("solo_sostenible", soloSostenible);
            startActivity(i);
        });

        link.setOnClickListener(v -> mostrarDialogoConsejos());
    }

    private void mostrarDialogoConsejos() {
        new AlertDialog.Builder(this)
                .setTitle("Consejos sostenibles")
                .setMessage("• Lleva termo, evita desechables\n" +
                        "• Camina/bici/transporte público\n" +
                        "• Respeta flora/fauna y senderos\n" +
                        "• Evita horas pico para dispersar visitas")
                .setPositiveButton("Ok", null)
                .show();
    }
}
