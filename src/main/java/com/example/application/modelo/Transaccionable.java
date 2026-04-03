package com.example.application.modelo;

// Esta interfaz es el "contrato" del banco.
// Toda cuenta bancaria DEBE poder depositar, retirar y consultar saldo.
// Si una clase dice "implements Transaccionable", está obligada a tener estos 3 métodos.

public interface Transaccionable {

    // Deposita dinero. Devuelve true si funcionó, false si el monto es inválido.
    boolean depositar(double monto);

    // Retira dinero. Devuelve true si había saldo, false si no alcanzaba.
    boolean retirar(double monto);

    // Devuelve cuánto dinero hay en la cuenta en este momento.
    double getSaldo();
}