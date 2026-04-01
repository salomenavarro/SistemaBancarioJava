package com.example.application.views.principal;

<<<<<<< HEAD
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
=======
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
>>>>>>> a6fd35cf9f6f1965fa9bd1696ed68cf05159f064

import java.util.ArrayList;
import java.util.List;

public class CreditosSection extends VerticalLayout {

<<<<<<< HEAD
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
=======
        public static class SolicitudCredito {
                private String cliente, tipo, monto, estado;

                public SolicitudCredito(String cliente, String tipo, String monto, String estado) {
                        this.cliente = cliente;
                        this.tipo = tipo;
                        this.monto = monto;
                        this.estado = estado;
                }

                public String getCliente() {
                        return cliente;
                }

                public String getTipo() {
                        return tipo;
                }

                public String getMonto() {
                        return monto;
                }

                public String getEstado() {
                        return estado;
                }
        }

        private List<SolicitudCredito> solicitudes = new ArrayList<>();
        private Grid<SolicitudCredito> grid = new Grid<>(SolicitudCredito.class, false);

        public CreditosSection() {

                configurarLayout();

                H2 titulo = crearTitulo();
                HorizontalLayout tarjetas = crearTarjetas();

                Span textoBanner = new Span("Tipo seleccionado: Vehicular");
                HorizontalLayout banner = crearBanner(textoBanner);

                VerticalLayout formulario = crearFormulario(textoBanner);
                Div tabla = crearTabla();

                add(titulo, tarjetas, banner, formulario, tabla);
        }

        private void configurarLayout() {
                setWidthFull();
                setPadding(true);
                setSpacing(true);
                setAlignItems(Alignment.CENTER);

                addClassName("view-background");
        }

        private H2 crearTitulo() {
                H2 titulo = new H2("Mis Créditos Bancarios");
                titulo.addClassName("title");
                return titulo;
        }

        private HorizontalLayout crearTarjetas() {

                Div t1 = crearTarjeta("Activo", "$5.000.000", "Crédito Personal", "Cuota", "$312.500", true);
                Div t2 = crearTarjeta("Disponible", "$15.000.000", "Libre Inversión", "Tasa", "1.5% MV", false);

                HorizontalLayout layout = new HorizontalLayout(t1, t2);
                layout.setSpacing(true);
                layout.setJustifyContentMode(JustifyContentMode.CENTER);

                return layout;
        }

        private Div crearTarjeta(String estado, String monto, String titulo,
                        String d1, String v1, boolean destacada) {

                Span badge = new Span(estado);
                badge.addClassName("badge");

                H3 montoTxt = new H3(monto);

                Paragraph subtitulo = new Paragraph(titulo);

                HorizontalLayout fila = new HorizontalLayout(new Span(d1), new Span(v1));
                fila.setWidthFull();
                fila.setJustifyContentMode(JustifyContentMode.BETWEEN);

                Div card = new Div(badge, montoTxt, subtitulo, fila);

                card.addClassName("card");

                if (destacada) {
                        card.addClassName("card-highlight");
                }

                return card;
        }

        private HorizontalLayout crearBanner(Span textoBanner) {

                Icon icon = VaadinIcon.ARROW_DOWN.create();

                HorizontalLayout banner = new HorizontalLayout(icon, textoBanner);
                banner.addClassName("banner");

                banner.setAlignItems(Alignment.CENTER);

                return banner;
        }

        private VerticalLayout crearFormulario(Span textoBanner) {

                H3 titulo = new H3("Solicitar crédito");
                titulo.addClassName("subtitle");

                TextField nombre = new TextField("Nombre");
                TextField cedula = new TextField("Cédula");

                ComboBox<String> tipo = new ComboBox<>("Tipo");
                tipo.setItems("Vehicular", "Personal", "Hipotecario");
                tipo.setValue("Vehicular");

                tipo.addValueChangeListener(event -> {
                        if (event.getValue() != null) {
                                textoBanner.setText("Tipo seleccionado: " + event.getValue());
                        }
                });

                NumberField monto = new NumberField("Monto");

                TextField plazo = new TextField("Plazo");
                TextField cuota = new TextField("Cuota estimada");
                cuota.setReadOnly(true);

                Button btn = new Button("Solicitar crédito");
                btn.setWidthFull();
                btn.addClassName("btn-main");

                btn.addClickListener(e -> {

                        if (!nombre.isEmpty()) {
                                solicitudes.add(new SolicitudCredito(
                                                nombre.getValue(),
                                                tipo.getValue(),
                                                "$" + monto.getValue(),
                                                "Pendiente"));

                                grid.setItems(solicitudes);

                                Notification.show("Solicitud enviada");

                                nombre.clear();
                                cedula.clear();
                                monto.clear();
                                plazo.clear();
                        }
                });

                FormLayout form = new FormLayout();
                form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
                form.add(nombre, cedula, tipo, monto, plazo, cuota);

                VerticalLayout container = new VerticalLayout(titulo, form, btn);
                container.addClassName("box");

                return container;
        }

        private Div crearTabla() {

                grid.addColumn(SolicitudCredito::getCliente).setHeader("CLIENTE");
                grid.addColumn(SolicitudCredito::getTipo).setHeader("TIPO");
                grid.addColumn(SolicitudCredito::getMonto).setHeader("MONTO");
                grid.addColumn(SolicitudCredito::getEstado).setHeader("ESTADO");

                grid.setAllRowsVisible(true);

                H3 titulo = new H3("Solicitudes registradas");
                titulo.addClassName("subtitle");

                Div container = new Div(titulo, grid);
                container.addClassName("grid-box");

                return container;

        }
>>>>>>> a6fd35cf9f6f1965fa9bd1696ed68cf05159f064
}