package com.inventario.facade;

import com.inventario.events.ProductoEvent;
import com.inventario.model.Producto;
import com.inventario.persistence.ProductoDAO;
import com.inventario.service.ValidadorProducto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@Named("productoFacade")            
@ApplicationScoped                  
public class ProductoFacade {

    // DataSource producido por com.inventario.config.DataSourceProducer
    @Inject
    private DataSource ds;

    // Validador (Dependent)
    @Inject
    private ValidadorProducto validador;

    // Canal de eventos CDI
    @Inject
    private Event<ProductoEvent> productoEvent;

  
    public List<Producto> listar() throws Exception {
        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);
            return dao.listar();
        }
    }

    public void crear(Producto p) throws Exception {
        
        validador.validar(p);

        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);

            // Verificar unicidad por código
            Optional<Producto> existente = dao.buscarPorCodigo(p.getCodigo());
            if (existente.isPresent()) {
                throw new Exception("Ya existe un producto con el código: " + p.getCodigo());
            }

            dao.insertar(p);

           
            productoEvent.fire(new ProductoEvent(p, "CREADO"));
        }
    }

    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido");
        }
        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);
            dao.eliminarPorId(id);

            
            Producto phantom = new Producto(id, null, null);
            productoEvent.fire(new ProductoEvent(phantom, "ELIMINADO"));
        }
    }

    public Optional<Producto> buscarPorId(int id) throws Exception {
        try (Connection con = ds.getConnection()) {
            return new ProductoDAO(con).buscarPorId(id);
        }
    }

    public void actualizar(Producto p) throws Exception {
        validador.validar(p);
        if (p.getId() == null || p.getId() <= 0) {
            throw new Exception("ID inválido");
        }

        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);
            Optional<Producto> dupe = dao.buscarPorCodigo(p.getCodigo());
            if (dupe.isPresent() && !dupe.get().getId().equals(p.getId())) {
                throw new Exception("Ya existe un producto con el código: " + p.getCodigo());
            }
            dao.actualizar(p);
        }
    }
}
