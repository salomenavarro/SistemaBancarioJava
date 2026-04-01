package com.example.application.views.principal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

public class TiposDeCuentasSection extends VerticalLayout {

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
}