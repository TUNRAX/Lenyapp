package com.example.tunrax.materialdesigntest;
/**
 * Esta activity es el fondo del editar al usuario, nada mas
 */

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Edit extends AppCompatActivity implements EditFront.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
