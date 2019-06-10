package com.example.tunrax.materialdesigntest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VerReporte extends AppCompatActivity {

    private TextView lblTitulo, lblDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reporte);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        Bundle bundle = getIntent().getExtras();
        String titulo= bundle.getString("titulo");
        String descripcion= bundle.getString("descripcion");

        lblTitulo.setText(titulo);
        lblDescripcion.setText(descripcion);
    }
}
