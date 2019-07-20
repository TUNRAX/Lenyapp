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
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
//TODO cambiar todo este sistema qlo
public class ProveedorData extends AppCompatActivity
        implements CalificacionFragment.OnFragmentInteractionListener, Runnable {
    private TextView lblEmpresa, lblNombre, lblDireccion, lblCiudad, lblRut;
    private FloatingActionButton btnFav;
    private TableLayout tblImagenes;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    int idDetalle = 0;
    int precioUnitario = 0;
    int ventaMinima = 0;
    String producto = "";
    String medida = "";
    private TableLayout tblProductos;
    private TextView lblTipoLenya, lblVentaMinima, lblPrecio;
    final Handler handler = new Handler();
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        Bundle bundle = getIntent().getExtras();



        String nombreProv = bundle.getString("nombreProv");
        String apellidoProv = bundle.getString("apellidoProv");
        String nomEmpresa = bundle.getString("nomEmpresa");
        String rut = bundle.getString("rut");
        String direccion = bundle.getString("direccion");
        String ciudad = bundle.getString("ciudad");

        int calificacion = bundle.getInt("calificacion");
        final int idProveedor = bundle.getInt("idProveedor");
        //TODO obtener las imagenes y ubicarlas abajo
        final ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
        final ArrayList<imgProducto> listaProductos = new ArrayList<imgProducto>();
        final ArrayList<historialEnvios> listaHistorialEnvios = new ArrayList<historialEnvios>();



        setContentView(R.layout.activity_proveedor_data);


        final JSONObject userJson2 = new JSONObject();
        try {
            userJson2.put("idUsuario", id);
            userJson2.put("idProveedor", idProveedor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString2 = userJson2.toString();
        String url2 = "http://97899ef5.ngrok.io/agregarVisita.php";
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
        lblEmpresa.setText(nomEmpresa);
        String concadenar = nombreProv + " " + apellidoProv;
        lblNombre.setText(concadenar);
        lblDireccion.setText(direccion);
        lblCiudad.setText(ciudad);
        lblRut.setText(rut);


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
                String url2 = "http://97899ef5.ngrok.io/agregarFavoritos.php";
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

        /////////////////////////////////////////////////////////////////


        final String[] tipoDeCompra = {""};
        final ArrayList<DatosLenya> listaDatos = new ArrayList<DatosLenya>();
        tblProductos = (TableLayout) findViewById(R.id.tblProductos);
        // btnReporte = (ImageButton) findViewById(R.id.btnReporte);

        final TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params3=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params4=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow.LayoutParams params5=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        final TableRow row = new TableRow(ProveedorData.this);
        params1.setMargins(12,20,0,0);
        params2.setMargins(12,30,0,0);
        params3.setMargins(5,30,5,0);


        Thread thread1 = new Thread() {
            @Override
            public void run() {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(ProveedorData.this);
                String url ="http://97899ef5.ngrok.io/obtenerProductos.php?id="+ idProveedor;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                try {
                                    String respuestaVolley = response;
                                    JSONObject jsonObjectDetalle = new JSONObject(respuestaVolley);
                                    JSONArray jsonArrayDetalle = jsonObjectDetalle.getJSONArray("productos");
                                    for (int x = 0; x < jsonArrayDetalle.length(); x++) {
                                        listaDatos.add(new DatosLenya(jsonArrayDetalle.getJSONObject(x)));
                                    }
                                    try {

                                        for (int i = 0; i < listaDatos.size(); i++) {
                                            TableRow row = new TableRow(ProveedorData.this);
                                            final ArrayList<Calificacion> listaCalificacion = new ArrayList<Calificacion>();
                                            TextView lblprecioUnitario = new TextView(ProveedorData.this);
                                            TextView lblVentaMinima = new TextView(ProveedorData.this);
                                            TextView lblProducto = new TextView(ProveedorData.this);
                                            TextView lblMedida = new TextView(ProveedorData.this);
                                            final EditText txtCantidad = new EditText(ProveedorData.this);
                                            txtCantidad.setHint("¿Cuanto quiere?");
                                            txtCantidad.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                            row.setLayoutParams(lp);
                                            idDetalle = listaDatos.get(i).getId();
                                            precioUnitario = listaDatos.get(i).getPrecioUnitario();
                                            ventaMinima = listaDatos.get(i).getVentaMinima();
                                            producto = listaDatos.get(i).getProducto();
                                            medida = listaDatos.get(i).getMedida();
                                            final ImageView btnCarrito = new ImageButton(ProveedorData.this.getApplicationContext());
                                            btnCarrito.setImageResource(R.drawable.shoppingcart);
                                            btnCarrito.setTag(R.drawable.shoppingcart);
                                            btnCarrito.setLayoutParams(lp);
                                            btnCarrito.setOnClickListener(ClickListener);
                                            btnCarrito.setBackgroundColor(Color.TRANSPARENT);
                                            btnCarrito.setTag(i);
                                            btnCarrito.setId(i);

                                            btnCarrito.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View v) {
                                                    if(txtCantidad.getText().toString().equals("")){
                                                        Toast.makeText(ProveedorData.this, "Rellene el campo si desea pedir", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        final String cantidad = txtCantidad.getText().toString();
                                                        final int precioOficial= precioUnitario * Integer.parseInt(cantidad);
                                                        final Thread t1 = new Thread(new Runnable() {
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


                                                                        for (int i = 0; i < listaCliente.size(); i++) {
                                                                            idCliente = listaCliente.get(i).getId();

                                                                        }


                                                                    } catch (Exception e) {
                                                                        Log.e("app", "exception", e);
                                                                    }

                                                                    RequestQueue queue = Volley.newRequestQueue(ProveedorData.this);

                                                                    String url = "http://97899ef5.ngrok.io/crearPedido.php?idCliente=" + idCliente + "&idDetalle=" + idDetalle + "&tipoDeCompra=" + tipoDeCompra[0] + "&cantidad="+cantidad;

                                                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    String pedidoCreado = response;
                                                                                    try {
                                                                                        JSONObject jsonObjectHistorial = new JSONObject(pedidoCreado);
                                                                                        JSONObject idHistorialJson = jsonObjectHistorial.getJSONObject("idHistorial");
                                                                                        listaHistorialEnvios.add(new historialEnvios(idHistorialJson));
                                                                                        for (int i = 0; i < listaHistorialEnvios.size(); i++) {
                                                                                            idHistorial[0] = listaHistorialEnvios.get(i).getId();

                                                                                        }

                                                                                    } catch (Exception e) {
                                                                                        Log.e("app", "exception", e);
                                                                                    }
                                                                                }
                                                                            }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            Toast.makeText(ProveedorData.this, "Error", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                    queue.add(stringRequest);


                                                                    final int[] finalizar = {0};
                                                                    final Runnable runnable = new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            try {
                                                                                // Instantiate the RequestQueue.
                                                                                RequestQueue queue = Volley.newRequestQueue(ProveedorData.this);
                                                                                String url = "http://97899ef5.ngrok.io/comprobarValidado.php?idHistorial=" + idHistorial[0];

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
                                                                                        Toast.makeText(ProveedorData.this, "Error", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });


                                                                                queue.add(stringRequest);

                                                                                if (verificado[0] == 4) {
                                                                                    Toast.makeText(ProveedorData.this, "Redireccionando", Toast.LENGTH_SHORT).show();
                                                                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                                                    editor.putString("idHistorial", String.valueOf(idHistorial[0]));
                                                                                    editor.apply();


                                                                                    Intent i = new Intent(ProveedorData.this, MapsActivity2.class);
                                                                                    i.putExtra("idHistorial", idHistorial[0]);
                                                                                    i.putExtra("tipoCompra", tipoDeCompra[0]);
                                                                                    i.putExtra("precio", precioOficial);
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

                                                                    if (finalizar[0] == 1) {
                                                                        handler.removeCallbacks(runnable);
                                                                    } else {
                                                                        handler.postDelayed(runnable, 10000);
                                                                    }


                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });


                                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                switch (which) {
                                                                    case DialogInterface.BUTTON_POSITIVE:
                                                                        //Yes button clicked
                                                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                switch (which) {
                                                                                    case DialogInterface.BUTTON_POSITIVE:
                                                                                        tipoDeCompra[0] = "khipu";
                                                                                        progressDoalog = new ProgressDialog(ProveedorData.this);
                                                                                        progressDoalog.setMessage("Espere mientras nos contactamos con el proveedor");
                                                                                        progressDoalog.setTitle("Se esta procesando el pedido");
                                                                                        progressDoalog.show();
                                                                                        t1.start();
                                                                                        break;

                                                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                                                        tipoDeCompra[0] = "efectivo";
                                                                                        progressDoalog = new ProgressDialog(ProveedorData.this);
                                                                                        progressDoalog.setMessage("Espere mientras nos contactamos con el proveedor");
                                                                                        progressDoalog.setTitle("Se esta procesando el pedido");
                                                                                        progressDoalog.show();
                                                                                        t1.start();
                                                                                        break;

                                                                                    case DialogInterface.BUTTON_NEUTRAL:
                                                                                        Toast.makeText(ProveedorData.this, "Compra Cancelada", Toast.LENGTH_SHORT).show();
                                                                                        break;
                                                                                }
                                                                            }
                                                                        };

                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorData.this);
                                                                        builder.setMessage("Metodo de compra").setPositiveButton("Debito/tarjeta", dialogClickListener)
                                                                                .setNegativeButton("Efectivo", dialogClickListener)
                                                                                .setNeutralButton("Cancelar", dialogClickListener).show();




                                                                        break;

                                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                                        //No button clicked
                                                                        Toast.makeText(ProveedorData.this, "Pedido cancelado", Toast.LENGTH_SHORT).show();
                                                                        break;
                                                                }
                                                            }
                                                        };


                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProveedorData.this);
                                                        builder.setMessage("¿Estas seguro que deseas hacer un pedido?, el precio sera de $"+precioOficial).setPositiveButton("Si", dialogClickListener)
                                                                .setNegativeButton("No", dialogClickListener).show();

                                                    }
                                                }
                                            });

                                            lblprecioUnitario.setText(String.valueOf("$"+precioUnitario));
                                            lblprecioUnitario.setLayoutParams(params2);
                                            lblprecioUnitario.setTextSize(12);
                                            lblprecioUnitario.setTextColor(Color.parseColor("#212121"));
                                            lblprecioUnitario.setTypeface(custom_font);

                                            lblVentaMinima.setText(String.valueOf(ventaMinima));
                                            lblVentaMinima.setLayoutParams(params3);
                                            lblVentaMinima.setTextSize(14);
                                            lblVentaMinima.setTextColor(Color.parseColor("#212121"));
                                            lblVentaMinima.setTypeface(custom_font);

                                            lblProducto.setText(producto);
                                            lblProducto.setLayoutParams(params3);
                                            lblProducto.setTextSize(14);
                                            lblProducto.setTextColor(Color.parseColor("#212121"));
                                            lblProducto.setTypeface(custom_font);

                                            lblMedida.setText(medida);
                                            lblMedida.setLayoutParams(params3);
                                            lblMedida.setTextSize(14);
                                            lblMedida.setTextColor(Color.parseColor("#212121"));
                                            lblMedida.setTypeface(custom_font);

                                            txtCantidad.setLayoutParams(params3);
                                            txtCantidad.setTypeface(custom_font);
                                            txtCantidad.setTextSize(10);
                                            row.addView(lblprecioUnitario);
                                            row.addView(lblVentaMinima);
                                            row.addView(lblProducto);
                                            row.addView(lblMedida);
                                            row.addView(txtCantidad);
                                            row.addView(btnCarrito);
                                            tblProductos.addView(row,i);
                                        }

                                    } catch (Exception e) {
                                        Log.e("app", "exception", e);
                                    }
                                } catch (Exception e) {
                                    Log.e("app", "exception", e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProveedorData.this, "Error Volley", Toast.LENGTH_SHORT).show();
                    }
                });


                queue.add(stringRequest);


            }
        };
        thread1.start();



        /////////////////////////////////////////////////////////////////
        //TODO rellenar tabla con fotos
        tblImagenes = (TableLayout) findViewById(R.id.tblImagenes);
        final TableRow row1 = new TableRow(ProveedorData.this);
        RequestQueue queue = Volley.newRequestQueue(ProveedorData.this);
        String url = "http://97899ef5.ngrok.io/obtenerImagenes.php?idProveedor="+idProveedor;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final String resultadoReportes = response;
                        try {
                            JSONObject jsonObjectImagen = new JSONObject(resultadoReportes);
                            JSONArray jsonArrayImagen = jsonObjectImagen.getJSONArray("nombres");
                            for (int x = 0; x < jsonArrayImagen.length(); x++) {
                                listaProductos.add(new imgProducto(jsonArrayImagen.getJSONObject(x)));
                            }
                            for (int i = 0; i < listaProductos.size(); i++) {
                                TableRow row1 = new TableRow(ProveedorData.this);
                                final String nombre = listaProductos.get(i).getNombre();
                                String imageHttpAddress1 = "http://97899ef5.ngrok.io/img/" + nombre + ".jpg";
                                ImageView imgFoto = new ImageView(ProveedorData.this);
                                new LoadImage(imgFoto).execute(imageHttpAddress1);



                                row1.addView(imgFoto);
                                tblImagenes.addView(row1,i);
                            }



                        } catch (Exception e) {
                            Log.e("app", "exception", e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProveedorData.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selected_item = (Integer) v.getTag();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDoalog.incrementProgressBy(1);
        }
    };

    public String comprobarValido(int idHistorial) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://97899ef5.ngrok.io/comprobarValidado.php?idHistorial=" + idHistorial);
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
    public String obtenerIdCliente(int id) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://97899ef5.ngrok.io/seleccionarIdCliente.php?idUsuario=" + id);
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


    public String crearPedido(int idCliente, int idDetalle, String tipoDeCompra, int cantidad) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://97899ef5.ngrok.io/crearPedido.php?idCliente=" + idCliente + "&idDetalle=" + idDetalle + "&tipoDeCompra=" + tipoDeCompra + "&cantidad="+cantidad);
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
