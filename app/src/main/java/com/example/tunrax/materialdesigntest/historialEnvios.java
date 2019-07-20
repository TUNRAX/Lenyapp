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
    String tipoDeCompra;
    int cantidad;
    int pagado;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoDeCompra() {
        return tipoDeCompra;
    }

    public void setTipoDeCompra(String tipoDeCompra) {
        this.tipoDeCompra = tipoDeCompra;
    }

    public int getPagado() {
        return pagado;
    }

    public void setPagado(int pagado) {
        this.pagado = pagado;
    }

    public historialEnvios() {
    }

    public historialEnvios(int id, String fecha, int validado, int idCliente, int idDetalleProducto, int cantidad) {
        this.id = id;
        this.fecha = fecha;
        this.validado = validado;
        this.idCliente = idCliente;
        this.idDetalleProducto = idDetalleProducto;
        this.tipoDeCompra = tipoDeCompra;
        this.cantidad = cantidad;
        this.pagado = pagado;
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
        tipoDeCompra = in.readString();
        cantidad = in.readInt();
        pagado = in.readInt();
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
        validado =objetoJSON.getInt("estado");
        idCliente = objetoJSON.getInt("id_cliente");
        idDetalleProducto = objetoJSON.getInt("id_detalle_producto");
        tipoDeCompra = objetoJSON.getString("tipoDeCompra");
        cantidad = objetoJSON.getInt("cantidad");
        pagado = objetoJSON.getInt("pagado");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fecha);
        dest.writeInt(validado);
        dest.writeInt(idCliente);
        dest.writeInt(idDetalleProducto);
        dest.writeString(tipoDeCompra);
        dest.writeInt(cantidad);
        dest.writeInt(pagado);
    }
}
