package com.example.application.views.principal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.details.Details;

public class UsuariosSection extends VerticalLayout {

    public UsuariosSection() {
        addClassName("user-section-container");
        setAlignItems(Alignment.CENTER);
        setSpacing(true);

        // --- 1. TARJETA BANCARIA VISUAL ---
        add(createBankCard());

        // --- 2. CONTENEDOR PRINCIPAL (PANEL BLANCO) ---
        VerticalLayout mainPanel = new VerticalLayout();
        mainPanel.addClassName("info-card");
        mainPanel.setWidth("450px");
        mainPanel.setSpacing(true);

        // --- SECCIÓN: MI PERFIL (Datos Básicos) ---
        H3 profileTitle = new H3("Mi Perfil");
        profileTitle.getStyle().set("color", "#146551").set("margin", "0");
        
        TextField fullName = new TextField("Nombre del Usuario");
        fullName.setValue("Alex Martínez");
        fullName.setPrefixComponent(VaadinIcon.USER.create());
        fullName.setWidthFull();

        TextField linkedPhone = new TextField("Teléfono Vinculado");
        linkedPhone.setValue("+57 300 000 0000");
        linkedPhone.setPrefixComponent(VaadinIcon.PHONE.create());
        linkedPhone.setWidthFull();

        // --- SECCIÓN: GESTIÓN DE CUENTA (Datos Avanzados) ---
        Hr separator = new Hr();
        H4 managementTitle = new H4("Gestión de Cuenta");
        managementTitle.getStyle().set("color", "#265C4B");

        TextField alias = new TextField("Alias de Usuario");
        alias.setPlaceholder("Ej: Alex_Fintech");
        alias.setPrefixComponent(VaadinIcon.USER_CARD.create());
        alias.setWidthFull();

        TextField contactEmail = new TextField("Información de Contacto");
        contactEmail.setPlaceholder("correo@ejemplo.com");
        contactEmail.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        contactEmail.setWidthFull();

        // --- DESPLEGABLES (Límites y Credenciales) ---
        Details limites = new Details("Límites Transaccionales", 
            new VerticalLayout(
                new Span("Límite diario: $5.000.000"),
                new Span("Transferencias mensuales: $50.000.000"),
                new Button("Gestionar Límites", VaadinIcon.EDIT.create())
            )
        );
        limites.setWidthFull();

        Details credenciales = new Details("Gestión de Credenciales", 
            new VerticalLayout(
                new PasswordField("Contraseña Actual"),
                new PasswordField("Nueva Contraseña"),
                new Button("Actualizar Seguridad", VaadinIcon.KEY.create())
            )
        );
        credenciales.setWidthFull();

        // --- BOTÓN PRINCIPAL DE GUARDADO ---
        Button btnSaveAll = new Button("Guardar Perfil Completo", VaadinIcon.SAFE_LOCK.create());
        btnSaveAll.addClassName("action-button");
        btnSaveAll.setWidthFull();

        // Añadimos todo al panel en orden
        mainPanel.add(profileTitle, fullName, linkedPhone, separator, 
                       managementTitle, alias, contactEmail, limites, 
                       credenciales, btnSaveAll);
        
        add(mainPanel);
    }

    private Div createBankCard() {
        Div card = new Div();
        card.addClassName("bank-card-visual");
        card.setWidth("320px");

        Div chip = new Div();
        chip.addClassName("card-chip");

        Span num = new Span("**** **** **** 8842");
        num.addClassName("card-number");

        HorizontalLayout footer = new HorizontalLayout(new Span("ALEX MARTÍNEZ"), new Span("12/28"));
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.BETWEEN);

        card.add(chip, num, footer);
        return card;
    }
}