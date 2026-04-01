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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
>>>>>>> a6fd35cf9f6f1965fa9bd1696ed68cf05159f064

import java.util.ArrayList;
import java.util.List;

public class TiposDeCuentasSection extends VerticalLayout {

<<<<<<< HEAD
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
=======
        private List<String> cuentas = new ArrayList<>();
        private Grid<String> grid = new Grid<>();

        public TiposDeCuentasSection() {

                configurarLayout();

                H2 titulo = crearTitulo();
                HorizontalLayout tarjetas = crearTarjetas();
                VerticalLayout formulario = crearFormulario();
                Div tabla = crearTabla();

                add(titulo, tarjetas, formulario, tabla);
        }

        private void configurarLayout() {
                setWidthFull();
                setPadding(true);
                setSpacing(true);
                setAlignItems(Alignment.CENTER);

                addClassName("view-background");
        }

        private H2 crearTitulo() {
                H2 titulo = new H2("Tipos de Cuentas");
                titulo.addClassName("title");
                return titulo;
        }

        private HorizontalLayout crearTarjetas() {

                // me dio pereza poner los iconos, espero entiendas la tragedia, boss. 

                Div ahorro = crearTarjeta(
                                "Cuenta de Ahorros",
                                "Guarda tu dinero y genera intereses.",
                                "3% interés anual");

                Div corriente = crearTarjeta(
                                "Cuenta Corriente",
                                "Manejo diario con sobregiro.",
                                "Uso frecuente");

                Div premium = crearTarjeta(
                                "Cuenta Premium",
                                "Beneficios exclusivos y atención prioritaria.",
                                "Sin restricciones");

                HorizontalLayout layout = new HorizontalLayout(ahorro, corriente, premium);
                layout.setSpacing(true);
                layout.setJustifyContentMode(JustifyContentMode.CENTER);

                return layout;
        }

        private Div crearTarjeta(String titulo, String descripcion, String dato) {

                Span badge = new Span(dato);
                badge.addClassName("badge");

                H3 tituloTxt = new H3(titulo);
                Paragraph desc = new Paragraph(descripcion);

                Div card = new Div(tituloTxt, desc, badge);

                card.addClassName("card");
                card.addClassName("card-highlight");

                card.addClickListener(e -> Notification.show("Seleccionaste: " + titulo));

                return card;
        }

        private VerticalLayout crearFormulario() {

                H3 subtitulo = new H3("Crear Cuenta");
                subtitulo.addClassName("subtitle");

                TextField nombre = new TextField("Nombre");
                TextField cedula = new TextField("Cédula");

                ComboBox<String> tipoCuenta = new ComboBox<>("Tipo de cuenta");
                tipoCuenta.setItems("Ahorros", "Corriente", "Premium");

                NumberField saldo = new NumberField("Saldo inicial");

                Button crear = new Button("Crear cuenta");
                crear.setWidthFull();
                crear.addClassName("btn-main");

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
                form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
                form.add(nombre, cedula, tipoCuenta, saldo);

                VerticalLayout container = new VerticalLayout(subtitulo, form, crear);
                container.addClassName("box");

                return container;
        }

        private Div crearTabla() {

                grid.addColumn(c -> c).setHeader("Cuentas creadas");
                grid.setAllRowsVisible(true);

                H3 tituloTabla = new H3("Registros");
                tituloTabla.addClassName("subtitle");

                Div container = new Div(tituloTabla, grid);
                container.addClassName("grid-box");

                return container;
        }
>>>>>>> a6fd35cf9f6f1965fa9bd1696ed68cf05159f064
}