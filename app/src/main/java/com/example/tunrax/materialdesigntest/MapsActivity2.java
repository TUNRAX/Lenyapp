package com.example.tunrax.materialdesigntest;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private ImageButton btnBrujula;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 6000;  /* 6 segundos */
    private long FASTEST_INTERVAL = 3000; /* 3 segundos */
    double latitude = 0;
    double longitude = 0;
    int tipoDeCompra = 0;
    int idHistorial = 0;
    int precioOficial = 0;
    boolean yaEjecutado = false;


    private LocationManager locationManager;
    private LatLng latLng;
    LatLng latLngUsuario;
    private Marker marker;
    private Marker marker2;
    private boolean isPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String correo = prefs.getString("correo", " ");//"No name defined" is the default value.
        final int id = prefs.getInt("id", 0); //0 is the default value.
        Bundle bundle = getIntent().getExtras();
        tipoDeCompra = bundle.getInt("tipoCompra");
        idHistorial = bundle.getInt("idHistorial");
        precioOficial = bundle.getInt("precio");
        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //it was pre written
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation(); //check whether location service is enable or not in your  phone



        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //it was pre written
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (latLng != null) {
            if(marker==null){

                MarkerOptions userIndicator = new MarkerOptions()
                        .position(latLng)
                        .title("Leñador")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.deliverytruck));
                marker = googleMap.addMarker(userIndicator);

                MarkerOptions userIndicator2 = new MarkerOptions()
                        .position(latLngUsuario)
                        .title("Usted")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
                marker2 = googleMap.addMarker(userIndicator2);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(latLng);
                builder.include(latLngUsuario);
                final LatLngBounds bounds = builder.build();

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                    public void onCancel(){}
                    public void onFinish(){
                        CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -0.2);
                        mMap.animateCamera(zout);
                    }
                });
            }else {
                marker.remove();
                marker2.remove();
                MarkerOptions userIndicator = new MarkerOptions()
                        .position(latLng)
                        .title("Leñador")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.deliverytruck));
                marker = googleMap.addMarker(userIndicator);


                MarkerOptions userIndicator2 = new MarkerOptions()
                        .position(latLngUsuario)
                        .title("Usted")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
                marker2 = googleMap.addMarker(userIndicator2);

            }

            if(marker.isVisible() && marker2.isVisible()){
                if(!yaEjecutado) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(latLng);
                    builder.include(latLngUsuario);
                    final LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                        public void onCancel(){}
                        public void onFinish(){
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -0.2);
                            mMap.animateCamera(zout);
                        }
                    });
                    yaEjecutado= true;
                }
            }
            btnBrujula = (ImageButton) findViewById(R.id.btnBrujula);
            btnBrujula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(latLng);
                    builder.include(latLngUsuario);
                    final LatLngBounds bounds = builder.build();

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                        public void onCancel(){}
                        public void onFinish(){
                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -0.2);
                            mMap.animateCamera(zout);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

        } else {
            Toast.makeText(this, "Localizacion No detectada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Conexion suspendida");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Conexion fallida. Error: " + connectionResult.getErrorCode());
    }


    @Override
    public void onLocationChanged(Location location ) {
        final ArrayList<Coordenadas> listaCoordenadas = new ArrayList<Coordenadas>();
        final String latitudOut = String.valueOf(location.getLatitude());
        final String longitudOut = String.valueOf(location.getLongitude());
        double latitdUsu = Double.parseDouble(latitudOut);
        double longitudUsu = Double.parseDouble(longitudOut);
        latLngUsuario = new LatLng(latitdUsu, longitudUsu);
        Location leñadorGEO = new Location(LocationManager.GPS_PROVIDER);
        leñadorGEO.setLatitude(latitude);
        leñadorGEO.setLongitude(longitude);
        Location usuarioGEO = new Location(LocationManager.GPS_PROVIDER);
        usuarioGEO.setLatitude(latitdUsu);
        usuarioGEO.setLongitude(longitudUsu);

        if(leñadorGEO.distanceTo(usuarioGEO) < 100) {

        Intent i = new Intent(getApplicationContext(), trackingRealizado.class);
            i.putExtra("idHistorial", idHistorial);
            i.putExtra("tipoCompra", tipoDeCompra);
            i.putExtra("precio",precioOficial);
        startActivity(i);

        }
    // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://865e33a1.sa.ngrok.io/trackingUsuario.php?idHistorial="+ idHistorial +"&lat="+ latitudOut +"&long="+ longitudOut;

    // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity2.this, "Error volley", Toast.LENGTH_SHORT).show();
            }
        });

    // Add the request to the RequestQueue.
        queue.add(stringRequest);

        ////////////////////////////////////////////////////////
        // Instantiate the RequestQueue.
        RequestQueue queue1 = Volley.newRequestQueue(this);
        String url1 = "https://865e33a1.sa.ngrok.io/selectTrackingUsuario.php?idHistorial="+ idHistorial;

        // Request a string response from the provided URL.
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        String resultadoCoordenadas = response;
                        try {
                            JSONObject jsonObjectCoordenadas = new JSONObject(resultadoCoordenadas);
                            JSONArray jsonArrayCoordenadas = jsonObjectCoordenadas.getJSONArray("coordenadas");
                            for (int x = 0; x < jsonArrayCoordenadas.length(); x++) {
                                listaCoordenadas.add(new Coordenadas(jsonArrayCoordenadas.getJSONObject(x)));
                            }


                        } catch (Exception e) {
                            Log.e("app", "exception", e);
                        }
                        try {

                            for (int i = 0; i < listaCoordenadas.size(); i++) {

                                String latitud = listaCoordenadas.get(i).getLatitud();
                                String longitud = listaCoordenadas.get(i).getLongitud();
                                latitude = Double.parseDouble(latitud);
                                longitude = Double.parseDouble(longitud);

                                latLng = new LatLng(latitude, longitude);


                            }
                        } catch (Exception e) {
                            Log.e("app", "exception", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity2.this, "Fallo Inesperado", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue1.add(stringRequest1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Habilitar localizacion")
                .setMessage("Tienes tu localizacion de tu movil apagada.\nPor favor habilitala para " +
                        "usar esta aplicacion")
                .setPositiveButton("Configuraciones de locaclizacion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //aceptacion del permiso
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // Denegacion del permiso
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        return isPermission;

    }


}
