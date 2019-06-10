package com.example.tunrax.materialdesigntest;
/**
 * Esta activity contiene los datos principales del proveedor mas las fotos
 * ademas se incluyen diferentes fragments que muestran diferentes datos
 * como los numeros de contacto, los datos de la leña y la calificacion
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProveedorData extends AppCompatActivity
        implements DatosLenyaFragment.OnFragmentInteractionListener, ContactoFragment.OnFragmentInteractionListener, CalificacionFragment.OnFragmentInteractionListener {
    private TextView lblEmpresa, lblNombre, lblDireccion, lblCiudad, lblRut;
    private FloatingActionButton btnFav;
    private ImageView imgFoto1, imgFoto2;
    String fono1Fragment = "";
    String fono2Fragment = "";

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
        String apellidoProv= bundle.getString("apellidoProv");
        String nomEmpresa = bundle.getString("nomEmpresa");
        String rut = bundle.getString("rut");
        String direccion = bundle.getString ("direccion");
        String ciudad = bundle.getString("ciudad");
        int calificacion = bundle.getInt("calificacion");
        final int idProveedor = bundle.getInt("idProveedor");
        String imageHttpAddress1 = "http://7a144ad2.ngrok.io/img_nose/"+idProveedor+"_0.jpg";
        String imageHttpAddress2 = "http://7a144ad2.ngrok.io/img_nose/"+idProveedor+"_1.jpg";


        setContentView(R.layout.activity_proveedor_data);


        final JSONObject userJson2 = new JSONObject();
        try {
            userJson2.put("idUsuario", id);
            userJson2.put("idProveedor", idProveedor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString2 = userJson2.toString();
        String url2 = "http://7a144ad2.ngrok.io/agregarVisita.php";
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
        imgFoto1 =(ImageView) findViewById(R.id.imgFoto1);
        imgFoto2 = (ImageView) findViewById(R.id.imgFoto2);
        lblEmpresa.setText(nomEmpresa);
        String concadenar = nombreProv+" "+apellidoProv;
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
                String url2 = "http://7a144ad2.ngrok.io/agregarFavoritos.php";
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

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
