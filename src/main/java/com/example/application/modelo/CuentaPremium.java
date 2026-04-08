package com.example.application.modelo;

public class CuentaPremium extends Cuenta {

    public CuentaPremium(String numeroCuenta, String titular, double saldoInicial, String tipo) {
        super(numeroCuenta, titular, saldoInicial, tipo);
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0)
            return false;

        setSaldo(getSaldo() - monto);
        return true;
    }

    @Override
    public String getTipoCuenta() {
        return "Premium";
    }
}