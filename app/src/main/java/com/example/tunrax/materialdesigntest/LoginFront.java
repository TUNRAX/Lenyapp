package com.example.tunrax.materialdesigntest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFront.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFront#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Este fragment contiene los campos para poder hacer un inicio de sesion
 * en la aplicacion, contiene un boton que redirecciona a la pantalla de registro
 *
 * Si el inicio de sesion es correcto, se redireccionara a la clase del mapa enviando
 * los datos de los proveedores
 */
public class LoginFront extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtCorreo;
    private EditText txtContrasenya;
    private Button btnIngresar;
    private FloatingActionButton btnRegistrarse;

    boolean loginOk = false;
    int id = 0;
    String correo = "";

    private OnFragmentInteractionListener mListener;

    public LoginFront() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFront.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFront newInstance(String param1, String param2) {
        LoginFront fragment = new LoginFront();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtCorreo = (EditText) getView().findViewById(R.id.txtCorreo);
        txtContrasenya = (EditText) getView().findViewById(R.id.txtContrasenya);
        btnIngresar = (Button) getView().findViewById(R.id.btnIngresar);
        btnRegistrarse = (FloatingActionButton) getView().findViewById(R.id.btnRegistrarse);
        final ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        final ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), Registro.class);
                startActivity(i);
            }
        });


        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = txtCorreo.getText().toString();
                final String pass = txtContrasenya.getText().toString();
                int rol = 0;
                //thread rol
                final int[] rolObtenidoLOL = {0};
                Thread thread1 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            final String resultadoCheck = Check(user, pass);
                            JSONObject objetoJSON = new JSONObject();
                            JSONArray arr = new JSONArray(resultadoCheck);
                            JSONObject jObj = arr.getJSONObject(0);
                            for (int i=0; i < arr.length(); i++) {
                                JSONObject oneObject = arr.getJSONObject(i);
                                // Pulling items from the array
                                int rolObtenido = oneObject.getInt("id_rol");
                                rolObtenidoLOL[0] = rolObtenido;
                            }

                        } catch (JSONException e) {
                            Log.e("app", "exception", e);
                        }


                    }
                };
                thread1.start();

                //fin thread rol
                while(thread1.getState()!=Thread.State.TERMINATED) {
                    rol = rolObtenidoLOL[0];
                }
                if(thread1.getState()!=Thread.State.TERMINATED){
                    rol = rolObtenidoLOL[0];
                }
                    if (user.equals("") || pass.equals("")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Rellene los campos nesesarios", Toast.LENGTH_SHORT).show();
                    } else if(rol == 2) {
                        final JSONObject userJson1 = new JSONObject();
                        try {
                            userJson1.put("user", user);
                            userJson1.put("pass", pass);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String jsonString1 = userJson1.toString();
                        String url1 = "http://fd668ba1.sa.ngrok.io/login.php";
                        try {
                            Back ejec = new Back(new Back.AsyncResponse() {
                                @Override
                                public void processFinish(String jsonResult) {
                                    String correoInterno = "";
                                    correo = correoInterno;
                                    int idUsuario = 0;
                                    id = idUsuario;
                                    String contrasenya = "";
                                    try {
                                        JSONObject jsonObjectUsuario = new JSONObject(jsonResult);
                                        JSONArray jsonArrayUsuario = jsonObjectUsuario.getJSONArray("usuario");
                                        for (int x = 0; x < jsonArrayUsuario.length(); x++) {
                                            listaUsuario.add(new Usuario(jsonArrayUsuario.getJSONObject(x)));
                                        }

                                        for (int i = 0; i < listaUsuario.size(); i++) {
                                            correoInterno = listaUsuario.get(i).getCorreo();
                                            contrasenya = listaUsuario.get(i).getContrasenya();
                                        }
                                        correo = correoInterno;
                                        String passEncriptada = AeSimpleSHA1.SHA1(pass);
                                        for (int i = 0; i < listaUsuario.size(); i++) {
                                            idUsuario = listaUsuario.get(i).getId();
                                        }
                                        id = idUsuario;
                                        //comparamos los campos
                                        if (user.equals(correoInterno) && passEncriptada.equals(contrasenya)) {
                                            loginOk = true;
                                            Toast.makeText(getActivity().getApplicationContext(), "Cargando... por favor espere", Toast.LENGTH_LONG).show();
                                            if (loginOk) {
                                                String jsonString2 = "yolo";
                                                String url2 = "http://fd668ba1.sa.ngrok.io/ListaProveedores.php";
                                                try {
                                                    Back ejec2 = new Back(new Back.AsyncResponse() {
                                                        @Override
                                                        public void processFinish(String jsonResult) {
                                                            try {
                                                                JSONObject jsonObjectProveedor = new JSONObject(jsonResult);
                                                                JSONArray jsonArrayProveedor = jsonObjectProveedor.getJSONArray("vendedores");
                                                                for (int x = 0; x < jsonArrayProveedor.length(); x++) {
                                                                    lista.add(new Proveedor(jsonArrayProveedor.getJSONObject(x)));
                                                                }

                                                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                                editor.putInt("id", id);
                                                                editor.putString("correo", correo);
                                                                editor.apply();


                                                                Intent i = new Intent(getActivity().getApplicationContext(), MapActivity.class);
                                                                i.putExtra("lista", lista);
                                                                startActivity(i);
                                                            } catch (Exception e) {
                                                                Toast.makeText(getActivity().getApplicationContext(), "Error al Insertar Datos, intente nuevamente.", Toast.LENGTH_LONG).show();
                                                                Log.e("app", "exception", e);
                                                            }
                                                        }
                                                    });
                                                    ejec2.execute(url2, jsonString2);

                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Error inesperado.", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity().getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Error inesperado, comuniquese con administrador.", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                            ejec.execute(url1, jsonString1);

                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error al Insertar Datos, intente nuevamente.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "Acceso denegado, dirijase al sitio web", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_front, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
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

    public String Check(String user, String pass) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("http://fd668ba1.sa.ngrok.io/checkearRol.php?correo=" + user
                    + "&contrasenya=" + pass);
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
        }
        return resultado.toString();
    }


    public String Login(String user, String pass) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resultado = null;

        try {

            url = new URL("http://fd668ba1.sa.ngrok.io/checkearRol.php?correo=" + user
                    + "&contrasenya=" + pass);
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
        }
        return resultado.toString();
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
        void onFragmentInteraction(int position);
    }
}
