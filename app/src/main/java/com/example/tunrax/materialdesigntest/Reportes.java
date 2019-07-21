package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Reportes implements Parcelable {
    int id = 0;
    String titulo = "";
    String descripcion = "";
    int idUsuario = 0;

    public Reportes (JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        titulo =objetoJSON.getString("titulo");
        descripcion = objetoJSON.getString("descripcion");
        idUsuario = objetoJSON.getInt("id_usuario");
    }

    protected Reportes(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        descripcion = in.readString();
        idUsuario = in.readInt();
    }

    public static final Creator<Reportes> CREATOR = new Creator<Reportes>() {
        @Override
        public Reportes createFromParcel(Parcel in) {
            return new Reportes(in);
        }

        @Override
        public Reportes[] newArray(int size) {
            return new Reportes[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeInt(idUsuario);
    }
}
