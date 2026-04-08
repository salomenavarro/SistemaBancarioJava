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

    private Banco banco = Banco.getInstancia();
    private Grid<Cuenta> grid = new Grid<>(Cuenta.class, false);
    private HeroSection heroSection;
    private PagosSection pagosSection; // referencia para refrescar cuentas en pagos

    public TiposDeCuentasSection(HeroSection heroSection, PagosSection pagosSection) {

        this.heroSection = heroSection;
        this.pagosSection = pagosSection;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        add(
                new H2("Tipos de Cuentas"),
                crearTarjetas(),
                crearFormulario(),
                crearTabla());

        if (banco.getClienteActivo() != null) {
            grid.setItems(banco.getClienteActivo().getCuentas());
        }
    }

    public void actualizarGrid() {
        if (banco.getClienteActivo() != null) {
            grid.setItems(banco.getClienteActivo().getCuentas());
        }
    }

    private HorizontalLayout crearTarjetas() {
        Div ahorro = crearTarjeta("Cuenta Ahorros", "Guarda dinero", "3%");
        Div corriente = crearTarjeta("Cuenta Corriente", "Uso diario", "Frecuente");
        Div premium = crearTarjeta("Cuenta Premium", "Sin límites", "VIP");

        HorizontalLayout layout = new HorizontalLayout(ahorro, corriente, premium);
        layout.setSpacing(true);
        return layout;
    }

    private Div crearTarjeta(String titulo, String desc, String dato) {
        Div card = new Div(new H3(titulo), new Paragraph(desc), new Span(dato));
        card.getStyle()
                .set("padding", "20px")
                .set("border-radius", "15px")
                .set("background", "var(--dess-verde-oscuro)")
                .set("color", "white")
                .set("width", "250px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");
        return card;
    }

    private VerticalLayout crearFormulario() {
        TextField nombre = new TextField("Nombre");
        TextField cedula = new TextField("Cédula");
        ComboBox<String> tipoCuenta = new ComboBox<>("Tipo");
        tipoCuenta.setItems("Ahorros", "Corriente", "Premium");
        NumberField saldo = new NumberField("Saldo inicial");
        Button crear = new Button("Crear cuenta");
        crear.addClassName("btn-main");

        crear.addClickListener(e -> {
            if (tipoCuenta.getValue() == null || saldo.getValue() == null || nombre.isEmpty() || cedula.isEmpty()) {
                Notification.show("Completa todos los campos");
                return;
            }

            Cliente cliente = banco.getClienteActivo();
            if (cliente == null) {
                Notification.show("Debes iniciar sesión");
                return;
            }

            String numeroCuenta = String.valueOf(System.currentTimeMillis());
            Cuenta nuevaCuenta = null;

            if ("Ahorros".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaAhorros(numeroCuenta, nombre.getValue(), saldo.getValue(), "Ahorros");
            } else if ("Corriente".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaCorriente(numeroCuenta, nombre.getValue(), saldo.getValue(), "Corriente", 500000);
            } else if ("Premium".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaPremium(numeroCuenta, nombre.getValue(), saldo.getValue(), "Premium");
            }

            if (banco.abrirCuenta(nuevaCuenta)) {
                grid.setItems(banco.getClienteActivo().getCuentas());

                if (heroSection != null) {
                    heroSection.actualizarInterfaz();
                }

                // Avisa a Pagos para que aparezca la cuenta recién creada en el selector
                if (pagosSection != null) {
                    pagosSection.refrescarCuentas();
                }

                Notification.show("Cuenta creada correctamente");
            } else {
                Notification.show("Ya tienes una cuenta de ese tipo");
            }

            nombre.clear();
            cedula.clear();
            saldo.clear();
            tipoCuenta.clear();
        });

        FormLayout form = new FormLayout(nombre, cedula, tipoCuenta, saldo, crear);
        VerticalLayout contenedor = new VerticalLayout(form);
        contenedor.addClassName("box");
        return contenedor;
    }

    private Div crearTabla() {
        grid.addColumn(Cuenta::getTipoCuenta).setHeader("Tipo");
        grid.addColumn(Cuenta::getSaldo).setHeader("Saldo");

        Div contenedorGrid = new Div(grid);
        contenedorGrid.addClassName("grid-box");
        contenedorGrid.setWidthFull();
        return contenedorGrid;
    }
}