package com.example.application.modelo;

// Clase base de todas las cuentas del banco.
// Es "abstract" porque nunca se crea sola — siempre es
// CuentaAhorros, CuentaCorriente o CuentaPremium.
// Emiliano: tus tres clases deben decir "extends Cuenta".

public abstract class Cuenta implements Transaccionable {

    private String numeroCuenta;
    private String titular;
    private double saldo;
    private String tipo;
    private boolean bloqueada; // true = tarjeta bloqueada, no puede operar

    // Úsalo cuando la cuenta arranca con dinero.
    public Cuenta(String numeroCuenta, String titular, double saldoInicial, String tipo) {
        this.numeroCuenta = numeroCuenta;
        this.titular      = titular;
        this.saldo        = saldoInicial;
        this.tipo         = tipo;
        this.bloqueada    = false; // empieza desbloqueada
    }

    // Úsalo cuando la cuenta arranca sin dinero (saldo = 0).
    public Cuenta(String numeroCuenta, String titular, String tipo) {
        this(numeroCuenta, titular, 0.0, tipo);
    }

    // Deposita dinero. Rechaza si la cuenta está bloqueada o el monto es inválido.
    @Override
    public boolean depositar(double monto) {
        if (bloqueada) return false;
        if (monto <= 0) return false;
        this.saldo += monto;
        return true;
    }

    // Igual que depositar(monto) pero con descripción — sobrecarga de métodos.
    public boolean depositar(double monto, String descripcion) {
        return depositar(monto);
    }

    @Override
    public double getSaldo() {
        return saldo;
    }

    // Cada tipo de cuenta retira diferente — las subclases lo implementan.
    // IMPORTANTE: las subclases también deben verificar si está bloqueada.
    @Override
    public abstract boolean retirar(double monto);

    // Cada subclase devuelve su nombre: "Ahorros", "Corriente" o "Premium".
    public abstract String getTipoCuenta();

    // Transfiere dinero de esta cuenta a otra.
    public boolean transferir(double monto, Cuenta cuentaDestino) {
        if (this.retirar(monto)) {
            cuentaDestino.depositar(monto);
            return true;
        }
        return false;
    }

    // ── BLOQUEO DE TARJETA ────────────────────────────────────────────────────

    // Bloquea la cuenta — no puede depositar, retirar ni transferir.
    public void bloquear() {
        this.bloqueada = true;
    }

    // Reactiva la cuenta.
    public void desbloquear() {
        this.bloqueada = false;
    }

    // Devuelve true si la cuenta está bloqueada.
    public boolean estaBloqueada() {
        return bloqueada;
    }

    // ── RESUMEN ───────────────────────────────────────────────────────────────

    public String getResumen() {
        return tipo + " " + numeroCuenta + " — " + titular;
    }

    public String getResumen(boolean mostrarSaldo) {
        if (mostrarSaldo) {
            return getResumen() + " | Saldo: $" + String.format("%,.0f", saldo);
        }
        return getResumen();
    }

    // ── GETTERS / SETTER PROTEGIDO ────────────────────────────────────────────
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular()      { return titular; }
    public String getTipo()         { return tipo; }

    protected void setSaldo(double saldo) { this.saldo = saldo; }
}