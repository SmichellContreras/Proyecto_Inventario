package com.inventario.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("mensajeBean")
@RequestScoped
public class MensajeBean {
    private String info;
    private String error;

    public String getInfo()  { return info;  }
    public String getError() { return error; }
    public void setInfo(String m)  { this.info = m; }
    public void setError(String m) { this.error = m; }
    public void clear() { info = null; error = null; }
}
