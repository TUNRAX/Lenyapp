package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class tipoProducto implements Parcelable {
    private int id;
    private String nombre;

    public tipoProducto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public tipoProducto() {
    }

    protected tipoProducto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public tipoProducto(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        nombre =objetoJSON.getString("nombre");

    }

    public static final Creator<tipoProducto> CREATOR = new Creator<tipoProducto>() {
        @Override
        public tipoProducto createFromParcel(Parcel in) {
            return new tipoProducto(in);
        }

        @Override
        public tipoProducto[] newArray(int size) {
            return new tipoProducto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
    }
}
