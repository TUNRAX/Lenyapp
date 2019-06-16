package com.example.tunrax.materialdesigntest;
/**
 * Esta activity contiene los datos principales del proveedor mas las fotos
 * ademas se incluyen diferentes fragments que muestran diferentes datos
 * como los numeros de contacto, los datos de la leña y la calificacion
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class ProveedorData extends AppCompatActivity
        implements DatosLenyaFragment.OnFragmentInteractionListener, ContactoFragment.OnFragmentInteractionListener, CalificacionFragment.OnFragmentInteractionListener, Runnable {
    private TextView lblEmpresa, lblNombre, lblDireccion, lblCiudad, lblRut;
    private FloatingActionButton btnFav;
    private ImageView imgFoto1, imgFoto2;
    private Button btnPedir;
    String fono1Fragment = "";
    String fono2Fragment = "";
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        Bundle bundle = getIntent().getExtras();
        String fono1 = bundle.getString("fono1");
        fono1Fragment = fono1;
        String fono2 = bundle.getString("fono2");
        fono2Fragment = fono2;
        String nombreProv = bundle.getString("nombreProv");
        String apellidoProv = bundle.getString("apellidoProv");
        String nomEmpresa = bundle.getString("nomEmpresa");
        String rut = bundle.getString("rut");
        String direccion = bundle.getString("direccion");
        String ciudad = bundle.getString("ciudad");
        final int idDetalle = bundle.getInt("idDetalle");
        int calificacion = bundle.getInt("calificacion");
        final int idProveedor = bundle.getInt("idProveedor");
        String imageHttpAddress1 = "http://b227b69e.ngrok.io/img_nose/" + idProveedor + "_0.jpg";
        String imageHttpAddress2 = "http://b227b69e.ngrok.io/img_nose/" + idProveedor + "_1.jpg";
        final ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
        final ArrayList<historialEnvios> listaHistorialEnvios = new ArrayList<historialEnvios>();
        final Handler handler = new Handler();



        setContentView(R.layout.activity_proveedor_data);


        final JSONObject userJson2 = new JSONObject();
        try {
            userJson2.put("idUsuario", id);
            userJson2.put("idProveedor", idProveedor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString2 = userJson2.toString();
        String url2 = "http://b227b69e.ngrok.io/agregarVisita.php";
        try {
            Back ejec2 = new Back(new Back.AsyncResponse() {
                @Override
                public void processFinish(String jsonResult2) {
                    try {

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al ejecutar uno de los procesos.", Toast.LENGTH_LONG).show();

                    }
                }
            });
            ejec2.execute(url2, jsonString2);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "que se yo que esta pasando", Toast.LENGTH_LONG).show();
        }
        lblEmpresa = (TextView) findViewById(R.id.lblEmpresa);
        lblNombre = (TextView) findViewById(R.id.lblNombre);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblCiudad = (TextView) findViewById(R.id.lblCiudad);
        lblRut = (TextView) findViewById(R.id.lblRut);
        btnFav = (FloatingActionButton) findViewById(R.id.btnFav);
        imgFoto1 = (ImageView) findViewById(R.id.imgFoto1);
        imgFoto2 = (ImageView) findViewById(R.id.imgFoto2);
        btnPedir = (Button) findViewById(R.id.btnPedir);
        lblEmpresa.setText(nomEmpresa);
        String concadenar = nombreProv + " " + apellidoProv;
        lblNombre.setText(concadenar);
        lblDireccion.setText(direccion);
        lblCiudad.setText(ciudad);
        lblRut.setText(rut);
        new LoadImage(imgFoto1).execute(imageHttpAddress1);
        new LoadImage(imgFoto2).execute(imageHttpAddress2);


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject userJson2 = new JSONObject();
                try {
                    userJson2.put("idUsuario", id);
                    userJson2.put("idProveedor", idProveedor);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonString2 = userJson2.toString();
                String url2 = "http://b227b69e.ngrok.io/agregarFavoritos.php";
                try {
                    Back ejec2 = new Back(new Back.AsyncResponse() {
                        @Override
                        public void processFinish(String jsonResult2) {
                            try {
                                Toast.makeText(ProveedorData.this, "Agregado a favoritos con exito", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error al ejecutar uno de los procesos.", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                    ejec2.execute(url2, jsonString2);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "que se yo que esta pasando", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnPedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                progressDoalog = new ProgressDialog(ProveedorData.this);
                                progressDoalog.setMessage("Espere mientras nos contactamos con el proveedor");
                                progressDoalog.setTitle("Se esta procesando el pedido");
                                progressDoalog.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                                int idCliente = 0;
                                                final int[] idHistorial = {0};
                                                final int[] verificado = {0};
                                                final String obtenidaidCliente = obtenerIdCliente(id);

                                                try {

                                                    JSONObject jsonObjectCliente = new JSONObject(obtenidaidCliente);
                                                    JSONObject idClienteJson = jsonObjectCliente.getJSONObject("idCliente");
                                                    listaCliente.add(new Cliente(idClienteJson));

                                                    /*JSONObject object = new JSONObject(obtenidaidCliente);
                                                    JSONArray Jarray  = object.getJSONArray("idCliente");*/
                                                    //JSONArray ja = new JSONObject(obtenidaidCliente).getJSONArray("idCliente");
                                                    /*JSONObject jsonObjectCliente = obtenidaidCliente.getJSONArray("idCliente");
                                                    Iterator<?> keys = jsonObjectCliente.keys();

                                                    for (int i = 0; i < ja.length(); i++)
                                                    {
                                                        JSONObject Jasonobject = ja.getJSONArray("idCliente")(i);
                                                        listaCliente.add(new Cliente(Jasonobject));
                                                    }*/

                                                    /*JSONObject jsonObjectCliente = new JSONObject(obtenidaidCliente);
                                                    JSONArray jsonArrayCliente = jsonObjectCliente.getJSONArray("idCliente");
                                                    for (int x = 0; x < jsonArrayCliente.length(); x++) {
                                                        listaCliente.add(new Cliente(jsonArrayCliente.getJSONObject(x)));
                                                    }*/
                                                    for (int i = 0; i < listaCliente.size(); i++) {
                                                        idCliente = listaCliente.get(i).getId();

                                                    }


                                                } catch (Exception e) {
                                                    Log.e("app", "exception", e);
                                                }
                                                final String pedidoCreado = crearPedido(idCliente, idDetalle);
                                                try {
                                                    JSONObject jsonObjectHistorial = new JSONObject(pedidoCreado);
                                                    JSONObject idHistorialJson = jsonObjectHistorial.getJSONObject("idHistorial");
                                                    listaHistorialEnvios.add(new historialEnvios(idHistorialJson));
                                                    /*JSONObject jsonObjectHistorial = new JSONObject(pedidoCreado);
                                                    JSONArray jsonArrayHistorial = jsonObjectHistorial.getJSONArray("idHistorial");
                                                    for (int x = 0; x < jsonArrayHistorial.length(); x++) {
                                                        listaHistorialEnvios.add(new historialEnvios(jsonArrayHistorial.getJSONObject(x)));
                                                    }*/
                                                    for (int i = 0; i < listaHistorialEnvios.size(); i++) {
                                                         idHistorial[0] = listaHistorialEnvios.get(i).getId();

                                                    }

                                                } catch (Exception e) {
                                                    Log.e("app", "exception", e);
                                                }


                                                final int[] finalizar = {0};
                                                //final String verificar=comprobarValido(idHistorial[0]);
                                                final Runnable runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    // Instantiate the RequestQueue.
                                                                    RequestQueue queue = Volley.newRequestQueue(ProveedorData.this);
                                                                    String url = "http://b227b69e.ngrok.io/comprobarValidado.php?idHistorial=" + idHistorial[0];

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
                                                                            Toast.makeText(ProveedorData.this, "no funco esta wea", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });


                                                                    queue.add(stringRequest);

                                                                    if (verificado[0] == 1) {
                                                                        Toast.makeText(ProveedorData.this, "Redireccionando", Toast.LENGTH_SHORT).show();
                                                                        Intent i = new Intent(getApplicationContext(), MapsActivity2.class);
                                                                        i.putExtra("idHistorial", idHistorial[0]);
                                                                        startActivity(i);
                                                                        progressDoalog.dismiss();
                                                                        finalizar[0] = 1;


                                                                    } else if (verificado[0] == 2) {
                                                                        Toast.makeText(ProveedorData.this, "Leñador no disponible", Toast.LENGTH_LONG).show();
                                                                        progressDoalog.dismiss();
                                                                        finalizar[0] = 1;

                                                                    } else {
                                                                        handle.postDelayed(this, 10000);
                                                                    }

                                                                } catch (Exception e) {
                                                                    Log.e("app", "exception", e);
                                                                }


                                                            }

                                                        };

                                                        if(finalizar[0] == 1) {
                                                            handler.removeCallbacks(runnable);
                                                        }else {
                                                            handler.postDelayed(runnable, 10000);
                                                        }



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(ProveedorData.this, "Ok", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };


                AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorData.this);
                builder.setMessage("¿Estas seguro que deseas hacer un pedido?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDoalog.incrementProgressBy(1);
        }
    };

    public String obtenerIdCliente(int id) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://b227b69e.ngrok.io/seleccionarIdCliente.php?idUsuario="+ id);
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
            e.printStackTrace();
        }
        return resultado.toString();
    }


    public String crearPedido(int idCliente, int idDetalle) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://b227b69e.ngrok.io/crearPedido.php?idCliente="+ idCliente +"&idDetalle="+ idDetalle);
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
            e.printStackTrace();
        }
        return resultado.toString();
    }

    public String comprobarValido(int idHistorial) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://b227b69e.ngrok.io/comprobarValidado.php?idHistorial="+ idHistorial);
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
            e.printStackTrace();
        }
        return resultado.toString();
    }

    @Override
    public void run() {

    }
}
