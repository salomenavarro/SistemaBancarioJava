package com.example.application.modelo;

public class Cliente {
    private String nombreCompleto;
    private String cedula;
    private String correoElectronico;
    private String celular;
    private String direccion;

    public Cliente(String nombreCompleto, String cedula, String correoElectronico, String celular, String direccion) {
        this.nombreCompleto = nombreCompleto;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.celular = celular;
        this.direccion = direccion;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCedula() { return cedula; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getCelular() { return celular; }
    public String getDireccion() { return direccion; }
}