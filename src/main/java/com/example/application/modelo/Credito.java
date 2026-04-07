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

        asignarTasa();
    }

    // CONSTRUCTOR BÁSICO (sobrecarga)
    public Credito(String tipo, double monto, int plazoMeses, String cliente) {

        this.tipo = tipo;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.cliente = cliente;
        this.estado = "Pendiente";

        asignarTasa();
    }

    // MÉTODO PARA ASIGNAR LA TASA SEGÚN EL TIPO
    private void asignarTasa() {

        switch (tipo) {

            case "Personal":
                tasaInteres = 0.012;
                break;

            case "Hipotecario":
                tasaInteres = 0.009;
                break;

            case "Vehicular":
                tasaInteres = 0.015;
                break;
        }
    }

    // MÉTODO PARA CALCULAR CUOTA MENSUAL
    public double calcularCuota() {

        double i = tasaInteres;
        int n = plazoMeses;

        return monto * (i * Math.pow(1 + i, n)) /
                (Math.pow(1 + i, n) - 1);
    }

}
