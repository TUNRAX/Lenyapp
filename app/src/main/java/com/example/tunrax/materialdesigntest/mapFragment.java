package com.example.tunrax.materialdesigntest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mapFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Este fragment tiene un mapa de google el cual muestra las
 * localizaciones de los proveedores y al apretar un marker
 * muestra un mensaje, el cual si es apretado lleva a los datos de ese proveedor
 *
 */
public class mapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    MapView mapView;

    private GoogleMap mMap;
    private Marker marca;
    private double latitude;
    private double longitude;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public mapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mapFragment newInstance(String param1, String param2) {
        mapFragment fragment = new mapFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        //initialize map
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {

            public double latitude;
            public double longitude;

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                try {
                    String direccion="osorno, chile";
                    Geocoder gc=new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses;
                    addresses=gc.getFromLocationName(direccion,5);
                    if (addresses.size() > 0) {
                        this.latitude=addresses.get(0).getLatitude();
                        this.longitude=addresses.get(0).getLongitude();
                        LatLng coord=new LatLng(latitude,longitude);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 12));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bundle b = getActivity().getIntent().getExtras();

                String direccion = "";
                String vendedor = "";
                String dir = "";
                final ArrayList<Proveedor> lista = b.getParcelableArrayList("lista");
                final ArrayList<Proveedor> lista2 = new ArrayList<Proveedor>();

                Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses;


                try {
                    for (int i = 0; i < lista.size(); i++) {
                        direccion = lista.get(i).getDireccion() + "," + lista.get(i).getCiudad() + ",Chile";
                        dir = lista.get(i).getDireccion();
                        addresses = gc.getFromLocationName(direccion, 5);
                        vendedor = lista.get(i).getNombre();
                        if (addresses.size() > 0) {
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                            LatLng coord = new LatLng(latitude, longitude);
                            marca = googleMap.addMarker(new MarkerOptions()
                                    .position(coord)
                                    .title(vendedor)
                                    .snippet(dir));
                            marca.setTag(0);
                        }
                    }
                } catch (IOException e) {
                    Log.e("app", "exception", e);
                }
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marca) {
                        final String titulo = marca.getTitle();
                        final String snipplet = marca.getSnippet();
                        final ArrayList<Proveedor> listaProveedor = new ArrayList<Proveedor>();
                        final ArrayList<DatosLenya> listaDatos = new ArrayList<DatosLenya>();
                        final JSONObject userJson1 = new JSONObject();
                        try {
                            userJson1.put("nombre", titulo);
                            userJson1.put("direccion", snipplet);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String jsonString1 = userJson1.toString();
                        String url1 = "http://7a144ad2.ngrok.io/BuscarProveedor.php";
                        try {
                            Back ejec = new Back(new Back.AsyncResponse() {
                                @Override
                                public void processFinish(String jsonResult) {
                                    try {
                                        int idProveedor = 0;
                                        String nombreProv = "";
                                        String apellidoProv = "";
                                        String nomEmpresa = "";
                                        String rut = "";
                                        String direccion = "";
                                        String fono1 = "";
                                        String fono2 = "";
                                        String ciudad = "";
                                        int calificacion = 0;
                                        JSONObject jsonObjectProveedor = new JSONObject(jsonResult);
                                        JSONArray jsonArrayProveedor = jsonObjectProveedor.getJSONArray("proveedor");
                                        for (int x = 0; x < jsonArrayProveedor.length(); x++) {
                                            listaProveedor.add(new Proveedor(jsonArrayProveedor.getJSONObject(x)));
                                        }

                                        for (int i = 0; i < listaProveedor.size(); i++) {
                                            idProveedor = listaProveedor.get(i).getId();
                                            nombreProv = listaProveedor.get(i).getNombre();
                                            apellidoProv = listaProveedor.get(i).getApellido();
                                            nomEmpresa = listaProveedor.get(i).getNombre_empresa();
                                            rut = listaProveedor.get(i).getRut();
                                            direccion = listaProveedor.get(i).getDireccion();
                                            fono1 = listaProveedor.get(i).getFono1();
                                            fono2 = listaProveedor.get(i).getFono2();
                                            ciudad = listaProveedor.get(i).getCiudad();
                                            calificacion = listaProveedor.get(i).getCalificacion();
                                        }
                                        final int idProveedorPass = idProveedor;
                                        final String nombreProvPass = nombreProv;
                                        final String apellidoProvPass = apellidoProv;
                                        final String nomEmpresaPass = nomEmpresa;
                                        final String rutPass = rut;
                                        final String direccionPass = direccion;
                                        final String fono1Pass = fono1;
                                        final String fono2Pass = fono2;
                                        final String ciudadPass = ciudad;
                                        final int calificacionPass = calificacion;


                                        int id = idProveedor;
                                        final JSONObject userJson2 = new JSONObject();
                                        try {
                                            userJson2.put("id", id);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String jsonString2 = userJson2.toString();
                                        String url2 = "http://7a144ad2.ngrok.io/BuscarLenya.php";
                                        try {
                                            Back ejec2 = new Back(new Back.AsyncResponse() {
                                                @Override
                                                public void processFinish(String jsonResult2) {
                                                    int precioUnitario = 0;
                                                    int ventaMinima = 0;
                                                    String producto = "";
                                                    String medida = "";
                                                    try {
                                                        JSONObject jsonObjectDatos = new JSONObject(jsonResult2);
                                                        JSONArray jsonArrayDatos = jsonObjectDatos.getJSONArray("lenya");
                                                        for (int x = 0; x < jsonArrayDatos.length(); x++) {
                                                            listaDatos.add(new DatosLenya(jsonArrayDatos.getJSONObject(x)));
                                                        }

                                                        for (int i = 0; i < listaDatos.size(); i++) {
                                                            precioUnitario = listaDatos.get(i).getPrecioUnitario();
                                                            ventaMinima = listaDatos.get(i).getVentaMinima();
                                                            producto = listaDatos.get(i).getProducto();
                                                            medida = listaDatos.get(i).getMedida();
                                                        }

                                                        Intent i = new Intent(getActivity().getApplicationContext(), ProveedorData.class);
                                                        i.putExtra("idProveedor", idProveedorPass);
                                                        i.putExtra("nombreProv", nombreProvPass);
                                                        i.putExtra("apellidoProv", apellidoProvPass);
                                                        i.putExtra("nomEmpresa", nomEmpresaPass);
                                                        i.putExtra("rut", rutPass);
                                                        i.putExtra("direccion", direccionPass);
                                                        i.putExtra("fono1", fono1Pass);
                                                        i.putExtra("fono2", fono2Pass);
                                                        i.putExtra("ciudad", ciudadPass);
                                                        i.putExtra("calificacion", calificacionPass);
                                                        i.putExtra("precioUnitario", precioUnitario);
                                                        i.putExtra("ventaMinima", ventaMinima);
                                                        i.putExtra("producto", producto);
                                                        i.putExtra("medida", medida);


                                                        startActivity(i);
                                                    } catch (Exception e) {
                                                        Toast.makeText(getActivity().getApplicationContext(), "Error al cambiar de pagina.", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });
                                            ejec2.execute(url2, jsonString2);

                                        } catch (Exception e) {
                                            Toast.makeText(getActivity().getApplicationContext(), "que se yo que esta pasando", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), "BOI", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            ejec.execute(url1, jsonString1);

                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error capa 8", Toast.LENGTH_LONG).show();

                        }
                    }
                });


                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
            mListener = (mapFragment.OnFragmentInteractionListener) getActivity();
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
