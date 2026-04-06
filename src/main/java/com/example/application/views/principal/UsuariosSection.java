package com.example.application.views.principal;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.example.application.modelo.Cliente;

import java.util.Arrays;
import java.util.List;

public class UsuariosSection extends VerticalLayout {

    private Cliente cliente;
    private TextField txtNombre = new TextField("Nombre completo");
    private TextField txtEmail = new TextField("Correo electrónico");
    private TextField txtCelular = new TextField("Número de celular");
    private TextField txtDireccion = new TextField("Dirección de correspondencia");

    public UsuariosSection(Cliente cliente) {
   
        this.cliente = cliente;

        setWidthFull();
        setSpacing(true);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#F5F7F8");

        H2 tituloPage = new H2("Mi Perfil y Configuración");
        tituloPage.getStyle().set("color", "#265C4B");

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setMaxWidth("900px");
        mainContent.setWidthFull();
        mainContent.setSpacing(true);

        mainContent.add(createDatosPersonalesSection());
        mainContent.add(createSeguridadSection()); 
        mainContent.add(createCardControlSection());
        mainContent.add(createHistorialSesionesSection());

        add(tituloPage, mainContent);
    }

    private Component createDatosPersonalesSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech");

        H3 titulo = new H3("Datos Personales");
        titulo.getStyle().set("color", "#146551");

        txtNombre.setValue(cliente.getNombreCompleto());
        txtNombre.setPrefixComponent(VaadinIcon.USER.create());

        txtEmail.setValue(cliente.getCorreoElectronico());
        txtEmail.setPrefixComponent(VaadinIcon.ENVELOPE.create());

        txtCelular.setValue(cliente.getNumeroCelular());
        txtCelular.setHelperText("Vinculado para transferencias");

        txtDireccion.setValue(cliente.getDireccion());
        txtDireccion.setHelperText("Donde llegan las tarjetas físicas");

        FormLayout form = new FormLayout(txtNombre, txtEmail, txtCelular, txtDireccion);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("600px", 2));

        Button btnGuardar = new Button("Guardar Cambios", VaadinIcon.CHECK.create());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnGuardar.getStyle().set("background-color", "#146551");
        
        btnGuardar.addClickListener(e -> {
         
            cliente.setNombreCompleto(txtNombre.getValue());
            cliente.setCorreoElectronico(txtEmail.getValue());
            cliente.setNumeroCelular(txtCelular.getValue());
            cliente.setDireccion(txtDireccion.getValue());

            Notification.show("Perfil actualizado: " + cliente.getResumen(true))
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        section.add(titulo, form, btnGuardar);
        section.setHorizontalComponentAlignment(Alignment.END, btnGuardar);
        return section;
    }

    private Component createSeguridadSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech");

        H3 titulo = new H3("Seguridad");
        titulo.getStyle().set("color", "#146551");

        Span info = new Span("Tu cuenta tiene una clave provisional activa. Te recomendamos actualizarla por seguridad.");
        info.getStyle().set("font-size", "0.9em").set("color", "#D32F2F").set("font-weight", "500");

        Button btnPass = new Button("Cambiar Contraseña", VaadinIcon.KEY.create());
        btnPass.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnPass.getStyle().set("color", "#265C4B");

        btnPass.addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Actualizar Contraseña");

            PasswordField pf1 = new PasswordField("Nueva Contraseña");
            PasswordField pf2 = new PasswordField("Confirmar Contraseña");
            VerticalLayout dialogLayout = new VerticalLayout(pf1, pf2);
            
            Button saveButton = new Button("Actualizar", event -> {
                if(!pf1.getValue().isEmpty() && pf1.getValue().equals(pf2.getValue())) {
                    Notification.show("Contraseña actualizada").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    dialog.close();
                } else {
                    Notification.show("Las contraseñas no coinciden").addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });

            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button cancelButton = new Button("Cancelar", event -> dialog.close());
            dialog.getFooter().add(cancelButton, saveButton);
            dialog.add(dialogLayout);
            dialog.open();
        });

        section.add(titulo, info, btnPass);
        return section;
    }

    private Component createCardControlSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech");
        section.getStyle().set("border-left", "6px solid #589A8D");

        H3 titulo = new H3("Gestión de Tarjeta");
        titulo.getStyle().set("color", "#146551");

        Checkbox chkBloqueo = new Checkbox("Bloqueo temporal de tarjeta");
        chkBloqueo.addValueChangeListener(event -> {
            if (event.getValue()) {
                Notification.show("¡TARJETA BLOQUEADA!", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                Notification.show("Tarjeta reactivada").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        Select<String> limite = new Select<>();
        limite.setLabel("Límite diario de compras");
        limite.setItems("$500.000", "$1.000.000", "$2.000.000");
        limite.setValue("$1.000.000");
        limite.addValueChangeListener(e -> Notification.show("Nuevo límite: " + e.getValue()));

        section.add(titulo, chkBloqueo, limite);
        return section;
    }

    private Component createHistorialSesionesSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech");

        H3 titulo = new H3("Últimos Inicios de Sesión");
        titulo.getStyle().set("color", "#146551");

        record Sesion(String fecha, String dispositivo, String ubicacion) {}

        Grid<Sesion> grid = new Grid<>(Sesion.class, false);
        grid.addColumn(Sesion::fecha).setHeader("Fecha y Hora");
        grid.addColumn(Sesion::dispositivo).setHeader("Dispositivo");
        grid.addColumn(Sesion::ubicacion).setHeader("Ubicación");

        grid.setItems(Arrays.asList(
            new Sesion("2026-04-04 01:45", "Chrome - Windows 11", "Medellín, ANT"),
            new Sesion("2026-04-03 18:20", "App Móvil - iPhone 15", "Envigado, ANT"),
            new Sesion("2026-01-15 09:12", "Edge - MacBook Pro", "Bogotá, DC")
        ));
        grid.setAllRowsVisible(true);

        section.add(titulo, grid);
        return section;
    }
}