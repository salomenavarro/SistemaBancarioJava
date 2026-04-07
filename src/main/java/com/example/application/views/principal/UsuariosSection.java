package com.example.application.views.principal;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.example.application.modelo.Cliente;

public class UsuariosSection extends VerticalLayout {

    // El cliente cuyo perfil estamos mostrando
    private Cliente cliente;

    // Campos del formulario de datos personales
    private TextField txtNombre = new TextField("Nombre completo");
    private TextField txtEmail = new TextField("Correo electrónico");
    private TextField txtCelular = new TextField("Número de celular");
    private TextField txtDireccion = new TextField("Dirección");

    public UsuariosSection(Cliente cliente) {
        this.cliente = cliente;

        setWidthFull();
        setSpacing(true);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#F5F7F8");

        H2 titulo = new H2("Mi Perfil y Configuración");
        titulo.getStyle().set("color", "#265C4B");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setMaxWidth("900px");
        contenido.setWidthFull();
        contenido.setSpacing(true);

        contenido.add(crearSeccionDatos());
        contenido.add(crearSeccionContrasena());
        contenido.add(crearSeccionTarjeta());

        add(titulo, contenido);
    }

    // Sección 1: Datos personales del cliente
    private Component crearSeccionDatos() {
        VerticalLayout seccion = new VerticalLayout();
        seccion.addClassName("card-fintech");

        H3 titulo = new H3("Datos Personales");
        titulo.getStyle().set("color", "#146551");

        // Cargamos los datos del objeto Cliente en los campos
        txtNombre.setValue(cliente.getNombreCompleto());
        txtNombre.setPrefixComponent(VaadinIcon.USER.create());

        txtEmail.setValue(cliente.getCorreoElectronico());
        txtEmail.setPrefixComponent(VaadinIcon.ENVELOPE.create());

        txtCelular.setValue(cliente.getNumeroCelular());
        txtDireccion.setValue(cliente.getDireccion());

        FormLayout form = new FormLayout(txtNombre, txtEmail, txtCelular, txtDireccion);

        Button btnGuardar = new Button("Guardar cambios", VaadinIcon.CHECK.create());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGuardar.getStyle().set("background-color", "#146551");

        btnGuardar.addClickListener(e -> {
            // Actualizamos el objeto Cliente con los valores del formulario
            cliente.setNombreCompleto(txtNombre.getValue());
            cliente.setCorreoElectronico(txtEmail.getValue());
            cliente.setNumeroCelular(txtCelular.getValue());
            cliente.setDireccion(txtDireccion.getValue());

            Notification.show("Perfil actualizado: " + cliente.getResumen(true))
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        seccion.add(titulo, form, btnGuardar);
        seccion.setHorizontalComponentAlignment(Alignment.END, btnGuardar);
        return seccion;
    }

    // Sección 2: Cambiar contraseña — sin ventana emergente, directo en la página
    private Component crearSeccionContrasena() {
        VerticalLayout seccion = new VerticalLayout();
        seccion.addClassName("card-fintech");

        H3 titulo = new H3("Seguridad");
        titulo.getStyle().set("color", "#146551");

        PasswordField nuevaContrasena = new PasswordField("Nueva contraseña");
        PasswordField confirmarContrasena = new PasswordField("Confirmar contraseña");

        Button btnCambiar = new Button("Cambiar contraseña");
        btnCambiar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        btnCambiar.addClickListener(e -> {
            String nueva = nuevaContrasena.getValue();
            String confirmar = confirmarContrasena.getValue();

            if (nueva.isEmpty()) {
                Notification.show("Escribe una contraseña nueva")
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (!nueva.equals(confirmar)) {
                Notification.show("Las contraseñas no coinciden")
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // Actualizamos la contraseña en el objeto Cliente
            cliente.setContrasena(nueva);
            Notification.show("Contraseña actualizada")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            nuevaContrasena.clear();
            confirmarContrasena.clear();
        });

        seccion.add(titulo, nuevaContrasena, confirmarContrasena, btnCambiar);
        return seccion;
    }

    // Sección 3: Bloqueo de tarjeta y límite diario
    private Component crearSeccionTarjeta() {
        VerticalLayout seccion = new VerticalLayout();
        seccion.addClassName("card-fintech");
        seccion.getStyle().set("border-left", "6px solid #589A8D");

        H3 titulo = new H3("Gestión de Tarjeta");
        titulo.getStyle().set("color", "#146551");

        Checkbox chkBloqueo = new Checkbox("Bloqueo temporal de tarjeta");
        chkBloqueo.addValueChangeListener(event -> {
            if (event.getValue()) {
                Notification.show("Tarjeta bloqueada", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                Notification.show("Tarjeta reactivada")
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        Select<String> limite = new Select<>();
        limite.setLabel("Límite diario de compras");
        limite.setItems("$500.000", "$1.000.000", "$2.000.000");
        limite.setValue("$1.000.000");
        limite.addValueChangeListener(e ->
            Notification.show("Nuevo límite: " + e.getValue())
        );

        seccion.add(titulo, chkBloqueo, limite);
        return seccion;
    }
}