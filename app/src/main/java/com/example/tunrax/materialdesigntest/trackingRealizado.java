package com.example.tunrax.materialdesigntest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.browser2app.khenshin.KhenshinApplication;
import com.browser2app.khenshin.KhenshinConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class trackingRealizado extends AppCompatActivity {
    private TextView lblPago, lblTipoDePago, lblCantidad, lblPrecio, lblTipoLeña;
    int START_PAYMENT_REQUEST_CODE = 101;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    final int[] finalizar = {0};
    final int[] verificado = {0};
    int pagado = 0;
    int tipoDePago = 0;
    final boolean[] isPaused = {true};
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_realizado);
        lblTipoDePago = (TextView) findViewById(R.id.lblTipoDePago);
        lblCantidad = (TextView) findViewById(R.id.lblCantidad);
        lblPrecio = (TextView) findViewById(R.id.lblPrecio);
        lblTipoLeña = (TextView) findViewById(R.id.lblTipoLeña);

        final ArrayList<historialEnvios> listaHistorialEnvios = new ArrayList<historialEnvios>();
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        Bundle bundle = getIntent().getExtras();
        final int tipoDeCompra = bundle.getInt("tipoCompra");
        final int idHistorial = bundle.getInt("idHistorial");
        final int precio = bundle.getInt("precioOficial");
        final String medida = bundle.getString("medida");
        final int cantidad = bundle.getInt("cantidad");
        final String tipoDeLeña = bundle.getString("tipoProducto");
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("idHistorial", idHistorial);
        editor.apply();
        String trueCantidad = cantidad + " " + medida;
        String truePrecio = "$" + precio;
        lblCantidad.setText(trueCantidad);
        lblTipoLeña.setText(tipoDeLeña);
        lblPrecio.setText(truePrecio);
        if (tipoDeCompra == 2) {
            //TODO poner la cantidad que debe pagar
            lblTipoDePago.setText("Pague con efectivo");
            RequestQueue queue = Volley.newRequestQueue(trackingRealizado.this);
            String url ="http://fd668ba1.sa.ngrok.io/actualizarTipoDePago.php?idHistorial="+ idHistorial +"&tipoDePago=" + tipoDeCompra;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String historialEncontrados = response;
                            try {


                            } catch (Exception e) {
                                Log.e("app", "exception", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(trackingRealizado.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(stringRequest);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //final String pedidoObtenido = obtenerPedido(id);

                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RequestQueue queue = Volley.newRequestQueue(trackingRealizado.this);
                                String url = "http://fd668ba1.sa.ngrok.io/seleccionarHistorial.php?idHistorial=" + idHistorial;


                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                String historialEncontrados = response;
                                                try {
                                                    JSONObject jsonObjectHistorial = new JSONObject(historialEncontrados);
                                                    JSONArray jsonArrayHistorial = jsonObjectHistorial.getJSONArray("historial");
                                                    for (int x = 0; x < jsonArrayHistorial.length(); x++) {
                                                        listaHistorialEnvios.add(new historialEnvios(jsonArrayHistorial.getJSONObject(x)));
                                                    }




                                                } catch (Exception e) {
                                                    Log.e("app", "exception", e);
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(trackingRealizado.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                queue.add(stringRequest);

                                for (int i = 0; i < listaHistorialEnvios.size(); i++) {
                                    verificado[0] = listaHistorialEnvios.get(i).getValidado();
                                    pagado = listaHistorialEnvios.get(i).getPagado();
                                    tipoDePago = listaHistorialEnvios.get(i).getTipoDeCompraId();
                                }
                                if(verificado[0] == 1 && pagado == 1){
                                    finalizar[0] = 1;
                                    Toast.makeText(trackingRealizado.this, "Pago realizado con exito", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(trackingRealizado.this, tablaPedidos.class);
                                    startActivity(i);
                                }else{
                                    handle.postDelayed(this, 5000);
                                }
                            } catch (Exception e) {
                                Log.e("app", "exception", e);
                            }
                        }
                    };
                    if(isPaused[0]){
                            handler.removeCallbacks(runnable);
                        }else{
                            handler.postDelayed(runnable, 5000);
                        }
                    if(finalizar[0]==1){
                            handler.removeCallbacks(runnable);
                        }else{
                            handler.postDelayed(runnable, 5000);
                        }
                    }
            }).start();

        } else if (tipoDeCompra == 1) {
            RequestQueue queue = Volley.newRequestQueue(trackingRealizado.this);
            String url ="http://fd668ba1.sa.ngrok.io/actualizarTipoDePago.php?idHistorial="+ idHistorial +"&tipoDePago=" + tipoDeCompra;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String historialEncontrados = response;
                            try {


                            } catch (Exception e) {
                                Log.e("app", "exception", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(trackingRealizado.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(stringRequest);
            lblTipoDePago.setText("Pago en tarjeta de debito/credito");
            doPay(idHistorial, precio);
        }

    }

    public void doPay(final int idHistorial, final int precio) {
        // Instantiate the RequestQueue.
        int cincuentaMil = 50000;
        int precioOficialInterno = 0;
        if (precio >= cincuentaMil) {
            precioOficialInterno = cincuentaMil;
        } else {
            precioOficialInterno = precio;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://fd668ba1.sa.ngrok.io/server/php/create_payment.php?precio=" + precioOficialInterno;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {

                            jsonResponse = new JSONObject(response);
                            String paymentId = jsonResponse.getString("pago");
                            Intent intent = ((KhenshinApplication) getApplication()).getKhenshin().getStartTaskIntent();
                            intent.putExtra(KhenshinConstants.EXTRA_PAYMENT_ID, paymentId);  // ID DEL PAGO
                            intent.putExtra(KhenshinConstants.EXTRA_FORCE_UPDATE_PAYMENT, false); // NO FORZAR LA ACTUALIZACION DE DATOS
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // LIMPIAR EL STACK DE ACTIVIDADES
                            startActivityForResult(intent, START_PAYMENT_REQUEST_CODE); // INICIAR LA ACTIVIDAD ESPERANDO UNA RESPUESTA
                        } catch (JSONException e) {
                            Log.e("app", "exception", e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(trackingRealizado.this, "Error en la compra con khipu", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
        /* */

    }

    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == START_PAYMENT_REQUEST_CODE) {
            // String exitUrl = data.getStringExtra(KhenshinConstants.EXTRA_INTENT_URL);
            if (resultCode == RESULT_OK) {
                SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
                final int id = prefs.getInt("id", 0); //0 is the default value.
                int idHistorial = prefs.getInt("idHistorial", 0);
                int pago = 1;
                RequestQueue queue = Volley.newRequestQueue(trackingRealizado.this);
                String url = "http://fd668ba1.sa.ngrok.io/actualizarPago.php?idHistorial=" + idHistorial + "&pago=" + pago;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(trackingRealizado.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(stringRequest);
            }
            Toast.makeText(trackingRealizado.this, "Pago Realizado Con Exito",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(trackingRealizado.this, MapActivity.class);
            startActivity(i);
            SharedPreferences settings = trackingRealizado.this.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
            settings.edit().remove("idHistorial").apply();


        } else {
            Toast.makeText(trackingRealizado.this, "Pago Fallido, si persiste contacte con administrador",
                    Toast.LENGTH_LONG).show();

        }
    }

}
