package com.example.tunrax.materialdesigntest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
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
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class tablaPedidos extends AppCompatActivity {
    // private ImageButton btnReporte;
    private TableLayout tblTablaPedidos;
    int idUsu;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_pedidos);

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        idUsu = id;
        final ArrayList<historialEnvios> listaHistorial = new ArrayList<historialEnvios>();

        tblTablaPedidos = (TableLayout) findViewById(R.id.tblTablaPedidos);
        // btnReporte = (ImageButton) findViewById(R.id.btnReporte);

        final TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        final TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        final TableRow.LayoutParams params3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        final TableRow row = new TableRow(tablaPedidos.this);
        params1.setMargins(12, 20, 0, 0);
        params2.setMargins(12, 30, 0, 0);
        params3.setMargins(5, 30, 5, 0);

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                final String pedidosObtenidos = obtenerPedidos(idUsu);
                try {
                    JSONObject jsonObjectHistorial = new JSONObject(pedidosObtenidos);
                    JSONArray jsonArrayHistorial = jsonObjectHistorial.getJSONArray("historial");

                    for (int x = 0; x < jsonArrayHistorial.length(); x++) {
                        listaHistorial.add(new historialEnvios(jsonArrayHistorial.getJSONObject(x)));
                    }

                } catch (Exception e) {
                    Log.e("app", "exception", e);
                }

                try {
                    for (int i = 0; i < listaHistorial.size(); i++) {
                        //final String[] nombreLenador = {""};
                        //final String[] apellidoLenador = {""};
                        //final int[] precioUnitario = {0};
                        //final String[] tipoDeLeña = {""};

                        int idDetalle = listaHistorial.get(i).getIdDetalleProducto();
                        String proveedorObtenido = obtenerProveedor(idDetalle);
                        String detalleObtenido = obtenerDetalle(idDetalle);
                        String tipoProductoObtenido = obtenerProducto(idDetalle);

                        JSONObject jsonObjectProveedor = new JSONObject(proveedorObtenido);
                        JSONObject jsonProveedor = jsonObjectProveedor.getJSONObject("proveedor");
                        final String nombreLenador = jsonProveedor.getString("nombre");
                        final String apellidoLenador = jsonProveedor.getString("apellido");

                        JSONObject jsonObjectDatos = new JSONObject(detalleObtenido);
                        JSONObject datosJson = jsonObjectDatos.getJSONObject("detalle");
                        int precioUnitario = datosJson.getInt("precio_unitario");
                        final String medida = datosJson.getString("medida");

                        JSONObject jsonObjectTipo = new JSONObject(tipoProductoObtenido);
                        JSONObject tipoJson = jsonObjectTipo.getJSONObject("producto");
                        final String tipoDeLeña = tipoJson.getString("nombre");

                        final int idHistorial = listaHistorial.get(i).getId();
                        final int cantidad = listaHistorial.get(i).getCantidad();
                        final int precioOficial = precioUnitario * cantidad;
                        final String fechaPedido = listaHistorial.get(i).getFecha();
                        final String fechaEnvio = listaHistorial.get(i).getFechaEnvio();
                        final int estado = listaHistorial.get(i).getValidado();
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                        TableRow row = new TableRow(tablaPedidos.this);
                        TextView lblNombreLeñador = new TextView(tablaPedidos.this);
                        TextView lblTipoDeLeña = new TextView(tablaPedidos.this);
                        TextView lblCantidad = new TextView(tablaPedidos.this);
                        TextView lblPrecio = new TextView(tablaPedidos.this);
                        TextView lblFechaPedido = new TextView(tablaPedidos.this);
                        TextView lblFechaEntrega = new TextView(tablaPedidos.this);
                        TextView lblTextoEstado = new TextView(tablaPedidos.this);
                        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);
                        final Button btnCancelar = new Button(tablaPedidos.this);
                        final Button btnRastrear = new Button(tablaPedidos.this);
                        final Button btnEfectivo = new Button(tablaPedidos.this);
                        final Button btnDebito = new Button(tablaPedidos.this);
                        btnCancelar.setBackgroundColor(Color.RED);
                        btnCancelar.setText("cancelar");
                        btnRastrear.setBackgroundColor(Color.BLUE);
                        btnRastrear.setText("rastrear");
                        btnEfectivo.setBackgroundColor(Color.CYAN);
                        btnEfectivo.setText("Efectivo");
                        btnDebito.setBackgroundColor(Color.BLUE);
                        btnDebito.setText("tarjeta de credito/debito");

                                btnCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        RequestQueue queue = Volley.newRequestQueue(tablaPedidos.this);
                                                        String url ="http://fd668ba1.sa.ngrok.io/actualizarValidado.php?idHistorial=" + idHistorial + "&validado="+ 2;


                                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        tablaPedidos.this.recreate();
                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Toast.makeText(tablaPedidos.this, "Error Volley", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        queue.add(stringRequest);
                                                        break;

                                                    case DialogInterface.BUTTON_NEGATIVE:

                                                        break;
                                                }
                                            }
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(tablaPedidos.this);
                                        builder.setMessage("¿Esta seguro que desea cancelar el pedido?").setPositiveButton("Si", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    }

                                });

                        btnRastrear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RequestQueue queue = Volley.newRequestQueue(tablaPedidos.this);
                                String url ="http://fd668ba1.sa.ngrok.io/obtenerClientePorIdUsuario.php?idUsuario=" + id;


                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                JSONObject jsonObjectCliente = null;
                                                try {
                                                    jsonObjectCliente = new JSONObject(response);
                                                    JSONObject clienteJson = jsonObjectCliente.getJSONObject("cliente");
                                                    String direccionCliente = clienteJson.getString("direccion");
                                                    Intent i = new Intent(tablaPedidos.this, MapsActivity2.class);
                                                    i.putExtra("idHistorial", idHistorial);
                                                    i.putExtra("direccion", direccionCliente);
                                                    i.putExtra("precioOficial", precioOficial);
                                                    startActivity(i);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(tablaPedidos.this, "Error Volley", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                queue.add(stringRequest);


                            }
                        });

                        btnEfectivo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(tablaPedidos.this, trackingRealizado.class);
                                i.putExtra("tipoCompra", 2);
                                i.putExtra("medida", medida);
                                i.putExtra("cantidad", cantidad);
                                i.putExtra("tipoProducto", tipoDeLeña);
                                i.putExtra("idHistorial", idHistorial);
                                i.putExtra("precioOficial", precioOficial);

                                startActivity(i);
                                Toast.makeText(tablaPedidos.this, "Estoy funcionando y pagando con efectivo", Toast.LENGTH_SHORT).show();
                            }
                        });

                        btnDebito.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(tablaPedidos.this, trackingRealizado.class);
                                i.putExtra("tipoCompra", 1);
                                i.putExtra("medida", medida);
                                i.putExtra("cantidad", cantidad);
                                i.putExtra("tipoProducto", tipoDeLeña);
                                i.putExtra("idHistorial", idHistorial);
                                i.putExtra("precioOficial", precioOficial);

                                startActivity(i);
                                Toast.makeText(tablaPedidos.this, "Estoy funcionando y pagando con efectivo", Toast.LENGTH_SHORT).show();
                            }
                        });

                        lblNombreLeñador.setText(nombreLenador + " " + apellidoLenador);
                        lblNombreLeñador.setLayoutParams(params2);
                        lblNombreLeñador.setTextSize(16);
                        lblNombreLeñador.setTextColor(Color.parseColor("#212121"));
                        lblNombreLeñador.setTypeface(custom_font);

                        lblTipoDeLeña.setText(tipoDeLeña);
                        lblTipoDeLeña.setLayoutParams(params3);
                        lblTipoDeLeña.setTextSize(16);
                        lblTipoDeLeña.setTextColor(Color.parseColor("#212121"));
                        lblTipoDeLeña.setTypeface(custom_font);

                        lblCantidad.setText(String.valueOf(cantidad));
                        lblCantidad.setLayoutParams(params3);
                        lblCantidad.setTextSize(16);
                        lblCantidad.setTextColor(Color.parseColor("#212121"));
                        lblCantidad.setTypeface(custom_font);

                        lblPrecio.setText(String.valueOf(precioOficial));
                        lblPrecio.setLayoutParams(params3);
                        lblPrecio.setTextSize(16);
                        lblPrecio.setTextColor(Color.parseColor("#212121"));
                        lblPrecio.setTypeface(custom_font);

                        lblFechaPedido.setText(String.valueOf(fechaPedido));
                        lblFechaPedido.setLayoutParams(params3);
                        lblFechaPedido.setTextSize(16);
                        lblFechaPedido.setTextColor(Color.parseColor("#212121"));
                        lblFechaPedido.setTypeface(custom_font);

                        if (fechaEnvio.equals("1753-01-01")) {
                            lblFechaEntrega.setText("Fecha de entrega aun no estipulada");
                        } else {
                            lblFechaEntrega.setText(String.valueOf(fechaEnvio));
                        }
                        lblFechaPedido.setLayoutParams(params3);
                        lblFechaPedido.setTextSize(16);
                        lblFechaPedido.setTextColor(Color.parseColor("#212121"));
                        lblFechaPedido.setTypeface(custom_font);

                        if (estado == 1) {
                            lblTextoEstado.setText("Pedido realizado con exito");
                        } else {
                            lblTextoEstado.setText("Pedido cancelado");
                        }

                        lblTextoEstado.setLayoutParams(params3);
                        lblTextoEstado.setTextSize(16);
                        lblTextoEstado.setTextColor(Color.parseColor("#212121"));
                        lblTextoEstado.setTypeface(custom_font);

                        row.addView(lblNombreLeñador);
                        row.addView(lblTipoDeLeña);
                        row.addView(lblCantidad);
                        row.addView(lblPrecio);
                        row.addView(lblFechaPedido);
                        row.addView(lblFechaEntrega);
                        if (estado == 3) {
                            row.addView(btnCancelar);
                        } else if (estado == 6) {
                            row.addView(btnCancelar);
                        } else if (estado == 4) {
                            row.addView(btnRastrear);
                        } else if (estado == 7) {
                            row.addView(btnEfectivo);
                            row.addView(btnDebito);
                        } else if (estado == 1) {
                            row.addView(lblTextoEstado);
                        } else if (estado == 2) {
                            row.addView(lblTextoEstado);
                        }

                        tblTablaPedidos.addView(row, finalI);
                            }
                        });
                    }

                } catch (Exception e) {
                    Log.e("app", "exception", e);
                }

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

    public String obtenerPedidos(int idUsu) {
        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://fd668ba1.sa.ngrok.io/seleccionarHistorialPorCliente.php?idUsuario=" + idUsu);
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
        return resultado.toString();
    }

    public String obtenerProveedor(int idDetalle) {
        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://fd668ba1.sa.ngrok.io/obtenerProveedorPorIdDetalle.php?idDetalle=" + idDetalle);
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
        return resultado.toString();
    }

    public String obtenerDetalle(int idDetalle) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("http://fd668ba1.sa.ngrok.io/obtenerDetallePorId.php?idDetalle=" + idDetalle);
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
            Log.e("app", "exception", e);
        }
        return resultado.toString();
    }

    public String obtenerProducto(int idDetalle) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("http://fd668ba1.sa.ngrok.io/obtenerTipoProductoPorIdDetalle.php?idDetalle=" + idDetalle);
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
            Log.e("app", "exception", e);
        }
        return resultado.toString();
    }
}
