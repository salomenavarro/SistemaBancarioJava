package com.example.application.modelo;

public class CuentaPremium extends Cuenta {

    public CuentaPremium(String numeroCuenta, String titular, double saldoInicial) {
        //ya no se inicializa el tipo en el constuctor, solo se pone directamente.
        super(numeroCuenta, titular, saldoInicial,"Premium");
    }

    @Override
    public boolean retirar(double monto) {
        // pedimmos que la cuenta no este bloqueda para retirar.
        if (estaBloqueada())
            return false; // si esta bloqueada false.

        setSaldo(getSaldo() - monto);
        return true; // Como esta no tiene restricciones de sobregiro, se ejecuta directo el true.
    }

    @Override
    public String getTipoCuenta() {
        return "Premium";
    }
}