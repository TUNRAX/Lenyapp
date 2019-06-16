package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Coordenadas implements Parcelable {
    String Latitud = "";
    String Longitud = "";
    private ArrayList<Coordenadas> lista_coordenadas = new ArrayList<Coordenadas>();
    public Coordenadas() {
    }

    public Coordenadas(String latitud, String longitud) {
        Latitud = latitud;
        Longitud = longitud;
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
        lista_coordenadas = in.createTypedArrayList(Coordenadas.CREATOR);

    }
    public void setLista_coordenadas(ArrayList<Coordenadas> lista_coordenadas) {
        this.lista_coordenadas = lista_coordenadas;
    }
}
