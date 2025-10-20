package com.inventario.persistence;

import com.inventario.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProductoDAO {

    private final Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

   
    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
      
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

   
    public List<Producto> listar() throws SQLException {
        final String sql = "SELECT id, codigo, nombre, categoria, precio, stock, activo "
                + "FROM productos ORDER BY nombre ASC";
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    
    public Optional<Producto> buscarPorCodigo(String codigo) throws SQLException {
        final String sql = "SELECT id, codigo, nombre, categoria, precio, stock, activo "
                + "FROM productos WHERE codigo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    
    public void insertar(Producto p) throws SQLException {
        final String sql = "INSERT INTO productos "
                + "(codigo, nombre, categoria, precio, stock, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setBoolean(6, p.getActivo() != null ? p.getActivo() : Boolean.TRUE);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No se pudo insertar el producto (0 filas afectadas).");
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        }
    }

    
    public void eliminarPorId(int id) throws SQLException {
        final String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    
    public Optional<Producto> buscarPorId(int id) throws SQLException {
        final String sql = "SELECT id,codigo,nombre,categoria,precio,stock,activo FROM productos WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public void actualizar(Producto p) throws SQLException {
        final String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, precio=?, stock=?, activo=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setBoolean(6, p.getActivo() != null ? p.getActivo() : Boolean.TRUE);
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

}
