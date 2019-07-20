package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class imgProducto implements Parcelable {
    int id;
    String nombre;


    public imgProducto() {
    }

    public imgProducto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

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


    public static Creator<imgProducto> getCREATOR() {
        return CREATOR;
    }

    protected imgProducto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public static final Creator<imgProducto> CREATOR = new Creator<imgProducto>() {
        @Override
        public imgProducto createFromParcel(Parcel in) {
            return new imgProducto(in);
        }

        @Override
        public imgProducto[] newArray(int size) {
            return new imgProducto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public imgProducto(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        nombre = objetoJSON.getString("nombre");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
    }
}
