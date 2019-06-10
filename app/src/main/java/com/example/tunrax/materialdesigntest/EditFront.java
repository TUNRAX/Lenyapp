package com.example.tunrax.materialdesigntest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFront.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFront#newInstance} factory method to
 * create an instance of this fragment.
 * <p>
 * Este fragment tiene diferentes campos de texto para
 * registrar a un usuario el cual se hara al momento de presionar
 * el boton inferior de la activity cuando los datos esten llenos (TODO)
 */
public class EditFront extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtNombreEdit, txtApellidoEdit, txtDireccionEdit, txtCiudadEdit, txtFonoEdit, txtContrasenyaEdit, txtRepContrasenyaEdit;
    private Button btnRealizar;

    int idUsu;

    private OnFragmentInteractionListener mListener;

    public EditFront() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFront.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFront newInstance(String param1, String param2) {
        EditFront fragment = new EditFront();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_front, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (EditFront.OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtNombreEdit = (EditText) getView().findViewById(R.id.txtNombreEdit);
        txtApellidoEdit = (EditText) getView().findViewById(R.id.txtApellidoEdit);
        txtDireccionEdit = (EditText) getView().findViewById(R.id.txtDireccionEdit);
        txtCiudadEdit = (EditText) getView().findViewById(R.id.txtCiudadEdit);
        txtFonoEdit = (EditText) getView().findViewById(R.id.txtFonoEdit);
        txtContrasenyaEdit = (EditText) getView().findViewById(R.id.txtContrasenyaEdit);
        txtRepContrasenyaEdit = (EditText) getView().findViewById(R.id.txtRepContrasenyaEdit);
        btnRealizar = (Button) getView().findViewById(R.id.btnRealizar);
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefsFile", getContext().MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        idUsu = id;
        Bundle bundle = getActivity().getIntent().getExtras();
        String nombre = bundle.getString("nombre");
        String apellido = bundle.getString("apellido");
        String direccion = bundle.getString("direccion");
        String fono = bundle.getString("fono");
        String ciudad = bundle.getString("ciudad");
        String contrasenya = bundle.getString("contrasenya");
        txtNombreEdit.setText(nombre);
        txtApellidoEdit.setText(apellido);
        txtDireccionEdit.setText(direccion);
        txtFonoEdit.setText(fono);
        txtCiudadEdit.setText(ciudad);
        txtContrasenyaEdit.setText(contrasenya);
        txtRepContrasenyaEdit.setText(contrasenya);

        btnRealizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto1 = txtContrasenyaEdit.getText().toString();
                String texto2 = txtRepContrasenyaEdit.getText().toString();
                final String nombreRecibido = txtNombreEdit.getText().toString();
                final String apellidoRecibido = txtApellidoEdit.getText().toString();
                final String direccionRecibido = txtDireccionEdit.getText().toString();
                final String fonoRecibido = txtFonoEdit.getText().toString();
                final String ciudadRecibido = txtCiudadEdit.getText().toString();
                final String contrasenyaRecibido = txtContrasenyaEdit.getText().toString();
                if (nombreRecibido.equals("") || apellidoRecibido.equals("") || direccionRecibido.equals("") || fonoRecibido.equals("") || ciudadRecibido.equals("") || texto1.equals("") || texto2.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Rellene los campos nesesarios", Toast.LENGTH_SHORT).show();
                } else {
                    if (texto1.equals(texto2)) {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                actualizarUsuario(nombreRecibido, apellidoRecibido, direccionRecibido, fonoRecibido, ciudadRecibido, contrasenyaRecibido, idUsu);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Cambio Sus datos con exito", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        };
                        thread.start();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void actualizarUsuario(String nombreRecibido, String apellidoRecibido, String direccionRecibido, String fonoRecibido, String ciudadRecibido, String contrasenyaRecibido, int idUsu) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = ("http://7a144ad2.ngrok.io/actualizarUsuario.php?idUsuario=" + idUsu
                    + "&nombre=" + nombreRecibido
                    + "&apellido=" + apellidoRecibido
                    + "&direccion=" + direccionRecibido
                    +   "&ciudad="  +   ciudadRecibido
                    + "&fono=" + fonoRecibido
                    + "&contrasenya=" + contrasenyaRecibido);
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

    public String RegistroCliente(String nombre, String apellido, String rut, String direccion, String fono, String ciudad, String correo, String contrasenya) {

        String url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url =("http://7a144ad2.ngrok.io/RegistroCliente.php?nombre="+nombre
                    +"&apellido="+apellido
                    +"&rut="+rut
                    +"&direccion="+direccion
                    +"&fono="+fono
                    +"&ciudad="+ciudad
                    +"&correo="+correo
                    +"&contrasenya="+contrasenya);
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
