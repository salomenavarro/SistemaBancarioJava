package com.example.application.modelo;

public class CuentaCorriente extends Cuenta {

    private double limiteSobregiro;

    public CuentaCorriente(String numeroCuenta, String titular, double saldoInicial, String tipo, double limiteSobregiro) {
        super(numeroCuenta, titular, saldoInicial, tipo);
        this.limiteSobregiro = limiteSobregiro;
    }

    @Override
    public boolean retirar(double monto) {
    // CORRECCIÓN: El cliente puede retirar hasta (saldo + limite)
    if (monto > 0 && monto <= (getSaldo() + limiteSobregiro)) {
        setSaldo(getSaldo() - monto);
        return true;
    }
    return false;
}

    @Override
    public String getTipoCuenta() {
        return "Corriente";
    }
}