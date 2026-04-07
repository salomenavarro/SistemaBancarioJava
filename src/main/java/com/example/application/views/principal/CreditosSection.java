package com.example.application.views.principal;

import com.example.application.modelo.Credito;
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

import java.util.ArrayList;
import java.util.List;

public class CreditosSection extends VerticalLayout {

        private List<Credito> solicitudes = new ArrayList<>();
        private Grid<Credito> grid = new Grid<>(Credito.class, false);

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
                                Credito credito = new Credito(
                                                tipo.getValue(),
                                                monto.getValue(),
                                                Integer.parseInt(plazo.getValue()),
                                                nombre.getValue());

                                solicitudes.add(credito);

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

                grid.addColumn(Credito::getCliente).setHeader("CLIENTE");
                grid.addColumn(Credito::getTipo).setHeader("TIPO");
                grid.addColumn(Credito::getMonto).setHeader("MONTO");
                grid.addColumn(Credito::getEstado).setHeader("ESTADO");

                grid.setAllRowsVisible(true);

                H3 titulo = new H3("Solicitudes registradas");
                titulo.addClassName("subtitle");

                Div container = new Div(titulo, grid);
                container.addClassName("grid-box");

                return container;

        }
}