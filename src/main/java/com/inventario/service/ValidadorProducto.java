package com.inventario.service;

import com.inventario.model.Producto;
import jakarta.enterprise.context.Dependent;

@Dependent
public class ValidadorProducto {

    public void validar(Producto p) throws Exception {
        if (p == null) throw new Exception("Producto nulo");
        var codigo = safe(p.getCodigo());
        var nombre = safe(p.getNombre());

        if (codigo.length() < 3) throw new Exception("El código debe tener al menos 3 caracteres");
        if (nombre.length() < 5) throw new Exception("El nombre debe tener al menos 5 caracteres");
        if (p.getPrecio() == null || p.getPrecio() <= 0) throw new Exception("El precio debe ser mayor a 0");
        if (p.getStock() == null || p.getStock() < 0) throw new Exception("El stock no puede ser negativo");

        p.setCodigo(codigo);
        p.setNombre(nombre);
        if (p.getActivo() == null) p.setActivo(Boolean.TRUE);
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }
}
