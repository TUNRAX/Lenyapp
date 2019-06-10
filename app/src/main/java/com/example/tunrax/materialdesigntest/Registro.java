package com.example.tunrax.materialdesigntest;
/**
 * Esta activity solo contiene el fondo del registro, nada mas
 */

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Registro extends AppCompatActivity implements RegisterFront.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
