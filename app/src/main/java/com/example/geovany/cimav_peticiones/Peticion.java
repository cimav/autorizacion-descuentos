package com.example.geovany.cimav_peticiones;

/**
 * Created by geova on 15/04/2016.
 */
public class Peticion {
    private int id, descuentoSolicitado, prioridad;
    private String codigo, nombreCliente;
    private double subTotal;

    public Peticion(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getDescuentoSolicitado() {
        return descuentoSolicitado;
    }

    public void setDescuentoSolicitado(int descuentoSolicitado) {
        this.descuentoSolicitado = descuentoSolicitado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public String toString() {
        return codigo;
    }
}



