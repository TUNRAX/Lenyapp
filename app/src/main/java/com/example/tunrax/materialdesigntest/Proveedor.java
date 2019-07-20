package com.example.tunrax.materialdesigntest;
/**
 * esta clase solo tiene getters and setters de proveedor
 */

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Proveedor implements Parcelable {

    private int id;
    private String nombre;
    private String apellido;
    private String nombre_empresa;
    private String rut;
    private String direccion;
    private String ciudad;
    private int calificacion;
    private int id_usuario;
    private ArrayList<Proveedor> lista_proveedores = new ArrayList<Proveedor>();


    public Proveedor(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        nombre =objetoJSON.getString("nombre");
        apellido = objetoJSON.getString("apellido");
        nombre_empresa = objetoJSON.getString("nombre_empresa");
        rut = objetoJSON.getString("rut");
        direccion = objetoJSON.getString("direccion");
        ciudad = objetoJSON.getString("ciudad");
        calificacion = objetoJSON.getInt("calificacion");
        id_usuario = objetoJSON.getInt("id_usuario");
    }

    protected Proveedor(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        apellido = in.readString();
        nombre_empresa = in.readString();
        rut = in.readString();
        direccion = in.readString();
        ciudad = in.readString();
        calificacion = in.readInt();
        id_usuario = in.readInt();
        lista_proveedores = in.createTypedArrayList(Proveedor.CREATOR);
    }

    public static final Creator<Proveedor> CREATOR = new Creator<Proveedor>() {
        @Override
        public Proveedor createFromParcel(Parcel in) {
            return new Proveedor(in);
        }

        @Override
        public Proveedor[] newArray(int size) {
            return new Proveedor[size];
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre_empresa() {
        return nombre_empresa;
    }

    public void setNombre_empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public ArrayList<Proveedor> getLista_proveedores() {
        return lista_proveedores;
    }

    public void setLista_proveedores(ArrayList<Proveedor> lista_proveedores) {
        this.lista_proveedores = lista_proveedores;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(nombre_empresa);
        dest.writeString(rut);
        dest.writeString(direccion);
        dest.writeString(ciudad);
        dest.writeInt(calificacion);
        dest.writeInt(id_usuario);
        dest.writeTypedList(lista_proveedores);
    }
}

