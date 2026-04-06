package com.example.application.views.principal;

import com.example.application.modelo.Banco;
import com.example.application.modelo.Cliente;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class LoginSection extends VerticalLayout {

    private Banco banco;
    private Runnable alIniciarSesion; // qué hace cuando el login es exitoso
    private Runnable alRegistrarse;   // qué hace cuando el registro es exitoso

    private Div panelLogin;
    private Div panelRegistro;

    public LoginSection(Banco banco, Runnable alIniciarSesion, Runnable alRegistrarse) {
        this.banco           = banco;
        this.alIniciarSesion = alIniciarSesion;
        this.alRegistrarse   = alRegistrarse;

        setWidth("100%");
        setAlignItems(Alignment.CENTER);
        addClassName("login_section");

        // Logo
        H1 logo = new H1("DESS");
        logo.addClassName("login_nombre_banco");

        Paragraph slogan = new Paragraph("Tu banco, simple y seguro");
        slogan.addClassName("login_slogan");

        // Construye los dos paneles
        panelLogin    = crearPanelLogin();
        panelRegistro = crearPanelRegistro();
        panelRegistro.setVisible(false); // empieza mostrando el login

        add(logo, slogan, panelLogin, panelRegistro);
    }

    private Div crearPanelLogin() {
        Div panel = new Div();
        panel.addClassName("login_panel");

        H2 titulo = new H2("Bienvenido de nuevo");
        titulo.addClassName("login_titulo");

        EmailField campoEmail = new EmailField("Correo electrónico");
        campoEmail.addClassName("campo_full");

        PasswordField campoContrasena = new PasswordField("Contraseña");
        campoContrasena.addClassName("campo_full");

        Span error = new Span();
        error.addClassName("login_error");
        error.setVisible(false);

        Button btnEntrar = new Button("Entrar");
        btnEntrar.addClassName("btn_primario_verde");
        btnEntrar.addClickListener(e -> {
            error.setVisible(false);

            if (campoEmail.isEmpty() || campoContrasena.isEmpty()) {
                error.setText("Completa todos los campos.");
                error.setVisible(true);
                return;
            }

            if (banco.iniciarSesion(campoEmail.getValue(), campoContrasena.getValue())) {
                alIniciarSesion.run(); // → PrincipalView muestra la app
            } else {
                error.setText("Email o contraseña incorrectos.");
                error.setVisible(true);
            }
        });

        Span linkRegistro = new Span("¿No tienes cuenta? Regístrate");
        linkRegistro.addClassName("login_link");
        linkRegistro.addClickListener(e -> {
            panelLogin.setVisible(false);
            panelRegistro.setVisible(true);
        });

        panel.add(titulo, campoEmail, campoContrasena, error, btnEntrar, linkRegistro);
        return panel;
    }

    private Div crearPanelRegistro() {
        Div panel = new Div();
        panel.addClassName("login_panel");

        H2 titulo = new H2("Crea tu cuenta");
        titulo.addClassName("login_titulo");

        TextField campoNombre = new TextField("Nombre completo");
        campoNombre.addClassName("campo_full");

        TextField campoCedula = new TextField("Cédula");
        campoCedula.addClassName("campo_full");

        EmailField campoEmail = new EmailField("Correo electrónico");
        campoEmail.addClassName("campo_full");

        PasswordField campoContrasena = new PasswordField("Contraseña");
        campoContrasena.setPlaceholder("Mínimo 6 caracteres");
        campoContrasena.addClassName("campo_full");

        Span mensaje = new Span();
        mensaje.addClassName("login_error");
        mensaje.setVisible(false);

        Button btnCrear = new Button("Crear cuenta");
        btnCrear.addClassName("btn_primario_verde");
        btnCrear.addClickListener(e -> {
            mensaje.setVisible(false);

            if (campoNombre.isEmpty() || campoCedula.isEmpty()
                    || campoEmail.isEmpty() || campoContrasena.isEmpty()) {
                mensaje.setText("Completa todos los campos.");
                mensaje.setVisible(true);
                return;
            }

            if (campoContrasena.getValue().length() < 6) {
                mensaje.setText("La contraseña debe tener mínimo 6 caracteres.");
                mensaje.setVisible(true);
                return;
            }

            Cliente nuevo = new Cliente(
                campoNombre.getValue(),
                campoCedula.getValue(),
                campoEmail.getValue(),
                campoContrasena.getValue()
            );

            if (banco.registrarCliente(nuevo)) {
                // Registro exitoso → avisa y vuelve al login
                Notification notif = new Notification("¡Cuenta creada! Ahora inicia sesión.", 3000);
                notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notif.setPosition(Notification.Position.TOP_CENTER);
                notif.open();
                alRegistrarse.run(); // → PrincipalView muestra el login
            } else {
                mensaje.setText("Ya existe una cuenta con ese email o cédula.");
                mensaje.setVisible(true);
            }
        });

        Span linkLogin = new Span("¿Ya tienes cuenta? Inicia sesión");
        linkLogin.addClassName("login_link");
        linkLogin.addClickListener(e -> {
            panelRegistro.setVisible(false);
            panelLogin.setVisible(true);
        });

        panel.add(titulo, campoNombre, campoCedula, campoEmail,
                  campoContrasena, mensaje, btnCrear, linkLogin);
        return panel;
    }
}