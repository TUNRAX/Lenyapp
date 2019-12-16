package com.example.tunrax.materialdesigntest;
/**
 * Esta activity tiene dos campos los cuales serviran para enviar
 * un correo a los administradores de la aplicacion, al llenar los campos y apretar
 * el boton se enviara un correo electronico con los datos insertados. (TODO)
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReporteActivity extends AppCompatActivity {

    private EditText txtTitulo;
    private EditText txtDescripcion;
    private Button btnEnviar;
    int idUsu;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        final String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        idUsu = id;
        checkearPedidos(idUsu);
        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            String titulo = txtTitulo.getText().toString();
                            String descripcion = txtDescripcion.getText().toString();
                            respaldarReporte(titulo, descripcion, id);
                        } catch (Exception e) {
                            Log.e("app", "exception", e);
                        }

                    }

                };
                thread.start();
                Log.i("SendMailActivity", "Send Button Clicked.");

                String fromEmail = "reportemail@lenapp.cl";
                String fromPassword = "C4lL0Wh331Ch4IrM1n3R02M1n3Cr4f7.";
                String toEmails = "tomasalvarezyunginger@hotmail.com";
                List toEmailList = Arrays.asList(toEmails
                        .split("\\s*,\\s*"));
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = String.valueOf(txtTitulo.getText());
                String emailBody = "el usuario con correo "+correo+" Ha enviado un mensaje:\n"+ String.valueOf(txtDescripcion.getText());
                new SendMailTask(ReporteActivity.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
            }
        });
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

                            RequestQueue queue = Volley.newRequestQueue(ReporteActivity.this);
                            String url = "http://fd668ba1.sa.ngrok.io/seleccionarPedidoCliente.php?idUsuario=" + idUsu;


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
                                    Toast.makeText(ReporteActivity.this, "Error Volley", Toast.LENGTH_SHORT).show();
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
                                                progressDoalog[0] = new ProgressDialog(ReporteActivity.this);
                                                progressDoalog[0].setMessage("Espere mientras nos contactamos con el proveedor");
                                                progressDoalog[0].setTitle("Se esta procesando el pedido");
                                                progressDoalog[0].show();
                                                final Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            if(!actionIsMade[0]) {
                                                                newVerificado[0] = 3;
                                                                RequestQueue queue1 = Volley.newRequestQueue(ReporteActivity.this);
                                                                String url1 = "http://fd668ba1.sa.ngrok.io/actualizarValidado.php?validado=" + newVerificado[0] + "&idHistorial=" + idHistorial[0];

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
                                                            RequestQueue queue = Volley.newRequestQueue(ReporteActivity.this);
                                                            String url = "http://fd668ba1.sa.ngrok.io/comprobarValidado.php?idHistorial=" + idHistorial[0];

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
                                                                    Toast.makeText(ReporteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                            queue.add(stringRequest);

                                                            if (verificado[0] == 4) {
                                                                Toast.makeText(ReporteActivity.this, "Redireccionando", Toast.LENGTH_SHORT).show();
                                                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                                editor.putString("idHistorial", String.valueOf(idHistorial[0]));
                                                                editor.apply();


                                                                Intent i = new Intent(ReporteActivity.this, MapsActivity2.class);
                                                                i.putExtra("idHistorial", idHistorial[0]);
                                                                i.putExtra("tipoCompra", tipoDePago[0]);
                                                                i.putExtra("precio", precioOficial[0]);
                                                                startActivity(i);
                                                                progressDoalog[0].dismiss();
                                                                finalizar[0] = 1;


                                                            } else if (verificado[0] == 2) {
                                                                Toast.makeText(ReporteActivity.this, "Leñador no disponible", Toast.LENGTH_LONG).show();
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
                                                RequestQueue queue1 = Volley.newRequestQueue(ReporteActivity.this);
                                                String url1 = "http://fd668ba1.sa.ngrok.io/actualizarValidado.php?validado=" + newVerificado[0] + "&idHistorial=" + idHistorial[0];

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

                                AlertDialog.Builder builder = new AlertDialog.Builder(ReporteActivity.this);
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
    public void respaldarReporte(String titulo, String descripcion, int id) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://fd668ba1.sa.ngrok.io/respaldarReporte.php?idUsuario=" + id
                    + "&titulo=" + titulo
                    + "&descripcion=" + descripcion);
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
            Log.e("app", "exception", e);
        }
    }
}


