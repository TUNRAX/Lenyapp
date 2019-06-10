package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Calificacion implements Parcelable {
    int id = 0;
    int idUsuario = 0;
    int idProveedor = 0;

    public Calificacion(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        idUsuario = objetoJSON.getInt("idUsuario");
        idProveedor = objetoJSON.getInt("idProveedor");
    }

    protected Calificacion(Parcel in) {
        id = in.readInt();
        idUsuario = in.readInt();
        idProveedor = in.readInt();
    }

    public static final Creator<Calificacion> CREATOR = new Creator<Calificacion>() {
        @Override
        public Calificacion createFromParcel(Parcel in) {
            return new Calificacion(in);
        }

        @Override
        public Calificacion[] newArray(int size) {
            return new Calificacion[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idUsuario);
        dest.writeInt(idProveedor);
    }
}
