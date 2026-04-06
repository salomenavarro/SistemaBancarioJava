package com.example.application.views.principal;

import com.example.application.modelo.Banco;
import com.example.application.modelo.Cuenta;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeroSection extends VerticalLayout {

    public HeroSection(Banco banco, Runnable alCerrarSesion) {
        setWidth("100%");
        setPadding(false);
        setSpacing(false);
        addClassName("hero_section");

        // Datos reales del cliente activo
        String nombre = banco.getClienteActivo() != null
            ? banco.getClienteActivo().getNombreCompleto()
            : "Bienvenido";

        Cuenta cuenta = banco.getClienteActivo() != null
            ? banco.getClienteActivo().getCuentaPrincipal()
            : null;

        String saldo   = cuenta != null ? "$" + String.format("%,.0f", cuenta.getSaldo()) : "Sin cuentas aún";
        String numero  = cuenta != null ? "**** " + cuenta.getNumeroCuenta().substring(Math.max(0, cuenta.getNumeroCuenta().length() - 4)) : "—";
        String tipo    = cuenta != null ? cuenta.getTipoCuenta() : "—";

        // ── LADO IZQUIERDO ────────────────────────────────────────────────────
        VerticalLayout izquierdo = new VerticalLayout();
        izquierdo.addClassName("hero_contenido_izquierdo");
        izquierdo.setPadding(false);
        izquierdo.setSpacing(false);

        Span etiqueta = new Span("Bienvenido, " + nombre);
        etiqueta.addClassName("hero_etiqueta");

        H1 titulo = new H1("Tu banco, simple y seguro");
        titulo.addClassName("hero_titulo");

        Paragraph descripcion = new Paragraph(
            "Administra tus finanzas, realiza transferencias y paga tus servicios desde un solo lugar.");
        descripcion.addClassName("hero_descripcion");

        HorizontalLayout botones = new HorizontalLayout();
        botones.addClassName("hero_botones");

        Button btnAbrir = new Button("Abrir cuenta");
        btnAbrir.addClassName("btn_primario");

        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.addClassName("btn_secundario");
        btnSalir.addClickListener(e -> {
            banco.cerrarSesion();
            alCerrarSesion.run();
        });

        botones.add(btnAbrir, btnSalir);

        HorizontalLayout chips = new HorizontalLayout();
        chips.addClassName("chip_acceso_rapido");
        chips.add(crearChip("Abrir cuenta"), crearChip("Transferir"),
                  crearChip("Pagar servicios"), crearChip("Mis créditos"));

        izquierdo.add(etiqueta, titulo, descripcion, botones, chips);

        // ── TARJETA ───────────────────────────────────────────────────────────
        Div tarjeta = new Div();
        tarjeta.addClassName("hero_tarjeta");

        tarjeta.add(
            crearParrafo("Tarjeta DESS", "tarjeta_label"),
            crearParrafo(nombre, "tarjeta_nombre"),
            crearParrafo("Saldo disponible", "tarjeta_saldo_label")
        );

        H2 h2Saldo = new H2(saldo);
        h2Saldo.addClassName("tarjeta_saldo");
        tarjeta.add(h2Saldo);

        HorizontalLayout filaTarjeta = new HorizontalLayout();
        filaTarjeta.addClassName("tarjeta_fila_info");
        filaTarjeta.add(crearItem("N° Cuenta", numero), crearItem("Tipo", tipo), crearItem("Válida", "12/28"));
        tarjeta.add(filaTarjeta);

        // ── ENSAMBLADO ────────────────────────────────────────────────────────
        HorizontalLayout contenedor = new HorizontalLayout();
        contenedor.addClassName("hero_contenedor");
        contenedor.setWidth("100%");
        contenedor.add(izquierdo, tarjeta);

        add(contenedor);
    }

    private Span crearChip(String texto) {
        Span chip = new Span(texto);
        chip.addClassName("hero_chip");
        return chip;
    }

    private Paragraph crearParrafo(String texto, String clase) {
        Paragraph p = new Paragraph(texto);
        p.addClassName(clase);
        return p;
    }

    private Div crearItem(String label, String valor) {
        Div item = new Div();
        item.addClassName("tarjeta_item");
        Span l = new Span(label); l.addClassName("tarjeta_item_label");
        Span v = new Span(valor); v.addClassName("tarjeta_item_valor");
        item.add(l, v);
        return item;
    }
}