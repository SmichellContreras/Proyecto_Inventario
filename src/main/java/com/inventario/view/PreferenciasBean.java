package com.inventario.view;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named("preferenciasBean")
@SessionScoped
public class PreferenciasBean implements Serializable {
    private String idioma = "es";
    private final Map<String, Object> filtros = new HashMap<>();

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Map<String, Object> getFiltros() { return filtros; }
    public void setFiltro(String key, Object val) { filtros.put(key, val); }
    public Object getFiltro(String key) { return filtros.get(key); }
}
