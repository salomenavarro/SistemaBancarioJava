package com.example.application.views.principal;

import com.example.application.modelo.Banco;
import com.example.application.modelo.Cuenta;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import java.util.List;

public class HeroSection extends VerticalLayout {

    private Banco banco;
    private Span etiqueta;
    private HorizontalLayout contenedorTarjetas;

    public HeroSection(Banco banco, Runnable alCerrarSesion,
                       Component seccionCuentas, Component seccionTransferencias,
                       Component seccionPagos, Component seccionCreditos) {

        this.banco = banco;
        setWidth("100%");
        setPadding(false);
        setSpacing(false);
        addClassName("hero_section");

        // --- IZQUIERDA ---
        VerticalLayout izquierdo = new VerticalLayout();
        izquierdo.addClassName("hero_contenido_izquierdo");
        izquierdo.setPadding(false);
        izquierdo.setSpacing(false);

        this.etiqueta = new Span();
        this.etiqueta.addClassName("hero_etiqueta");

        H1 titulo = new H1("Tu banco, simple y seguro");
        titulo.addClassName("hero_titulo");

        Paragraph descripcion = new Paragraph("Administra tus finanzas, realiza transferencias y paga tus servicios.");
        descripcion.addClassName("hero_descripcion");

        HorizontalLayout botones = new HorizontalLayout();
        botones.addClassName("hero_botones");

        Button btnAbrir = new Button("Abrir cuenta");
        btnAbrir.addClassName("btn_primario");
        btnAbrir.addClickListener(e -> seccionCuentas.getElement().callJsFunction("scrollIntoView", "{behavior: 'smooth', block: 'start'}"));

        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.addClassName("btn_secundario");
        btnSalir.addClickListener(e -> {
            banco.cerrarSesion();
            alCerrarSesion.run();
        });
        botones.add(btnAbrir, btnSalir);

        HorizontalLayout chips = new HorizontalLayout();
        chips.addClassName("chip_acceso_rapido");
        chips.add(
                crearChipConScroll("Abrir cuenta", seccionCuentas),
                crearChipConScroll("Transferir", seccionTransferencias),
                crearChipConScroll("Pagar servicios", seccionPagos),
                crearChipConScroll("Mis créditos", seccionCreditos));

        izquierdo.add(etiqueta, titulo, descripcion, botones, chips);

        // --- DERECHA (CARRUSEL CON FLECHAS) ---
        this.contenedorTarjetas = new HorizontalLayout();
        this.contenedorTarjetas.addClassName("hero_tarjeta_scroller");
        this.contenedorTarjetas.setSpacing(true);
        this.contenedorTarjetas.setPadding(false);

        // Envolvemos las tarjetas en un Scroller para permitir el movimiento
        Scroller scroller = new Scroller(this.contenedorTarjetas);
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        scroller.setWidth("400px"); // Ajusta este ancho según tu diseño
        scroller.addClassName("no-scrollbar"); // Clase para ocultar barras si lo deseas

        // Botones de flecha
        Button btnAnterior = new Button(VaadinIcon.CHEVRON_LEFT.create());
        btnAnterior.addClassName("btn_flecha_carrusel");
        btnAnterior.addClickListener(e -> {
            scroller.getElement().executeJs("this.scrollBy({left: -350, behavior: 'smooth'})");
        });

        Button btnSiguiente = new Button(VaadinIcon.CHEVRON_RIGHT.create());
        btnSiguiente.addClassName("btn_flecha_carrusel");
        btnSiguiente.addClickListener(e -> {
            scroller.getElement().executeJs("this.scrollBy({left: 350, behavior: 'smooth'})");
        });

        // Contenedor que junta Flecha - Scroller - Flecha
        HorizontalLayout layoutCarrusel = new HorizontalLayout(btnAnterior, scroller, btnSiguiente);
        layoutCarrusel.setAlignItems(Alignment.CENTER);
        layoutCarrusel.setSpacing(false);

        HorizontalLayout contenedorPrincipal = new HorizontalLayout(izquierdo, layoutCarrusel);
        contenedorPrincipal.addClassName("hero_contenedor");
        contenedorPrincipal.setWidth("100%");
        add(contenedorPrincipal);

        actualizarInterfaz();
    }

    public void actualizarInterfaz() {
        if (banco.getClienteActivo() == null) return;

        String nombre = banco.getClienteActivo().getNombreCompleto();
        etiqueta.setText("Bienvenido, " + nombre);

        contenedorTarjetas.removeAll();

        List<Cuenta> cuentas = banco.getClienteActivo().getCuentas();

        if (cuentas.isEmpty()) {
            contenedorTarjetas.add(crearTarjetaVisual("Sin nombre", null));
        } else {
            for (Cuenta c : cuentas) {
                contenedorTarjetas.add(crearTarjetaVisual(nombre, c));
            }
        }
    }

    private Div crearTarjetaVisual(String nombreCliente, Cuenta cuenta) {
        Div tarjeta = new Div();
        tarjeta.addClassName("hero_tarjeta");

        Paragraph label = new Paragraph("Tarjeta DESS");
        label.addClassName("tarjeta_label");

        Paragraph nombreP = new Paragraph(nombreCliente);
        nombreP.addClassName("tarjeta_nombre");

        Paragraph saldoLabel = new Paragraph("Saldo disponible");
        saldoLabel.addClassName("tarjeta_saldo_label");

        H2 saldoH2 = new H2(cuenta != null ? "$" + String.format("%,.0f", cuenta.getSaldo()) : "$0");
        saldoH2.addClassName("tarjeta_saldo");

        HorizontalLayout filaInfo = new HorizontalLayout();
        filaInfo.addClassName("tarjeta_fila_info");

        String num = (cuenta != null) ? cuenta.getNumeroCuenta() : "0000000000";
        String ultimosCuatro = num.substring(Math.max(0, num.length() - 4));
        
        filaInfo.add(
            crearItem("N° Cuenta", "**** " + ultimosCuatro),
            crearItem("Tipo", (cuenta != null) ? cuenta.getTipoCuenta() : "---"),
            crearItem("Válida", "12/28")
        );

        tarjeta.add(label, nombreP, saldoLabel, saldoH2, filaInfo);
        return tarjeta;
    }

    private Span crearChipConScroll(String texto, Component seccion) {
        Span chip = new Span(texto);
        chip.addClassName("hero_chip");
        chip.addClickListener(e -> seccion.getElement().callJsFunction("scrollIntoView", "{behavior: 'smooth', block: 'start'}"));
        return chip;
    }

    private Div crearItem(String label, String valor) {
        Div item = new Div();
        item.addClassName("tarjeta_item");
        Span l = new Span(label);
        l.addClassName("tarjeta_item_label");
        Span v = new Span(valor);
        v.addClassName("tarjeta_item_valor");
        item.add(l, v);
        return item;
    }
}