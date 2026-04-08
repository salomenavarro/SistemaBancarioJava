package com.example.application.modelo;

public class CuentaAhorros extends Cuenta {

    public CuentaAhorros(String numeroCuenta, String titular, double saldoInicial) {
        //ya no se inicializa el tipo en el constuctor, solo se pone directamente.
        super(numeroCuenta, titular, saldoInicial, "Ahorros");
    }

    @Override
    public boolean retirar(double monto) {
        // pedimmos que la cuenta no este bloqueda para retirar.
        if (estaBloqueada())
            return false; // si esta bloqueada false.

        if (monto <= getSaldo()) {
            setSaldo(getSaldo() - monto);
            return true;
        }
        return false;
    }

    @Override
    public String getTipoCuenta() {
        return "Ahorros";
    }
}