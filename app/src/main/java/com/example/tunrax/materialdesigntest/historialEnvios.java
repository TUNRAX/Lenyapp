package com.example.tunrax.materialdesigntest;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class historialEnvios implements Parcelable {
    int id;
    String fecha;
    int validado;
    int idCliente;
    int idDetalleProducto;

    public historialEnvios() {
    }

    public historialEnvios(int id, String fecha, int validado, int idCliente, int idDetalleProducto) {
        this.id = id;
        this.fecha = fecha;
        this.validado = validado;
        this.idCliente = idCliente;
        this.idDetalleProducto = idDetalleProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getValidado() {
        return validado;
    }

    public void setValidado(int validado) {
        this.validado = validado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdDetalleProducto() {
        return idDetalleProducto;
    }

    public void setIdDetalleProducto(int idDetalleProducto) {
        this.idDetalleProducto = idDetalleProducto;
    }

    public static Creator<historialEnvios> getCREATOR() {
        return CREATOR;
    }

    protected historialEnvios(Parcel in) {
        id = in.readInt();
        fecha = in.readString();
        validado = in.readInt();
        idCliente = in.readInt();
        idDetalleProducto = in.readInt();
    }

    public static final Creator<historialEnvios> CREATOR = new Creator<historialEnvios>() {
        @Override
        public historialEnvios createFromParcel(Parcel in) {
            return new historialEnvios(in);
        }

        @Override
        public historialEnvios[] newArray(int size) {
            return new historialEnvios[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public historialEnvios(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        fecha =objetoJSON.getString("fecha");
        validado =objetoJSON.getInt("validado");
        idCliente = objetoJSON.getInt("id_cliente");
        idDetalleProducto = objetoJSON.getInt("id_detalle_producto");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fecha);
        dest.writeInt(validado);
        dest.writeInt(idCliente);
        dest.writeInt(idDetalleProducto);
    }
}
