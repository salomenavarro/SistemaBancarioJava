package com.example.application.modelo;

// Clase base de todas las cuentas del banco.
// Es "abstract" porque nunca se crea sola — siempre es
// CuentaAhorros, CuentaCorriente o CuentaPremium.
public abstract class Cuenta implements Transaccionable {

    // Atributos privados — nadie los toca directamente desde afuera.
    // Para leerlos usa los getters de abajo.
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private String tipo;

    // Úsalo cuando la cuenta arranca con dinero.
    public Cuenta(String numeroCuenta, String titular, double saldoInicial, String tipo) {
        this.numeroCuenta = numeroCuenta;
        this.titular      = titular;
        this.saldo        = saldoInicial;
        this.tipo         = tipo;
        
    }

    // Úsalo cuando la cuenta arranca sin dinero (saldo = 0).
    public Cuenta(String numeroCuenta, String titular, String tipo) {
        this(numeroCuenta, titular, 0.0, tipo);
    }

    // Deposita dinero. Rechaza montos negativos o en cero.
    @Override
    public boolean depositar(double monto) {
        if (monto <= 0) return false;
        this.saldo += monto;
        return true;
    }

    // Igual que depositar(monto) pero guarda también una descripción del movimiento.
    // Mismo nombre, diferente parámetro — esto se llama sobrecarga de métodos.
    public boolean depositar(double monto, String descripcion) {
        return depositar(monto);
    }

    @Override
    public double getSaldo() {
        return saldo;
    }

    // Cada tipo de cuenta retira diferente:
    // Ahorros → no permite pasarse del saldo.
    // Corriente → permite sobregiro.
    // Premium → sin restricciones.
    @Override
    public abstract boolean retirar(double monto);

    // Cada subclase devuelve su nombre: "Ahorros", "Corriente" o "Premium".
    public abstract String getTipoCuenta();

    // Transfiere dinero de esta cuenta a otra.
    // Primero retira de aquí, luego deposita allá.
    public boolean transferir(double monto, Cuenta cuentaDestino) {
        if (this.retirar(monto)) {
            cuentaDestino.depositar(monto);
            return true;
        }
        return false;
    }

    // Resumen corto: "Ahorros **** 4821 — María García"
    public String getResumen() {
        return tipo + " " + numeroCuenta + " — " + titular;
    }

    // Resumen con saldo incluido si pasas true.
    // Mismo nombre, diferente parámetro — sobrecarga de métodos.
    public String getResumen(boolean mostrarSaldo) {
        if (mostrarSaldo) {
            return getResumen() + " | Saldo: $" + String.format("%,.0f", saldo);
        }
        return getResumen();
    }

    // Getters — para leer los atributos desde afuera.
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular()      { return titular; }
    public String getTipo()         { return tipo; }

    // Solo las subclases pueden cambiar el saldo directamente.
    protected void setSaldo(double saldo) { this.saldo = saldo; }
}