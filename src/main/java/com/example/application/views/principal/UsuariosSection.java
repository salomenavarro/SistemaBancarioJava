package com.example.application.views.principal;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.select.Select;

public class UsuariosSection extends VerticalLayout {

    public UsuariosSection() {
    
        setSpacing(false);
        setPadding(false);
        setWidthFull();
        getStyle().set("background-color", "#F5F7F8"); 

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setWidthFull();
        mainContent.setMaxWidth("900px"); 
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.setAlignItems(Alignment.CENTER);

        H2 tituloPage = new H2("Mi Perfil y Configuración");
        tituloPage.addClassName("titulo-seccion");
        tituloPage.getStyle().set("color", "#265C4B"); 
        mainContent.add(tituloPage);

        mainContent.add(createDatosPersonalesSection());
        mainContent.add(createSeguridadSection());
        mainContent.add(createCardControlSection());

        add(mainContent);
        setAlignItems(Alignment.CENTER);
    }

    private Component createDatosPersonalesSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech"); 

        H3 titulo = new H3("Datos Personales");
        titulo.getStyle().set("color", "#146551"); 

        FormLayout form = new FormLayout();
        form.setWidthFull();

        TextField txtNombre = new TextField("Nombre completo");
        txtNombre.setValue("Juan Pérez Rodríguez");
        txtNombre.setReadOnly(true); 
        txtNombre.setSuffixComponent(VaadinIcon.EDIT.create()); 

        TextField txtEmail = new TextField("Correo electrónico");
        txtEmail.setValue("usuario@email.com");
        txtEmail.setReadOnly(true);

        TextField txtCelular = new TextField("Número de celular");
        txtCelular.setValue("+57 300 000 0000");
        txtCelular.setHelperText("Vinculado para transferencias");
        txtCelular.setReadOnly(true);

        TextField txtDireccion = new TextField("Dirección de correspondencia");
        txtDireccion.setValue("Calle 10 # 5-20, Medellín");
        txtDireccion.setHelperText("Donde llegan las tarjetas físicas");
        txtDireccion.setReadOnly(true);

        form.add(txtNombre, txtEmail, txtCelular, txtDireccion);
        
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );

        Button btnActualizar = new Button("Actualizar datos del contacto");
        btnActualizar.addClassName("btn-primario"); 
        btnActualizar.getStyle().set("margin-top", "15px");

        section.add(titulo, form, btnActualizar);
        section.setHorizontalComponentAlignment(Alignment.END, btnActualizar); 

        return section;
    }

    private Component createSeguridadSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech");

        H3 titulo = new H3("Seguridad");
        titulo.getStyle().set("color", "#146551");

        HorizontalLayout biometriaLayout = new HorizontalLayout();
        biometriaLayout.setWidthFull();
        biometriaLayout.setAlignItems(Alignment.CENTER);
        biometriaLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Span lblBiometria = new Span("Ingreso con Biometría (Huella/Rostro)");
        lblBiometria.getStyle().set("font-weight", "500");
        
        Checkbox toggleBiometria = new Checkbox();
        toggleBiometria.addClassName("toggle-switch"); 

        biometriaLayout.add(lblBiometria, toggleBiometria);

        HorizontalLayout clavesLayout = new HorizontalLayout();
        clavesLayout.setSpacing(true);
        clavesLayout.getStyle().set("margin-top", "10px");

        Button btnCambiarPass = new Button("Cambiar contraseña de la App", VaadinIcon.KEY.create());
        btnCambiarPass.addClassName("btn-secundario"); 

        Button btnGestionarPin = new Button("Gestionar PIN del cajero", VaadinIcon.PASSWORD.create());
        btnGestionarPin.addClassName("btn-secundario");

        clavesLayout.add(btnCambiarPass, btnGestionarPin);

        Div sesionesInfo = new Div();
        sesionesInfo.addClassName("info-estado");
        sesionesInfo.setText("Sesiones activas: 2 dispositivos (Este teléfono y Web)");
        sesionesInfo.getStyle().set("margin-top", "15px");

        Button btnCerrarSesion = new Button("Cerrar sesión", VaadinIcon.EXIT.create());
        btnCerrarSesion.addClassName("btn-peligro"); 
        btnCerrarSesion.getStyle().set("margin-top", "10px");

        section.add(titulo, biometriaLayout, new Hr(), clavesLayout, sesionesInfo, btnCerrarSesion);
        section.setHorizontalComponentAlignment(Alignment.START, btnCerrarSesion); 

        return section;
    }

    private Component createCardControlSection() {
        VerticalLayout section = new VerticalLayout();
        section.addClassName("card-fintech");
        section.getStyle().set("border-left", "6px solid #589A8D"); 

        H3 titulo = new H3("Gestión de Tarjeta");
        titulo.getStyle().set("color", "#146551");

        HorizontalLayout bloqueoLayout = new HorizontalLayout();
        bloqueoLayout.setWidthFull();
        bloqueoLayout.setAlignItems(Alignment.CENTER);
        bloqueoLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Span lblBloqueo = new Span("Bloqueo temporal de tarjeta");
        lblBloqueo.getStyle().set("font-weight", "600").set("color", "#265C4B");
        
        Checkbox toggleBloqueo = new Checkbox();
        toggleBloqueo.addClassName("toggle-switch");

        bloqueoLayout.add(lblBloqueo, toggleBloqueo);

        HorizontalLayout limiteLayout = new HorizontalLayout();
        limiteLayout.setWidthFull();
        limiteLayout.setAlignItems(Alignment.CENTER);
        limiteLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        limiteLayout.getStyle().set("margin-top", "10px");

        Span lblLimite = new Span("Límite diario de compras");
        
        Select<String> selectLimite = new Select<>();
        selectLimite.setItems("$500.000", "$1.000.000", "$2.000.000", "$5.000.000 (Máx)");
        selectLimite.setValue("$2.000.000"); 
        selectLimite.setWidth("180px");

        limiteLayout.add(lblLimite, selectLimite);

        HorizontalLayout internetLayout = new HorizontalLayout();
        internetLayout.setWidthFull();
        internetLayout.setAlignItems(Alignment.CENTER);
        internetLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        internetLayout.getStyle().set("margin-top", "10px");

        Span lblInternet = new Span("Compras por internet habilitadas");
        
        Checkbox toggleInternet = new Checkbox();
        toggleInternet.addClassName("toggle-switch");
        toggleInternet.setValue(true);

        internetLayout.add(lblInternet, toggleInternet);

        section.add(titulo, bloqueoLayout, new Hr(), limiteLayout, internetLayout);

        return section;
    }
}