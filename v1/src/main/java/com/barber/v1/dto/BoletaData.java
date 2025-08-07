package com.barber.v1.dto;

import java.time.LocalDateTime;

public class BoletaData {
    // Datos empresa
    private String ruc = "20123456789";
    private String razonSocial = "APPbarber SAC";
    private String direccion = "Av. Principal 123 - Lima";
    private String telefono = "987654321";
    private String email = "contacto@appbarber.com";
    private String web = "www.appbarber.com";
    private String clienteNombre;
    private String numeroBoleta;
    private LocalDateTime fechaEmision;

    private String servicio;
    private String descripcion;
    private double precio;

    // Getters y setters
    public String getRuc() { return ruc; }
    public String getRazonSocial() { return razonSocial; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getWeb() { return web; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }


    public String getNumeroBoleta() { return numeroBoleta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
