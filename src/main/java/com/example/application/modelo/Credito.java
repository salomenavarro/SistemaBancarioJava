package com.example.application.modelo;

public class Credito {

    // ATRIBUTOS 
    private String tipo;
    private double monto;
    private int plazoMeses;
    private double tasaInteres;
    private String estado;
    private String cliente;

    // CONSTRUCTOR 
    public Credito(String tipo, double monto, int plazoMeses, String cliente, String estado) {

        this.tipo = tipo;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.cliente = cliente;
        this.estado = estado;
    }

    // CONSTRUCTOR BÁSICO (sobrecarga)
    public Credito(String tipo, double monto, int plazoMeses, String cliente) {

        this.tipo = tipo;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.cliente = cliente;
        this.estado = "Pendiente";
    }
}
