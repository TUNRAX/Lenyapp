package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Coordenadas implements Parcelable {
    String Latitud = "";
    String Longitud = "";
    String lat_usu = "";
    String long_usu = "";
    private ArrayList<Coordenadas> lista_coordenadas = new ArrayList<Coordenadas>();
    public Coordenadas() {
    }

    public Coordenadas(String latitud, String longitud) {
        Latitud = latitud;
        Longitud = longitud;
        lat_usu = lat_usu;
        long_usu = long_usu;
    }

    public String getLat_usu() {
        return lat_usu;
    }

    public void setLat_usu(String lat_usu) {
        this.lat_usu = lat_usu;
    }

    public String getLong_usu() {
        return long_usu;
    }

    public void setLong_usu(String long_usu) {
        this.long_usu = long_usu;
    }

    public static Creator<Coordenadas> getCREATOR() {
        return CREATOR;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public ArrayList<Coordenadas> getLista_coordenadas() {
        return lista_coordenadas;
    }

    public Coordenadas(JSONObject objetoJSON)throws JSONException {
        Latitud =objetoJSON.getString("lat");
        Longitud =objetoJSON.getString("long");
        lat_usu =objetoJSON.getString("lat_usu");
        long_usu= objetoJSON.getString("long_usu");
    }


    public static final Creator<Coordenadas> CREATOR = new Creator<Coordenadas>() {
        @Override
        public Coordenadas createFromParcel(Parcel in) {
            return new Coordenadas(in);
        }

        @Override
        public Coordenadas[] newArray(int size) {
            return new Coordenadas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Latitud);
        dest.writeString(Longitud);
        dest.writeTypedList(lista_coordenadas);
    }

    protected Coordenadas(Parcel in) {
        Latitud = in.readString();
        Longitud = in.readString();
        lat_usu = in.readString();
        long_usu = in.readString();
        lista_coordenadas = in.createTypedArrayList(Coordenadas.CREATOR);

    }
    public void setLista_coordenadas(ArrayList<Coordenadas> lista_coordenadas) {
        this.lista_coordenadas = lista_coordenadas;
    }
}
