package com.example.application.modelo;

public class CuentaCorriente extends Cuenta {

    private double limiteSobregiro;

    public CuentaCorriente(String numeroCuenta, String titular, double saldoInicial,
            double limiteSobregiro) {
        // ya no se inicializa el tipo en el constuctor, solo se pone directamente.
        super(numeroCuenta, titular, saldoInicial, "Corriente");
        this.limiteSobregiro = limiteSobregiro;
    }

    @Override
    public boolean retirar(double monto) {
        // pedimmos que la cuenta no este bloqueda para retirar.
        if (estaBloqueada())
            return false; // si esta bloqueada false.

        if (monto <= getSaldo() + limiteSobregiro) {
            setSaldo(getSaldo() - monto);
            return true; // si no esta bloqueada true y se hace la operacion.
        }

        // return por defecto, por si hay ejecucion invalida.
        return false;
    }

    @Override
    public String getTipoCuenta() {
        return "Corriente";
    }
}