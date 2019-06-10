package com.example.tunrax.materialdesigntest;
/**
 * Esta clase solo tiene los setters and getters de usuario
 */

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario implements Parcelable{
    int id;
    String correo;
    String contrasenya;
    String idRol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }

    public static Creator<Usuario> getCREATOR() {
        return CREATOR;
    }

    public Usuario (JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        correo =objetoJSON.getString("correo");
        contrasenya = objetoJSON.getString("contrasenya");
        idRol = objetoJSON.getString("id_rol");
    }

    protected Usuario(Parcel in) {
        id = in.readInt();
        correo = in.readString();
        contrasenya = in.readString();
        idRol = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(correo);
        dest.writeString(contrasenya);
        dest.writeString(idRol);
    }
}
