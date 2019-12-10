package com.example.tunrax.materialdesigntest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    int idUsu;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_reportes);
        final ArrayList<Reportes> listaReportes = new ArrayList<Reportes>();

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        idUsu = id;
        checkearPedidos(idUsu);
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

    public void checkearPedidos(final int idUsu){
        final ArrayList<historialEnvios> listaHistorialEnvios = new ArrayList<historialEnvios>();
        final ArrayList<DatosLenya> listaDatos = new ArrayList<DatosLenya>();
        final Handler handler = new Handler();
        final ProgressDialog[] progressDoalog = new ProgressDialog[1];

        final int[] finalizar = {0};
        final int[] idHistorial = {0};
        final int[] tipoDePago = {0};
        final int[] verificado = {0};
        final int[] precio = {0};
        final int[] cantidad = {0};
        final int[] precioOficial = {0};
        final boolean[] isPaused = {true};
        final int[] newVerificado = {0};
        final boolean[] actionIsMade = {false};

        new Thread(new Runnable() {
            @Override
            public void run() {
                //final String pedidoObtenido = obtenerPedido(id);

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {

                            RequestQueue queue = Volley.newRequestQueue(TablaReportes.this);
                            String url = "https://865e33a1.sa.ngrok.io/seleccionarPedidoCliente.php?idUsuario=" + idUsu;


                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            String pedidosEncontrados = response;
                                            try {
                                                JSONObject jsonObjectHistorial = new JSONObject(pedidosEncontrados);
                                                JSONObject idHistorialJson = jsonObjectHistorial.getJSONObject("historial");
                                                JSONObject idPrecioJson = jsonObjectHistorial.getJSONObject("precio");
                                                listaHistorialEnvios.add(new historialEnvios(idHistorialJson));
                                                listaDatos.add(new DatosLenya(idPrecioJson));



                                            } catch (Exception e) {
                                                Log.e("app", "exception", e);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(TablaReportes.this, "Error Volley", Toast.LENGTH_SHORT).show();
                                }
                            });

                            queue.add(stringRequest);

                            for (int i = 0; i < listaHistorialEnvios.size(); i++) {
                                verificado[0] = listaHistorialEnvios.get(i).getValidado();
                                idHistorial[0] = listaHistorialEnvios.get(i).getId();
                                tipoDePago[0] = listaHistorialEnvios.get(i).getTipoDeCompraId();
                                cantidad[0] = listaHistorialEnvios.get(i).getCantidad();
                            }

                            for (int i = 0; i < listaDatos.size(); i++) {
                                precio[0] = listaDatos.get(i).getPrecioUnitario();
                            }
                            precioOficial[0] = precio[0] * cantidad[0];
                            if (verificado[0] == 5) {
                                isPaused[0] = true;

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                progressDoalog[0] = new ProgressDialog(TablaReportes.this);
                                                progressDoalog[0].setMessage("Espere mientras nos contactamos con el proveedor");
                                                progressDoalog[0].setTitle("Se esta procesando el pedido");
                                                progressDoalog[0].show();
                                                final Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            if(!actionIsMade[0]) {
                                                                newVerificado[0] = 3;
                                                                RequestQueue queue1 = Volley.newRequestQueue(TablaReportes.this);
                                                                String url1 = "https://865e33a1.sa.ngrok.io/actualizarValidado.php?validado=" + newVerificado[0] + "&idHistorial=" + idHistorial[0];

                                                                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {

                                                                            }
                                                                        }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Log.d("error volley", String.valueOf(error));
                                                                    }
                                                                });


                                                                queue1.add(stringRequest1);
                                                                actionIsMade[0] = true;
                                                            }
                                                            // Instantiate the RequestQueue.
                                                            RequestQueue queue = Volley.newRequestQueue(TablaReportes.this);
                                                            String url = "https://865e33a1.sa.ngrok.io/comprobarValidado.php?idHistorial=" + idHistorial[0];

                                                            // Request a string response from the provided URL.
                                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            // Display the first 500 characters of the response string.
                                                                            String verificar = response;
                                                                            try {
                                                                                JSONObject jsonObjectHistorial = new JSONObject(verificar);
                                                                                JSONObject idHistorialJson = jsonObjectHistorial.getJSONObject("validado");
                                                                                listaHistorialEnvios.add(new historialEnvios(idHistorialJson));

                                                                                for (int i = 0; i < listaHistorialEnvios.size(); i++) {
                                                                                    verificado[0] = listaHistorialEnvios.get(i).getValidado();

                                                                                }


                                                                            } catch (Exception e) {
                                                                                Log.e("app", "exception", e);
                                                                            }
                                                                        }


                                                                    }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Toast.makeText(TablaReportes.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                            queue.add(stringRequest);

                                                            if (verificado[0] == 4) {
                                                                Toast.makeText(TablaReportes.this, "Redireccionando", Toast.LENGTH_SHORT).show();
                                                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                                editor.putString("idHistorial", String.valueOf(idHistorial[0]));
                                                                editor.apply();


                                                                Intent i = new Intent(TablaReportes.this, MapsActivity2.class);
                                                                i.putExtra("idHistorial", idHistorial[0]);
                                                                i.putExtra("tipoCompra", tipoDePago[0]);
                                                                i.putExtra("precio", precioOficial[0]);
                                                                startActivity(i);
                                                                progressDoalog[0].dismiss();
                                                                finalizar[0] = 1;


                                                            } else if (verificado[0] == 2) {
                                                                Toast.makeText(TablaReportes.this, "Leñador no disponible", Toast.LENGTH_LONG).show();
                                                                progressDoalog[0].dismiss();
                                                                finalizar[0] = 1;
                                                                checkearPedidos(idUsu);

                                                            } else {
                                                                handle.postDelayed(this, 10000);
                                                            }

                                                        } catch (Exception e) {
                                                            Log.e("app", "exception", e);
                                                        }

                                                    }

                                                };

                                                if (finalizar[0] == 1) {
                                                    handler.removeCallbacks(runnable);
                                                } else {
                                                    handler.postDelayed(runnable, 10000);
                                                }
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                newVerificado[0] = 2;
                                                RequestQueue queue1 = Volley.newRequestQueue(TablaReportes.this);
                                                String url1 = "https://865e33a1.sa.ngrok.io/actualizarValidado.php?validado=" + newVerificado[0] + "&idHistorial=" + idHistorial[0];

                                                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {

                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.d("error volley", String.valueOf(error));
                                                    }
                                                });

                                                queue1.add(stringRequest1);
                                                checkearPedidos(idUsu);
                                                finalizar[0] = 1;
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(TablaReportes.this);
                                builder.setMessage("Realizo un pedido por web, ¿Desea realizarlo? el precio sera de "+ precioOficial[0]).setPositiveButton("Si", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

                            } else {
                                handle.postDelayed(this, 5000);
                            }


                        } catch (Exception e) {
                            Log.e("app", "exception", e);
                        }

                    }

                };
                if (isPaused[0]) {
                    handler.removeCallbacks(runnable);
                } else {
                    handler.postDelayed(runnable, 10000);
                }
                if (finalizar[0] == 1) {
                    handler.removeCallbacks(runnable);
                } else {
                    handler.postDelayed(runnable, 10000);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public String Reportes(int id) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("https://865e33a1.sa.ngrok.io/obtenerReportes.php?idUsuario=" + id);
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
