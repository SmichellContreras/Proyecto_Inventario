package com.inventario.model;

import java.io.Serializable;


public class Producto implements Serializable {
    private Integer id;
    private String codigo;
    private String nombre;
    private String categoria;
    private Double precio;
    private Integer stock;
    private Boolean activo;

    public Producto() {}

    public Producto(Integer id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    
    public Producto(Integer id, String codigo, String nombre, String categoria,
                    Double precio, Integer stock, Boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
    }

   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

  

    @Override
    public String toString() {
        return String.format("Producto[codigo=%s, nombre=%s, stock=%d]", 
                              codigo, nombre, stock);
    }

    /** Indica si el producto está disponible para venta */
    public boolean isDisponible() {
        return (stock != null && stock > 0) && Boolean.TRUE.equals(activo);
    }
}
