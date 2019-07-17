package com.example.tunrax.materialdesigntest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

public class trackingRealizado extends AppCompatActivity {
    private TextView lblPago;
    private Button btnConfirmar;
    int START_PAYMENT_REQUEST_CODE = 101;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_realizado);
        lblPago = (TextView) findViewById(R.id.lblPago);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        lblPago.setVisibility(View.GONE);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        Bundle bundle = getIntent().getExtras();
        final String tipoDeCompra = bundle.getString("tipoCompra");
        final int idHistorial = bundle.getInt("idHistorial");
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipoDeCompra.equals("efectivo")) {
                    lblPago.setVisibility(View.VISIBLE);
                    btnConfirmar.setVisibility(View.GONE);
                    int verificado = 1;
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(trackingRealizado.this);
                    String url = "http://9f44d8db.ngrok.io/actualizarValidado.php?validado=" + verificado + "&idHistorial=" + idHistorial;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(trackingRealizado.this, "Error con la conexion", Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(stringRequest);
                } else {
                    doPay(idHistorial);
                }

            }

        });
    }

    public void doPay(final int idHistorial) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://9f44d8db.ngrok.io/server/php/create_payment.php";

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
                            intent.putExtra(KhenshinConstants.EXTRA_INTENT_URL, idHistorial);
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


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == START_PAYMENT_REQUEST_CODE) {
            // String exitUrl = data.getStringExtra(KhenshinConstants.EXTRA_INTENT_URL);
            if (resultCode == RESULT_OK) {
                SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
                final int id = prefs.getInt("id", 0); //0 is the default value.
                String idHistorial = prefs.getString("idHistorial", " ");
                int pago = 1;
                RequestQueue queue = Volley.newRequestQueue(trackingRealizado.this);
                String url = "http://9f44d8db.ngrok.io/actualizarPago.php?idHistorial=" + idHistorial + "&pago=" + pago;

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
            SharedPreferences settings = trackingRealizado.this.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
            settings.edit().remove("idHistorial").apply();


        } else {
            Toast.makeText(trackingRealizado.this, "Pago Fallido, si persiste contacte con administrador",
                    Toast.LENGTH_LONG).show();

        }
    }

}
