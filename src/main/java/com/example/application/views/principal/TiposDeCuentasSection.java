package com.example.application.views.principal;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.button.Button;

import java.util.ArrayList;
import java.util.List;

public class TiposDeCuentasSection extends VerticalLayout {

    private List<String> cuentas = new ArrayList<>();
    private Grid<String> grid = new Grid<>();

    public TiposDeCuentasSection() {

        setWidth("100%");
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        // 🎨 Fondo general
        getStyle().set("background", "#8FC1B5");

        // 🔹 TÍTULO
        H2 titulo = new H2("Tipos de Cuentas");

        // 🔹 TARJETAS
        Div ahorro = crearTarjeta(
                "Cuenta de Ahorros",
                "Ideal para guardar dinero y generar intereses."
        );

        Div corriente = crearTarjeta(
                "Cuenta Corriente",
                "Permite movimientos frecuentes sin restricciones."
        );

        HorizontalLayout contenedor = new HorizontalLayout(ahorro, corriente);
        contenedor.setSpacing(true);

        // 🔹 FORMULARIO
        H2 subtitulo = new H2("Crear Cuenta");

        TextField nombre = new TextField("Nombre del cliente");
        TextField cedula = new TextField("Cédula");

        ComboBox<String> tipoCuenta = new ComboBox<>("Tipo de cuenta");
        tipoCuenta.setItems("Ahorros", "Corriente");

        NumberField saldo = new NumberField("Saldo inicial");

        Button crear = new Button("Crear cuenta");

        // 🎨 Botón estilo banco
        crear.getStyle()
                .set("background", "#007566")
                .set("color", "white")
                .set("border-radius", "10px");

        crear.addClickListener(e -> {

            String cuenta = nombre.getValue() + " - " + tipoCuenta.getValue();

            cuentas.add(cuenta);
            grid.setItems(cuentas);

            Notification.show("Cuenta creada correctamente");

            nombre.clear();
            cedula.clear();
            saldo.clear();
            tipoCuenta.clear();
        });

        FormLayout form = new FormLayout();
        form.add(nombre, cedula, tipoCuenta, saldo, crear);

        form.setMaxWidth("600px");
        form.getStyle()
                .set("background", "white")
                .set("padding", "25px")
                .set("border-radius", "15px")
                .set("box-shadow", "0 6px 15px rgba(0,0,0,0.1)")
                .set("border-top", "5px solid #007566")
                .set("margin", "20px auto");

        // 🔹 GRID
        grid.addColumn(c -> c).setHeader("Cuentas creadas");
        grid.setWidth("600px");

        Div gridContainer = new Div(grid);
        gridContainer.setWidth("600px");
        gridContainer.getStyle()
                .set("background", "white")
                .set("padding", "20px")
                .set("border-radius", "15px")
                .set("box-shadow", "0 6px 15px rgba(0,0,0,0.1)")
                .set("border-top", "5px solid #146551")
                .set("margin", "20px auto");

        // 🔹 AGREGAR TODO
        add(titulo, contenedor, subtitulo, form, gridContainer);
    }

    // 🔥 TARJETAS ESTILO PRO
    private Div crearTarjeta(String titulo, String descripcion) {

        H2 tituloCuenta = new H2(titulo);
        Paragraph desc = new Paragraph(descripcion);

        Div card = new Div(tituloCuenta, desc);

        card.getStyle()
                .set("border-radius", "15px")
                .set("padding", "20px")
                .set("width", "260px")
                .set("background", "linear-gradient(135deg, #265C4B, #146551)")
                .set("color", "white")
                .set("box-shadow", "0 6px 15px rgba(0,0,0,0.2)")
                .set("cursor", "pointer")
                .set("transition", "0.3s");

        // ✨ Hover efecto
        card.getElement().addEventListener("mouseover", e ->
                card.getStyle().set("transform", "scale(1.05)")
        );

        card.getElement().addEventListener("mouseout", e ->
                card.getStyle().set("transform", "scale(1)")
        );

        card.addClickListener(e -> {
            Notification.show("Seleccionaste: " + titulo);
        });

        return card;
    }
}