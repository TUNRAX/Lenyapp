package com.example.tunrax.materialdesigntest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFront.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFront#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Este fragment tiene diferentes campos de texto los cuales al presionar
 * el boton de la parte de abajo de la pantalla añade los datos de un cliente y su
 * usuario para hacer login
 */
public class RegisterFront extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtNombre, txtApellido, txtDireccion, txtCiudad, txtRut, txtCorreo, txtContrasenyaReg, txtRepContrasenya, txtFono;
    private Button btnGuardar;

    private OnFragmentInteractionListener mListener;

    public RegisterFront() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFront.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFront newInstance(String param1, String param2) {
        RegisterFront fragment = new RegisterFront();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtNombre = (EditText) getView().findViewById(R.id.txtNombre);
        txtApellido = (EditText) getView().findViewById(R.id.txtApellido);
        txtDireccion = (EditText) getView().findViewById(R.id.txtDireccion);
        txtCiudad = (EditText) getView().findViewById(R.id.txtCiudad);
        txtRut = (EditText) getView().findViewById(R.id.txtRut);
        txtCorreo = (EditText) getView().findViewById(R.id.txtCorreo);
        txtContrasenyaReg = (EditText) getView().findViewById(R.id.txtContrasenyaReg);
        txtRepContrasenya = (EditText) getView().findViewById(R.id.txtRepContrasenya);
        txtFono = (EditText) getView().findViewById(R.id.txtFono);
        btnGuardar = (Button) getView().findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreRecibido = txtNombre.getText().toString();
                String apellidoRecibido = txtApellido.getText().toString();
                String rutRecibido = txtRut.getText().toString();
                String direccionRecibido = txtDireccion.getText().toString();
                String fonoRecibido = txtFono.getText().toString();
                String ciudadRecibido = txtCiudad.getText().toString();
                String correoRecibido = txtCorreo.getText().toString();
                String contra1Recibido = txtContrasenyaReg.getText().toString();
                String contra2Recibido = txtRepContrasenya.getText().toString();
                boolean rut = validarRut(rutRecibido);
                if (nombreRecibido.equals("") || apellidoRecibido.equals("") || rutRecibido.equals("") || direccionRecibido.equals("") || fonoRecibido.equals("") || correoRecibido.equals("") || ciudadRecibido.equals("") || contra1Recibido.equals("") || contra2Recibido.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Rellene los campos nesesarios", Toast.LENGTH_SHORT).show();
                } else if (rut) {
                    if (txtContrasenyaReg.getText().toString().equals(txtRepContrasenya.getText().toString())) {
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                final String resultado = RegistroCliente(txtNombre.getText().toString(),
                                        txtApellido.getText().toString(),
                                        txtRut.getText().toString(),
                                        txtDireccion.getText().toString(),
                                        txtFono.getText().toString(),
                                        txtCiudad.getText().toString(),
                                        txtCorreo.getText().toString(),
                                        txtContrasenyaReg.getText().toString());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int respuesta = DatosJSON(resultado);
                                        if (respuesta > 0) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Datos creados con exito", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "Error al crear los datos", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        };
                        thread.start();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "El rut es invalido", Toast.LENGTH_SHORT).show();
                }
            
            }
        });
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

    public static boolean validarRut(String rutRecibido) {

        boolean validacion = false;
        try {
            rutRecibido =  rutRecibido.toUpperCase();
            rutRecibido = rutRecibido.replace(".", "");
            rutRecibido = rutRecibido.replace("-", "");
            int rutAux = Integer.parseInt(rutRecibido.substring(0, rutRecibido.length() - 1));

            char dv = rutRecibido.charAt(rutRecibido.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    public int DatosJSON(String response) {
        int resultado = 0;

        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                resultado = 1;
            }
        } catch (Exception e) {
        }

        return resultado;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_front, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (RegisterFront.OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
}
