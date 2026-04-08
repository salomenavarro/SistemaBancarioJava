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
        // CORRECCIÓN: Validación simple para que el monto no sea negativo
        this.monto = (monto > 0) ? monto : 0;
        this.plazoMeses = (plazoMeses > 0) ? plazoMeses : 12;
        this.cliente = cliente;
        this.estado = "Pendiente";
        asignarTasa();
    }

    // MÉTODO PARA ASIGNAR LA TASA SEGÚN EL TIPO
    private void asignarTasa() {
        switch (tipo) {
            case "Personal": tasaInteres = 0.012; break;
            case "Hipotecario": tasaInteres = 0.009; break;
            case "Vehicular": tasaInteres = 0.015; break;
            default: tasaInteres = 0.01; // CORRECCIÓN: Tasa por defecto si el tipo falla
        }
    }

    // MÉTODO PARA CALCULAR CUOTA MENSUAL
    public double calcularCuota() {

        double i = tasaInteres;
        int n = plazoMeses;

        return monto * (i * Math.pow(1 + i, n)) /
                (Math.pow(1 + i, n) - 1);
    }

    // RESUMEN SIMPLE
    public String getResumen() {

        return "Cliente: " + cliente +
                " | Tipo: " + tipo +
                " | Monto: " + monto +
                " | Estado: " + estado;
    }

       // RESUMEN DETALLADO (sobrecarga)
    public String getResumen(boolean detallado) {

        if (!detallado) {
            return getResumen();
        }

        return "Cliente: " + cliente +
                "\nTipo: " + tipo +
                "\nMonto: " + monto +
                "\nPlazo: " + plazoMeses +
                "\nTasa: " + tasaInteres +
                "\nCuota mensual: " + calcularCuota() +
                "\nEstado: " + estado;
    }

    // GETTERS

    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public String getEstado() {
        return estado;
    }

    public String getCliente() {
        return cliente;
    }

    // SETTER CON VALIDACIÓN DE ESTADO
    public void setEstado(String estado) {

        if (estado.equals("Pendiente") || estado.equals("Aprobado") || estado.equals("Rechazado")) {
            this.estado = estado;
        }
    }

}
