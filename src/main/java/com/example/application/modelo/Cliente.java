package com.example.application.modelo;

public class Cliente {
    private String nombreCompleto;
    private String cedula;
    private String correoElectronico;
    private String celular; 
    private String direccion;
    private String password;

    public Cliente(String nombreCompleto, String cedula, String correoElectronico, String celular, String direccion) {
        this.nombreCompleto = nombreCompleto;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
        this.celular = celular;
        this.direccion = direccion;
        this.password = "1234";
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCedula() { return cedula; }
    public String getCorreoElectronico() { return correoElectronico; }
    public String getNumeroCelular() { return celular; }
    public String getDireccion() { return direccion; }
    public String getPassword() { return password; }

    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public void setNumeroCelular(String celular) { this.celular = celular; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setPassword(String password) { this.password = password; }

    public String getResumen(boolean detallado) {
        if (detallado) {
            return nombreCompleto + " (" + correoElectronico + ")";
        }
        return nombreCompleto;
    }
}
