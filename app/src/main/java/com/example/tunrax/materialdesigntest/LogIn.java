package com.example.tunrax.materialdesigntest;
/**
 * Esta activity solo contiene el fondo del login
 * no actua de manera directa con la aplicacion
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogIn extends AppCompatActivity implements LoginFront.OnFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    @Override
    public void onFragmentInteraction(int position) {

    }
}
