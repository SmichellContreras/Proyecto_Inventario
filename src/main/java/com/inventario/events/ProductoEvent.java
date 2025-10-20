package com.inventario.events;

import com.inventario.model.Producto;

/** Evento de dominio para acciones sobre Producto */
public class ProductoEvent {
    private final Producto producto;
    private final String tipo; 

    public ProductoEvent(Producto producto, String tipo) {
        this.producto = producto;
        this.tipo = tipo;
    }

    public Producto getProducto() { return producto; }
    public String getTipo() { return tipo; }
}
