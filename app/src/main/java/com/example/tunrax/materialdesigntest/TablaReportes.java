package com.example.tunrax.materialdesigntest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TablaReportes extends AppCompatActivity {

    private TableLayout tblReportes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_reportes);
        final ArrayList<Reportes> listaReportes = new ArrayList<Reportes>();

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.

        tblReportes = (TableLayout) findViewById(R.id.tblReportes);

        final TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params3=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow row = new TableRow(TablaReportes.this);
        params1.setMargins(12,20,0,0);
        params2.setMargins(12,30,0,0);
        params3.setMargins(5,30,160,20);

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                final String resultadoReportes = Reportes(id);
                try {
                    JSONObject jsonObjectReporte = new JSONObject(resultadoReportes);
                    JSONArray jsonArrayReporte = jsonObjectReporte.getJSONArray("reportes");
                    for (int x = 0; x < jsonArrayReporte.length(); x++) {
                        listaReportes.add(new Reportes(jsonArrayReporte.getJSONObject(x)));
                    }

                } catch (Exception e) {
                    Log.e("app", "exception", e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < listaReportes.size(); i++) {
                                final TableRow row = new TableRow(TablaReportes.this);
                                final TextView lblTitulo = new TextView(TablaReportes.this);
                                final String titulo = listaReportes.get(i).getTitulo();
                                final String descripcion = listaReportes.get(i).getDescripcion();
                                final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);
                                final ImageView btnVer = new ImageButton(TablaReportes.this);
                                btnVer.setImageResource(R.drawable.eyecloseup);
                                btnVer.setTag(R.drawable.eyecloseup);
                                btnVer.setLayoutParams(params3);
                                btnVer.setBackgroundColor(Color.TRANSPARENT);
                                btnVer.setTag(i);
                                btnVer.setId(i);
                                btnVer.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          Intent i = new Intent(getApplicationContext(), VerReporte.class);
                                          i.putExtra("titulo", titulo);
                                          i.putExtra("descripcion", descripcion);
                                          startActivity(i);
                                      }
                                  });

                                lblTitulo.setText(titulo);
                                lblTitulo.setLayoutParams(params2);
                                lblTitulo.setTextSize(16);
                                lblTitulo.setTextColor(Color.parseColor("#212121"));
                                lblTitulo.setTypeface(custom_font);

                                row.addView(lblTitulo);
                                row.addView(btnVer);
                                tblReportes.addView(row,i);
                            }

                        } catch (Exception e) {
                            Log.e("app", "exception", e);
                        }
                    }
                });

            }
        };
        thread1.start();
    }



    public String Reportes(int id) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("http://84361097.ngrok.io/obtenerReportes.php?idUsuario=" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();

            resultado = new StringBuilder();

            if (respuesta == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((linea = reader.readLine()) != null) {
                    resultado.append(linea);
                }
            }

        } catch (Exception e) {
        }

        return resultado.toString();
    }
}
