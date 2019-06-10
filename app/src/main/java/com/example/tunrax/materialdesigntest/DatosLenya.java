package com.example.tunrax.materialdesigntest;
/**
 * esta clase solo tiene los getters y setters de los datos de la leña
 */

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class DatosLenya implements Parcelable {
    int id;
    int precioUnitario;
    int ventaMinima;
    String producto;
    String medida;
    int idProveedor;

    public DatosLenya(JSONObject objetoJSON)throws JSONException {
        id = objetoJSON.getInt("id");
        precioUnitario = objetoJSON.getInt("precio_unitario");
        ventaMinima = objetoJSON.getInt("venta_minima");
        producto = objetoJSON.getString("producto");
        medida = objetoJSON.getString("medida");
        idProveedor = objetoJSON.getInt("id_proveedor");
    }

    protected DatosLenya(Parcel in) {
        id = in.readInt();
        precioUnitario = in.readInt();
        ventaMinima = in.readInt();
        producto = in.readString();
        medida = in.readString();
        idProveedor = in.readInt();
    }

    public static final Creator<DatosLenya> CREATOR = new Creator<DatosLenya>() {
        @Override
        public DatosLenya createFromParcel(Parcel in) {
            return new DatosLenya(in);
        }

        @Override
        public DatosLenya[] newArray(int size) {
            return new DatosLenya[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getVentaMinima() {
        return ventaMinima;
    }

    public void setVentaMinima(int ventaMinima) {
        this.ventaMinima = ventaMinima;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        medida = medida;
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
        dest.writeInt(precioUnitario);
        dest.writeInt(ventaMinima);
        dest.writeString(producto);
        dest.writeString(medida);
        dest.writeInt(idProveedor);
    }
}
