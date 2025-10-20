package com.inventario.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.logging.Logger;

@ApplicationScoped
public class ProductoObserver {

    private static final Logger log = Logger.getLogger(ProductoObserver.class.getName());

    
    public void onProductoEvent(@Observes ProductoEvent ev) {
        String detalle = (ev.getProducto() != null)
                ? (ev.getProducto().getCodigo() != null
                    ? ev.getProducto().getCodigo()
                    : "ID=" + ev.getProducto().getId())
                : "(sin producto)";

        log.info(() -> "Evento Producto: " + ev.getTipo() + " | " + detalle);
    }
}
