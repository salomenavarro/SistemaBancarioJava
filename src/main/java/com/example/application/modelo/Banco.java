package com.example.application.modelo;

import java.util.ArrayList;
import java.util.List;

// Coordina todo el sistema bancario DESS.
// Es el único que conoce a todos los clientes y sus cuentas.
// Todas las secciones de la app hablan con Banco para operar.

public class Banco {

    // ── ATRIBUTOS ─────────────────────────────────────────────────────────────
    private String nombre;
    private List<Cliente> clientes;
    private Cliente clienteActivo; // cliente con sesión abierta en este momento
    private static Banco instancia; // lo creo para tener un banco global y que toda la app sirva con el mismo banco 

    // ── CONSTRUCTOR ───────────────────────────────────────────────────────────
    public Banco(String nombre) {
        this.nombre        = nombre;
        this.clientes      = new ArrayList<>();
        this.clienteActivo = null;
    }

    // ── REGISTRO ──────────────────────────────────────────────────────────────

    // Registra un cliente nuevo.
    // Rechaza si ya existe alguien con la misma cédula o el mismo email.
    public boolean registrarCliente(Cliente cliente) {
        if (buscarClientePorCedula(cliente.getCedula()) != null) return false;
        if (buscarClientePorEmail(cliente.getCorreoElectronico()) != null) return false;
        clientes.add(cliente);
        return true;
    }

    //
    public static Banco getInstancia() {
    if (instancia == null) {
        instancia = new Banco("DESS");
    }
    return instancia;
}

    // ── LOGIN / LOGOUT ────────────────────────────────────────────────────────

    // Busca el cliente por email y contraseña.
    // Si coinciden, lo guarda como clienteActivo y devuelve true.
    public boolean iniciarSesion(String email, String contrasena) {
        for (Cliente c : clientes) {
            if (c.getCorreoElectronico().equals(email) && c.getContrasena().equals(contrasena)) {
                this.clienteActivo = c;
                return true;
            }
        }
        return false;
    }

    // Cierra la sesión del cliente activo.
    public void cerrarSesion() {
        this.clienteActivo = null;
    }

    // ── CUENTAS BANCARIAS ─────────────────────────────────────────────────────

    // Abre una cuenta bancaria nueva para el cliente activo.
    // Rechaza si:
    // - No hay sesión activa
    // - El cliente ya tiene una cuenta del mismo tipo
    public boolean abrirCuenta(Cuenta cuenta) {
        if (clienteActivo == null) return false;
        if (!puedeAbrirCuenta(cuenta.getTipoCuenta())) return false;
        clienteActivo.agregarCuenta(cuenta);
        return true;
    }

    // Verifica que el cliente activo no tenga ya una cuenta de ese tipo.
    // Un cliente solo puede tener UNA cuenta de cada tipo.
    public boolean puedeAbrirCuenta(String tipoCuenta) {
        if (clienteActivo == null) return false;
        for (Cuenta c : clienteActivo.getCuentas()) {
            if (c.getTipoCuenta().equals(tipoCuenta)) return false;
        }
        return true;
    }


    // ── OPERACIONES ───────────────────────────────────────────────────────────

    // Realiza una transferencia entre dos cuentas.
    // Rechaza si:
    // - No hay sesión activa
    // - El cliente activo no tiene cuentas
    // - El monto es inválido
    public boolean realizarTransferencia(Cuenta cuentaOrigen,
                                         Cuenta cuentaDestino,
                                         double monto) {
        if (clienteActivo == null) return false;
        if (clienteActivo.getCuentas().isEmpty()) return false;
        if (monto <= 0) return false;
        return cuentaOrigen.transferir(monto, cuentaDestino);
    }

    // Realiza un pago desde una cuenta del cliente activo.
    // Rechaza si:
    // - No hay sesión activa
    // - El cliente activo no tiene cuentas
    // - El monto es inválido
    public boolean realizarPago(Cuenta cuentaOrigen, double monto) {
        if (clienteActivo == null) return false;
        if (clienteActivo.getCuentas().isEmpty()) return false;
        if (monto <= 0) return false;
        return cuentaOrigen.retirar(monto);
    }

    // ── BÚSQUEDAS ─────────────────────────────────────────────────────────────

    // Busca un cliente por cédula. Devuelve null si no existe.
    public Cliente buscarClientePorCedula(String cedula) {
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) return c;
        }
        return null;
    }

    // Busca un cliente por email. Devuelve null si no existe.
    public Cliente buscarClientePorEmail(String email) {
        for (Cliente c : clientes) {
            if (c.getCorreoElectronico().equals(email)) return c;
        }
        return null;
    }

    // ── GETTERS ───────────────────────────────────────────────────────────────
    public String getNombre()          { return nombre; }
    public Cliente getClienteActivo()  { return clienteActivo; }
    public List<Cliente> getClientes() { return clientes; }

    // Devuelve true si hay un cliente con sesión abierta.
    public boolean haySesionActiva() {
        return clienteActivo != null;
    }


}