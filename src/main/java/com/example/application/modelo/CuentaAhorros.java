package com.example.application.modelo;

public class CuentaAhorros extends Cuenta {

    public CuentaAhorros(String numeroCuenta, String titular, double saldoInicial, String tipo) {
        
        super(numeroCuenta, titular, saldoInicial, tipo);
    }

    @Override
    public boolean retirar(double monto) {
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