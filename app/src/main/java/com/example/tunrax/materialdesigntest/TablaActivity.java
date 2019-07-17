package com.example.tunrax.materialdesigntest;

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

/**
 *Esta activity tiene los datos de la ultimas visitas que hiso el usuario recientemente (TODO)
 * podra evaluar al proveedor con un like (TODO)
 * y reportarlo si hiso algo indevido (TODO)
 */
public class TablaActivity extends AppCompatActivity {

   // private ImageButton btnReporte;
    private TableLayout tblTablaUVisitados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla);
        final ArrayList<Proveedor> listaProveedor = new ArrayList<Proveedor>();

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.

        tblTablaUVisitados = (TableLayout) findViewById(R.id.tblTablaUVisitados);
       // btnReporte = (ImageButton) findViewById(R.id.btnReporte);

        final TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params3=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow row = new TableRow(TablaActivity.this);
        params1.setMargins(12,20,0,0);
        params2.setMargins(12,30,0,0);
        params3.setMargins(5,30,5,0);


        Thread thread1 = new Thread() {
            @Override
            public void run() {
                final String resultadoProveedores = Proveedores(id);
                try {
                    JSONObject jsonObjectProveedor = new JSONObject(resultadoProveedores);
                    JSONArray jsonArrayProveedor = jsonObjectProveedor.getJSONArray("proveedores");
                    for (int x = 0; x < jsonArrayProveedor.length(); x++) {
                        listaProveedor.add(new Proveedor(jsonArrayProveedor.getJSONObject(x)));
                    }

                } catch (Exception e) {
                    Log.e("app", "exception", e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            for (int i = 0; i < listaProveedor.size(); i++) {
                                TableRow row = new TableRow(TablaActivity.this);
                                final ArrayList<Calificacion> listaCalificacion = new ArrayList<Calificacion>();
                                TextView lblNombreLeñador = new TextView(TablaActivity.this);
                                TextView lblDireccionLeñador = new TextView(TablaActivity.this);
                                Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(lp);
                                final int idProveedor = listaProveedor.get(i).getId();
                                final int idUsuario = id;
                                final String NombreLeñador = listaProveedor.get(i).getNombre();
                                final String DireccionLeñador = listaProveedor.get(i).getDireccion();
                                final ImageView btnLike = new ImageButton(TablaActivity.this);
                                btnLike.setImageResource(R.drawable.likewhitemini);
                                btnLike.setTag(R.drawable.likewhitemini);
                                btnLike.setLayoutParams(lp);
                                btnLike.setOnClickListener(ClickListener);
                                btnLike.setBackgroundColor(Color.TRANSPARENT);
                                btnLike.setTag(i);
                                btnLike.setId(i);
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            String calificacionVerificada = verificarCalificacion(idUsuario, idProveedor);
                                            JSONObject jsonObjectCalificacion = new JSONObject(calificacionVerificada);
                                            if (jsonObjectCalificacion.isNull("calificacion")) {

                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        btnLike.setImageResource(R.drawable.likeblackmini);
                                                        btnLike.setTag(R.drawable.likeblackmini);
                                                    }
                                                });
                                            }

                                        } catch (Exception e) {
                                            Log.e("app", "exception", e);
                                        }
                                    }
                                };
                                thread.start();
                                btnLike.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        if(Integer.parseInt(btnLike.getTag().toString()) == R.drawable.likeblackmini) {
                                            Thread thread = new Thread() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        eliminarCalificacion(idUsuario, idProveedor);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                btnLike.setImageResource(R.drawable.likewhitemini);
                                                                btnLike.setTag(R.drawable.likewhitemini);
                                                                Toast.makeText(TablaActivity.this, "descalificacion realizada", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } catch (Exception e) {
                                                        Log.e("app", "exception", e);
                                                    }
                                                }
                                            };
                                            thread.start();
                                        }else{
                                            Thread thread = new Thread() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Calificacion(idUsuario, idProveedor);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                btnLike.setImageResource(R.drawable.likeblackmini);
                                                                btnLike.setTag(R.drawable.likeblackmini);
                                                                Toast.makeText(TablaActivity.this, "Gracias por calificar", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } catch (Exception e) {
                                                        Log.e("app", "exception", e);
                                                    }
                                                }
                                            };
                                        thread.start();
                                        }

                                    }
                                });

                                lblNombreLeñador.setText(NombreLeñador);
                                lblNombreLeñador.setLayoutParams(params2);
                                lblNombreLeñador.setTextSize(16);
                                lblNombreLeñador.setTextColor(Color.parseColor("#212121"));
                                lblNombreLeñador.setTypeface(custom_font);

                                lblDireccionLeñador.setText(DireccionLeñador);
                                lblDireccionLeñador.setLayoutParams(params3);
                                lblDireccionLeñador.setTextSize(16);
                                lblDireccionLeñador.setTextColor(Color.parseColor("#212121"));
                                lblDireccionLeñador.setTypeface(custom_font);
                                row.addView(lblNombreLeñador);
                                row.addView(lblDireccionLeñador);
                                row.addView(btnLike);
                                tblTablaUVisitados.addView(row,i);
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

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected_item = (Integer) v.getTag();
        }
    };

    public String Proveedores(int id) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("http://9f44d8db.ngrok.io/obtenerLista.php?idUsuario=" + id);
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

    public void Calificacion(int idUsuario, int idProveedor) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://9f44d8db.ngrok.io/calificacion.php?idUsuario=" + idUsuario
                    + "&idProveedor=" + idProveedor);
            url = url.replaceAll(" ", "%20");
            URL sourceUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
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
    }
    public String verificarCalificacion(int idUsuario, int idProveedor) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://9f44d8db.ngrok.io/verificarCalificacion.php?idUsuario=" + idUsuario
                    + "&idProveedor=" + idProveedor);
            url = url.replaceAll(" ", "%20");
            URL sourceUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
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

    public void eliminarCalificacion(int idUsuario, int idProveedor) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://9f44d8db.ngrok.io/eliminarCalificacion.php?idUsuario=" + idUsuario
                    + "&idProveedor=" + idProveedor);
            url = url.replaceAll(" ", "%20");
            URL sourceUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) sourceUrl.openConnection();
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
    }

    public int DatosJSON(String response) {
        int resultado = 0;

        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                resultado = 1;
            }
        } catch (Exception e) {
        }

        return resultado;
    }
}
