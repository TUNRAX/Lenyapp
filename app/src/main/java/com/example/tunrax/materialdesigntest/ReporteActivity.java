package com.example.tunrax.materialdesigntest;
/**
 * Esta activity tiene dos campos los cuales serviran para enviar
 * un correo a los administradores de la aplicacion, al llenar los campos y apretar
 * el boton se enviara un correo electronico con los datos insertados. (TODO)
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ReporteActivity extends AppCompatActivity {

    private EditText txtTitulo;
    private EditText txtDescripcion;
    private Button btnEnviar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        final String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                    }

                    {

                    }
                };
                thread.start();
                Log.i("SendMailActivity", "Send Button Clicked.");

                String fromEmail = "reportemail@lenapp.cl";
                String fromPassword = "callo33LOL";
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

    public void respaldarReporte(String titulo, String descripcion, int id) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://b227b69e.ngrok.io/respaldarReporte.php?idUsuario=" + id
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


