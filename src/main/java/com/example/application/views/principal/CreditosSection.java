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

public class CreditosSection extends VerticalLayout {

    private List<String> creditos = new ArrayList<>();
    private Grid<String> grid = new Grid<>();

    public CreditosSection() {

        setWidth("100%");
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        // 🎨 Fondo general
        getStyle().set("background", "#8FC1B5");

        // 🔹 TÍTULO
        H2 titulo = new H2("Créditos");

        // 🔹 TARJETAS
        Div personal = crearTarjeta(
                "Crédito Personal",
                "Para gastos personales y libres."
        );

        Div hipotecario = crearTarjeta(
                "Crédito Hipotecario",
                "Para compra de vivienda."
        );

        Div vehicular = crearTarjeta(
                "Crédito Vehicular",
                "Para compra de vehículos."
        );

        HorizontalLayout tarjetas = new HorizontalLayout(personal, hipotecario, vehicular);
        tarjetas.setSpacing(true);

        // 🔹 FORMULARIO
        H2 subtitulo = new H2("Solicitar Crédito");

        TextField nombre = new TextField("Nombre del cliente");
        TextField cedula = new TextField("Cédula");

        ComboBox<String> tipoCredito = new ComboBox<>("Tipo de crédito");
        tipoCredito.setItems("Personal", "Hipotecario", "Vehicular");

        NumberField monto = new NumberField("Monto solicitado");

        Button solicitar = new Button("Solicitar");

        // 🎨 Botón estilo banco
        solicitar.getStyle()
                .set("background", "#007566")
                .set("color", "white")
                .set("border-radius", "10px");

        solicitar.addClickListener(e -> {

            String credito = nombre.getValue() + " - " + tipoCredito.getValue();

            creditos.add(credito);
            grid.setItems(creditos);

            Notification.show("Crédito solicitado correctamente");

            nombre.clear();
            cedula.clear();
            monto.clear();
            tipoCredito.clear();
        });

        FormLayout form = new FormLayout();
        form.add(nombre, cedula, tipoCredito, monto, solicitar);

        form.setMaxWidth("600px");
        form.getStyle()
                .set("background", "white")
                .set("padding", "25px")
                .set("border-radius", "15px")
                .set("box-shadow", "0 6px 15px rgba(0,0,0,0.1)")
                .set("border-top", "5px solid #265C4B")
                .set("margin", "20px auto");

        // 🔹 GRID
        grid.addColumn(c -> c).setHeader("Solicitudes de crédito");
        grid.setWidth("600px");

        Div gridContainer = new Div(grid);
        gridContainer.setWidth("600px");
        gridContainer.getStyle()
                .set("background", "white")
                .set("padding", "20px")
                .set("border-radius", "15px")
                .set("box-shadow", "0 6px 15px rgba(0,0,0,0.1)")
                .set("border-top", "5px solid #007566")
                .set("margin", "20px auto");

        // 🔹 AGREGAR TODO
        add(titulo, tarjetas, subtitulo, form, gridContainer);
    }

    // 🔥 TARJETAS ESTILO PRO (créditos con variante de color)
    private Div crearTarjeta(String titulo, String descripcion) {

        H2 tituloCredito = new H2(titulo);
        Paragraph desc = new Paragraph(descripcion);

        Div card = new Div(tituloCredito, desc);

        card.getStyle()
                .set("border-radius", "15px")
                .set("padding", "20px")
                .set("width", "260px")
                .set("background", "linear-gradient(135deg, #007566, #589A8D)")
                .set("color", "white")
                .set("box-shadow", "0 6px 15px rgba(0,0,0,0.2)")
                .set("cursor", "pointer")
                .set("transition", "0.3s");

        // ✨ Hover
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