package com.example.tunrax.materialdesigntest;

/**
 * Esta activity almacena una barra de busqueda (TODO)
 * Contiene un navigation drawer el cual sirve para llevar a otras funciones
 * que tiene la aplicacion el cual tiene editar usuario (TODO),
 * tabla de favoritos (TODO),
 * tabla de visitas recientes (TODO),
 * acerca de y
 * cerrar sesion.
 *
 * ademas contiene un fragment que contiene un mapa de google el cual
 * hace la funcion principal de la app
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

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


public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, mapFragment.OnFragmentInteractionListener {

    TextView txtNomApe;
    TextView txtCorreo;
    String nombre;
    String apellido;
    String correo;
    int idUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        int id = prefs.getInt("id", 0); //0 is the default value.
        idUsu = id;
        Bundle bundle = getIntent().getExtras();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        txtNomApe = (TextView) hView.findViewById(R.id.txtNomApe);
        txtCorreo = (TextView) hView.findViewById(R.id.txtCorreo);

        final ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();


        final JSONObject userJson1 = new JSONObject();
        try {
            userJson1.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString1 = userJson1.toString();
        String url1 = "http://84361097.ngrok.io/BuscarCliente.php";
        try {
            Back ejec = new Back(new Back.AsyncResponse() {
                @Override
                public void processFinish(String jsonResult) {
                    try {
                        String nomUsu = "";
                        String apeUsu = "";
                        String correoRial = correo;
                        JSONObject jsonObjectCliente = new JSONObject(jsonResult);
                        JSONArray jsonArrayCliente = jsonObjectCliente.getJSONArray("cliente");
                        for (int x = 0; x < jsonArrayCliente.length(); x++) {
                            listaCliente.add(new Cliente(jsonArrayCliente.getJSONObject(x)));
                        }

                        for (int i = 0; i < listaCliente.size(); i++) {
                            nomUsu = listaCliente.get(i).getNombre();
                            apeUsu = listaCliente.get(i).getApellido();
                        }
                        String concatenado = nomUsu + " " + apeUsu;
                        txtNomApe.setText(concatenado);
                        txtCorreo.setText(correoRial);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error capa 8", Toast.LENGTH_LONG).show();

                    }
                }
            });
            ejec.execute(url1, jsonString1);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Me quiero matar", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        Toast.makeText(getApplicationContext(), "Para mas datos acerca del proveedor presione el cuadro que salga arriba del marcador presionado", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.btnEdit) {
            final ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();
            final ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        String nombre = "";
                        String apellido = "";
                        String direccion = "";
                        String fono = "";
                        String ciudad = "";
                        String contrasenya = "";
                        String buscado = buscarUsuario(idUsu);
                        JSONObject jsonObjectCliente = new JSONObject(buscado);
                        JSONArray jsonArrayCliente = jsonObjectCliente.getJSONArray("cliente");
                        for (int x = 0; x < jsonArrayCliente.length(); x++) {
                            listaCliente.add(new Cliente(jsonArrayCliente.getJSONObject(x)));
                        }

                        for (int i = 0; i < listaCliente.size(); i++) {
                            nombre = listaCliente.get(i).getNombre();
                            apellido = listaCliente.get(i).getApellido();
                            direccion = listaCliente.get(i).getDireccion();
                            fono = listaCliente.get(i).getFono();
                            ciudad = listaCliente.get(i).getCiudad();
                        }

                        JSONObject jsonObjectUsuario = new JSONObject(buscado);
                        JSONArray jsonArrayUsuario = jsonObjectUsuario.getJSONArray("usuario");
                        for (int x = 0; x < jsonArrayUsuario.length(); x++) {
                            listaUsuario.add(new Usuario(jsonArrayUsuario.getJSONObject(x)));
                        }

                        for (int i = 0; i < listaUsuario.size(); i++) {
                            contrasenya = listaUsuario.get(i).getContrasenya();
                        }

                        Intent i = new Intent(getApplicationContext(), Edit.class);
                        i.putExtra("nombre", nombre);
                        i.putExtra("apellido", apellido);
                        i.putExtra("direccion", direccion);
                        i.putExtra("fono", fono);
                        i.putExtra("ciudad", ciudad);
                        i.putExtra("contrasenya", contrasenya);
                        startActivity(i);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error al cambiar de pagina.", Toast.LENGTH_LONG).show();
                        Log.e("app", "exception", e);
                    }
                }
                {

                }


            };
        thread.start();

        } else if (id == R.id.btnView) {
            Intent i = new Intent(MapActivity.this, TablaActivity.class);
            startActivity(i);
        } else if (id == R.id.btnInfo) {
            //Intent i = new Intent(MapActivity.this, AcercaDeActivity.class);
            //startActivity(i);
            Toast.makeText(this, "Creado por Leñapp Co.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnLogoff) {
            SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent i = new Intent(MapActivity.this, LogIn.class);
            startActivity(i);
        }else if (id == R.id.btnFavoritos) {
            Intent i = new Intent(MapActivity.this, FavoritoActivity.class);
            startActivity(i);
        }else if (id == R.id.btnReporte) {
            Intent i = new Intent(MapActivity.this, ReporteActivity.class);
            startActivity(i);
        }else if (id == R.id.btnReportesH) {
            Intent i = new Intent(MapActivity.this, TablaReportes.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public String buscarUsuario(int idUsu) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://84361097.ngrok.io/buscarUsuario.php?idUsuario=" + idUsu);
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

}

