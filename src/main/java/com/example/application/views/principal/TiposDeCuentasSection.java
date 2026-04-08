package com.example.application.views.principal;

import com.example.application.modelo.*;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public class TiposDeCuentasSection extends VerticalLayout {

    // Instancia única compartida
    private Banco banco = Banco.getInstancia();

    // Grid muestra objetos reales de Cuenta
    private Grid<Cuenta> grid = new Grid<>(Cuenta.class, false);

    public TiposDeCuentasSection() {

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        add(
            new H2("Tipos de Cuentas"),
            crearTarjetas(),
            crearFormulario(),
            crearTabla()
        );
    }

    private HorizontalLayout crearTarjetas() {

        // Solo visual
        Div ahorro = crearTarjeta("Cuenta Ahorros", "Guarda dinero", "3%");
        Div corriente = crearTarjeta("Cuenta Corriente", "Uso diario", "Frecuente");
        Div premium = crearTarjeta("Cuenta Premium", "Sin límites", "VIP");

        return new HorizontalLayout(ahorro, corriente, premium);
    }

    private Div crearTarjeta(String titulo, String desc, String dato) {

        Div card = new Div(new H3(titulo), new Paragraph(desc), new Span(dato));

        card.getStyle()
                .set("padding", "20px")
                .set("border-radius", "15px")
                .set("background", "#146551")
                .set("color", "white")
                .set("width", "250px");

        return card;
    }

    private VerticalLayout crearFormulario() {

        TextField nombre = new TextField("Nombre");
        TextField cedula = new TextField("Cédula");

        ComboBox<String> tipoCuenta = new ComboBox<>("Tipo");
        tipoCuenta.setItems("Ahorros", "Corriente", "Premium");

        NumberField saldo = new NumberField("Saldo inicial");

        Button crear = new Button("Crear cuenta");

        crear.addClickListener(e -> {

            // Validaciones mínimas
            if (tipoCuenta.getValue() == null) {
                Notification.show("Selecciona tipo de cuenta");
                return;
            }

            if (saldo.isEmpty() || saldo.getValue() == null) {
                Notification.show("Ingresa saldo");
                return;
            }

            String correo = cedula.getValue() + "@test.com";
            String pass = "1234";

            // Busca cliente o lo crea si no existe
            Cliente cliente = banco.buscarClientePorCedula(cedula.getValue());

            if (cliente == null) {
                cliente = new Cliente(nombre.getValue(), cedula.getValue(), correo, pass);
                banco.registrarCliente(cliente);
            }

            // Se asegura de tener sesión activa
            banco.iniciarSesion(correo, pass);

            String numeroCuenta = String.valueOf(System.currentTimeMillis());

            Cuenta nuevaCuenta = null;

            // Instancia según tipo seleccionado
            if ("Ahorros".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaAhorros(numeroCuenta, nombre.getValue(), saldo.getValue());
            } 
            else if ("Corriente".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaCorriente(numeroCuenta, nombre.getValue(), saldo.getValue(), 500000);
            } 
            else if ("Premium".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaPremium(numeroCuenta, nombre.getValue(), saldo.getValue());
            }

            // Se delega al Banco
            boolean creada = banco.abrirCuenta(nuevaCuenta);

            if (creada) {
                // Se muestran objetos reales en el grid
                grid.setItems(banco.getClienteActivo().getCuentas());
                Notification.show("Cuenta creada correctamente");
            } else {
                Notification.show("Ya tienes una cuenta de ese tipo o error");
            }

            nombre.clear();
            cedula.clear();
            saldo.clear();
            tipoCuenta.clear();
        });

        FormLayout form = new FormLayout(nombre, cedula, tipoCuenta, saldo, crear);
        return new VerticalLayout(form);
    }

    private Div crearTabla() {

        // Muestra datos reales de Cuenta
        grid.addColumn(Cuenta::getTipoCuenta).setHeader("Tipo");
        grid.addColumn(Cuenta::getSaldo).setHeader("Saldo");

        return new Div(grid);
    }
}