package com.inventario.controller;

import com.inventario.facade.ProductoFacade;
import com.inventario.model.Producto;
import com.inventario.view.MensajeBean;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    @Inject
    private ProductoFacade facade;

    @Inject
    private MensajeBean msg;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = param(req, "action");

        try {
            if ("del".equalsIgnoreCase(action)) {        // eliminar
                int id = Integer.parseInt(param(req, "id"));
                facade.eliminar(id);
                req.getSession().setAttribute("flashInfo", "Producto eliminado (ID " + id + ")");
                resp.sendRedirect(req.getContextPath() + "/productos");
                return;
            }

            if ("edit".equalsIgnoreCase(action)) {       // mostrar formulario de edición
                int id = Integer.parseInt(param(req, "id"));
                facade.buscarPorId(id).ifPresent(p -> req.setAttribute("edit", p));
            }

            
            pullFlash(req);

            // Listado siempre
            List<Producto> productos = facade.listar();
            req.setAttribute("productos", productos);
        } catch (Exception e) {
            msg.setError("Error: " + e.getMessage());
        }

        req.setAttribute("mensajeBean", msg);
        req.getRequestDispatcher("/productos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = param(req, "action");

        if ("update".equalsIgnoreCase(action)) {         // actualizar
            Producto p = new Producto();
            p.setId(parseInt(param(req, "id")));
            p.setCodigo(trim(param(req, "codigo")));
            p.setNombre(trim(param(req, "nombre")));
            p.setCategoria(trim(param(req, "categoria")));
            p.setPrecio(parseDouble(trim(param(req, "precio"))));
            p.setStock(parseInt(trim(param(req, "stock"))));
            p.setActivo(req.getParameter("activo") != null);
            try {
                facade.actualizar(p);
                req.getSession().setAttribute("flashInfo", "Producto actualizado: " + p.getCodigo());
                resp.sendRedirect(req.getContextPath() + "/productos");
                return;
            } catch (Exception e) {
                msg.setError(e.getMessage());
                req.setAttribute("form", p);
                try { req.setAttribute("productos", facade.listar()); } catch (Exception ignore) {}
                req.setAttribute("mensajeBean", msg);
                req.getRequestDispatcher("/productos.jsp").forward(req, resp);
                return;
            }
        }

        // crear (por defecto)
        Producto p = new Producto();
        p.setCodigo(trim(param(req, "codigo")));
        p.setNombre(trim(param(req, "nombre")));
        p.setCategoria(trim(param(req, "categoria")));
        p.setPrecio(parseDouble(trim(param(req, "precio"))));
        p.setStock(parseInt(trim(param(req, "stock"))));
        p.setActivo(req.getParameter("activo") != null);

        try {
            facade.crear(p);
            req.getSession().setAttribute("flashInfo", "Producto creado: " + p.getCodigo());
            resp.sendRedirect(req.getContextPath() + "/productos");
        } catch (Exception e) {
            msg.setError(e.getMessage());
            req.setAttribute("form", p);
            try { req.setAttribute("productos", facade.listar()); } catch (Exception ignore) {}
            req.setAttribute("mensajeBean", msg);
            req.getRequestDispatcher("/productos.jsp").forward(req, resp);
        }
    }

    
    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static String param(HttpServletRequest r, String k) { return r.getParameter(k); }
    private static Integer parseInt(String s) {
        try { return (s == null || s.isEmpty()) ? null : Integer.valueOf(s); }
        catch (NumberFormatException e) { return null; }
    }
    private static Double parseDouble(String s) {
        try { return (s == null || s.isEmpty()) ? null : Double.valueOf(s); }
        catch (NumberFormatException e) { return null; }
    }
    private void pullFlash(HttpServletRequest req) {
        Object info = req.getSession().getAttribute("flashInfo");
        Object err  = req.getSession().getAttribute("flashError");
        if (info != null) { msg.setInfo(String.valueOf(info)); req.getSession().removeAttribute("flashInfo"); }
        if (err  != null) { msg.setError(String.valueOf(err));  req.getSession().removeAttribute("flashError"); }
    }
}
