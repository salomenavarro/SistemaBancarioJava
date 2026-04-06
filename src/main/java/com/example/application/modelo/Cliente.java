package com.example.application.modelo;

import java.util.ArrayList;
import java.util.List;

// Representa a un usuario registrado en DESS.
// Tiene sus datos personales, contraseña para el login
// y la lista de cuentas bancarias que ha abierto.

public class Cliente {

    // ── ATRIBUTOS

    private String nombreCompleto;
    private String cedula;              
    private String correoElectronico;
    private String contrasena;         
    private String celular;
    private String direccion;
    private List<Cuenta> cuentas;       // cuentas bancarias del cliente en DESS

    // ── CONSTRUCTOR 
    public Cliente(String nombreCompleto, String cedula, String correoElectronico,
                   String contrasena, String celular, String direccion) {
        this.nombreCompleto      = nombreCompleto;
        this.cedula              = cedula;
        this.correoElectronico   = correoElectronico;
        this.contrasena          = contrasena;   // viene de afuera, no "1234"
        this.celular             = celular;
        this.direccion           = direccion;
        this.cuentas             = new ArrayList<>(); // empieza sin cuentas
    }

    // ── CONSTRUCTOR BÁSICO
    public Cliente(String nombreCompleto, String cedula,
                   String correoElectronico, String contrasena) {
        this(nombreCompleto, cedula, correoElectronico, contrasena, "", "");
    }

    // ── CUENTAS BANCARIAS 
    // Banco.abrirCuenta() llama a este método — no lo llames directo desde las secciones.

    // Agrega una cuenta bancaria al cliente.
    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    // Devuelve todas las cuentas del cliente.
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    // Devuelve true si el cliente tiene al menos una cuenta.
    public boolean tieneCuentas() {
        return !cuentas.isEmpty();
    }

    // Devuelve la primera cuenta (la que se muestra en el Hero).
    // Devuelve null si aún no tiene ninguna.
    public Cuenta getCuentaPrincipal() {
        if (cuentas.isEmpty()) return null;
        return cuentas.get(0);
    }

    // ── MÉTODOS DE NEGOCIO ────────────────────────────────────────────────────

    // Resumen corto — solo nombre y cédula.
    public String getResumen() {
        return nombreCompleto + " — CC: " + cedula;
    }

    // Resumen con más detalle si pasas true.
    // Este método ya lo tenías, solo le agregué que llame al getResumen() de arriba.
    public String getResumen(boolean detallado) {
        if (detallado) {
            return getResumen() + " | " + correoElectronico + " | " + celular;
        }
        return getResumen();
    }

    // Devuelve true si el cliente tiene todos sus datos de contacto llenos.
    public boolean tieneDatosCompletos() {
        return !nombreCompleto.isEmpty()
            && !correoElectronico.isEmpty()
            && !celular.isEmpty();
    }

    // ── GETTERS ───────────────────────────────────────────────────────────────
    // Mantuve los mismos nombres que tenías.
    public String getNombreCompleto()      { return nombreCompleto; }
    public String getCedula()              { return cedula; }
    public String getCorreoElectronico()   { return correoElectronico; }
    public String getContrasena()          { return contrasena; }
    public String getNumeroCelular()       { return celular; }
    public String getDireccion()           { return direccion; }

    // ── SETTERS ───────────────────────────────────────────────────────────────
    // Solo los campos que tiene sentido actualizar desde el perfil.
    // La cédula NO tiene setter porque no debe cambiar nunca.
    public void setNombreCompleto(String nombreCompleto)           { this.nombreCompleto = nombreCompleto; }
    public void setCorreoElectronico(String correoElectronico)     { this.correoElectronico = correoElectronico; }
    public void setContrasena(String contrasena)                   { this.contrasena = contrasena; }
    public void setNumeroCelular(String celular)                   { this.celular = celular; }
    public void setDireccion(String direccion)                     { this.direccion = direccion; }
}