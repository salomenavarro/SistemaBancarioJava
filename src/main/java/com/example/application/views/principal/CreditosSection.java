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
        
        // Se mantiene el banner informativo del tipo de crédito
        Span textoBanner = new Span("Tipo seleccionado: Vehicular");
        HorizontalLayout banner = crearBanner(textoBanner);

        VerticalLayout formulario = crearFormulario(textoBanner);
        Div tabla = crearTabla();

        // Se eliminaron las tarjetas como solicitaste
        add(titulo, banner, formulario, tabla);
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
        
        // Cambiamos a NumberField para que sea coherente con Monto y evitar errores de texto
        NumberField plazo = new NumberField("Plazo (meses)");
        
        TextField cuota = new TextField("Cuota estimada");
        cuota.setReadOnly(true);

        Button btn = new Button("Solicitar crédito");
        btn.setWidthFull();
        btn.addClassName("btn-main");

        btn.addClickListener(e -> {
            // Validación para asegurar que los campos numéricos no sean nulos
            if (!nombre.isEmpty() && monto.getValue() != null && plazo.getValue() != null) {
                
                // Se instancia el objeto de la clase Credito
                Credito credito = new Credito(
                        tipo.getValue(),
                        monto.getValue(),
                        plazo.getValue().intValue(),
                        nombre.getValue());

                // Se muestra la cuota calculada usando el método de la clase modelo
                cuota.setValue(String.format("$%.2f", credito.calcularCuota()));

                solicitudes.add(credito);
                
                // Actualizamos el Grid con la lista de solicitudes
                grid.setItems(new ArrayList<>(solicitudes));

                Notification.show("Solicitud enviada");

                // Limpiar campos de entrada
                nombre.clear();
                cedula.clear();
                monto.clear();
                plazo.clear();
            } else {
                Notification.show("Por favor, complete todos los campos");
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
        // Definición de columnas usando los métodos de la clase Credito
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