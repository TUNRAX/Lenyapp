package com.example.tunrax.materialdesigntest;
/**
 * esta clase solo tiene getters and setters de cliente
 */

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Cliente implements Parcelable {
    int id;
    String nombre;
    String apellido;
    String rut;
    String direccion;
    String fono;
    String ciudad;
    int idUsuario;

    public Cliente(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        nombre =objetoJSON.getString("nombre");
        apellido =objetoJSON.getString("apellido");
        rut = objetoJSON.getString("rut");
        direccion = objetoJSON.getString("direccion");
        fono = objetoJSON.getString("fono");
        ciudad = objetoJSON.getString("ciudad");
        idUsuario = objetoJSON.getInt("id_usuario");
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFono() {
        return fono;
    }

    public void setFono(String fono) {
        this.fono = fono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public static Creator<Cliente> getCREATOR() {
        return CREATOR;
    }

    protected Cliente(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        apellido = in.readString();
        rut = in.readString();
        direccion = in.readString();
        fono = in.readString();
        ciudad = in.readString();
        idUsuario = in.readInt();
    }

    public static final Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(rut);
        dest.writeString(direccion);
        dest.writeString(fono);
        dest.writeString(ciudad);
        dest.writeInt(idUsuario);
    }
}
