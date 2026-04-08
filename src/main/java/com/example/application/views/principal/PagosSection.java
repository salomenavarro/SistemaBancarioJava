package com.example.application.views.principal;

import com.example.application.modelo.Banco;
import com.example.application.modelo.Cuenta;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class PagosSection extends VerticalLayout {

    private Banco banco = Banco.getInstancia();

    private ComboBox<String> selectorCuenta;
    private ComboBox<String> selectorTipoPago;
    private ComboBox<String> selectorServicio;
    private TextField campoReferencia;
    private TextField campoMonto;
    private VerticalLayout contenedorHistorial;

    // 🔹 NUEVO: para actualizar Hero
    private Runnable alActualizar;

    public void setAlActualizar(Runnable alActualizar) {
        this.alActualizar = alActualizar;
    }

    public PagosSection() {
        setWidth("100%");
        setPadding(false);
        setSpacing(false);
        addClassName("pagos_section");

        Div encabezado = new Div();
        encabezado.addClassName("section_encabezado");

        Span etiqueta = new Span("Servicios");
        etiqueta.addClassName("section_etiqueta");

        H2 titulo = new H2("Pagos");
        titulo.addClassName("section_titulo");

        encabezado.add(etiqueta, titulo);

        HorizontalLayout columnas = new HorizontalLayout();
        columnas.addClassName("pagos_columnas");
        columnas.setWidth("100%");
        columnas.add(crearFormularioPago(), crearHistorial());

        add(encabezado, columnas);
    }

    public void refrescarCuentas() {
        cargarCuentasDelCliente();
    }

    private Div crearFormularioPago() {
        Div formulario = new Div();
        formulario.addClassName("pagos_formulario");

        H3 tituloForm = new H3("Nuevo pago");
        tituloForm.addClassName("form_titulo");

        selectorTipoPago = new ComboBox<>("¿Qué vas a pagar?");
        selectorTipoPago.addClassName("campo_full");
        selectorTipoPago.setItems("Servicio", "Cuota de crédito");
        selectorTipoPago.setPlaceholder("Selecciona una opción");

        selectorServicio = new ComboBox<>("Servicio");
        selectorServicio.addClassName("campo_full");
        selectorServicio.setItems("Energía", "Agua", "Celular", "Internet", "TV Cable", "Arriendo");
        selectorServicio.setPlaceholder("Selecciona un servicio");
        selectorServicio.setVisible(false);

        selectorTipoPago.addValueChangeListener(e -> {
            if ("Servicio".equals(e.getValue())) {
                selectorServicio.setVisible(true);
                campoReferencia.setLabel("N° de referencia");
            } else if ("Cuota de crédito".equals(e.getValue())) {
                selectorServicio.setVisible(false);
                selectorServicio.clear();
                campoReferencia.setLabel("Número del crédito");
            } else {
                selectorServicio.setVisible(false);
            }
        });

        selectorCuenta = new ComboBox<>("Cuenta origen");
        selectorCuenta.addClassName("campo_full");
        selectorCuenta.setPlaceholder("Selecciona una cuenta");
        cargarCuentasDelCliente();

        campoReferencia = new TextField("N° de referencia");
        campoReferencia.addClassName("campo_full");
        campoReferencia.setPlaceholder("Número de factura o contrato");

        campoMonto = new TextField("Valor a pagar");
        campoMonto.addClassName("campo_full");
        campoMonto.setPlaceholder("Ej: 85000");
        campoMonto.setPrefixComponent(new Span("$"));

        Span mensajeError = new Span();
        mensajeError.addClassName("login_error");
        mensajeError.setVisible(false);

        Button btnPagar = new Button("Confirmar pago");
        btnPagar.addClassName("btn_primario_verde");
        btnPagar.addClickListener(e -> procesarPago(mensajeError));

        formulario.add(tituloForm, selectorTipoPago, selectorServicio,
                       selectorCuenta, campoReferencia, campoMonto,
                       mensajeError, btnPagar);
        return formulario;
    }

    private Div crearHistorial() {
        Div panel = new Div();
        panel.addClassName("pagos_historial");

        H3 titulo = new H3("Pagos realizados");
        titulo.addClassName("form_titulo");

        contenedorHistorial = new VerticalLayout();
        contenedorHistorial.setPadding(false);
        contenedorHistorial.setSpacing(false);

        // 🔹 ELIMINADO: pagos de ejemplo

        panel.add(titulo, contenedorHistorial);
        return panel;
    }

    private void procesarPago(Span mensajeError) {
        mensajeError.setVisible(false);

        if (selectorTipoPago.isEmpty()) {
            mostrarError(mensajeError, "Selecciona qué vas a pagar.");
            return;
        }
        if ("Servicio".equals(selectorTipoPago.getValue()) && selectorServicio.isEmpty()) {
            mostrarError(mensajeError, "Selecciona un servicio.");
            return;
        }
        if (selectorCuenta.isEmpty()) {
            mostrarError(mensajeError, "Selecciona una cuenta de origen.");
            return;
        }
        if (campoMonto.isEmpty()) {
            mostrarError(mensajeError, "Ingresa el valor a pagar.");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(campoMonto.getValue().trim());
        } catch (NumberFormatException ex) {
            mostrarError(mensajeError, "El valor debe ser un número (ej: 85000).");
            return;
        }

        if (monto <= 0) {
            mostrarError(mensajeError, "El valor debe ser mayor a $0.");
            return;
        }

        Cuenta cuentaOrigen = buscarCuentaSeleccionada(selectorCuenta.getValue());

        if (cuentaOrigen == null) {
            mostrarError(mensajeError, "No se encontró la cuenta seleccionada.");
            return;
        }

        boolean exitoso = banco.realizarPago(cuentaOrigen, monto);

        if (exitoso) {
            String montoFmt = "$" + String.format("%,.0f", monto);
            boolean esCreditoPago = "Cuota de crédito".equals(selectorTipoPago.getValue());

            String nombrePago = esCreditoPago
                ? "Cuota crédito #" + campoReferencia.getValue()
                : selectorServicio.getValue();

            agregarPagoAlHistorial(nombrePago, "Hoy", montoFmt, true);

            // 🔹 NUEVO: actualizar Hero
            if (alActualizar != null) {
                alActualizar.run();
            }

            cargarCuentasDelCliente();

            if (esCreditoPago) {
                Notification notifCredito = new Notification(
                    "Pago registrado. El banco procesará y actualizará " +
                    "el estado de tu crédito en 1-3 días hábiles.", 5000);
                notifCredito.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                notifCredito.setPosition(Notification.Position.TOP_CENTER);
                notifCredito.open();
            } else {
                Notification notif = new Notification(
                    "Pago de " + montoFmt + " realizado con éxito", 3000);
                notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notif.setPosition(Notification.Position.BOTTOM_CENTER);
                notif.open();
            }

            selectorTipoPago.clear();
            selectorServicio.clear();
            selectorServicio.setVisible(false);
            campoReferencia.clear();
            campoMonto.clear();

        } else {
            mostrarError(mensajeError, "Saldo insuficiente para realizar el pago.");
        }
    }

    private void cargarCuentasDelCliente() {
        if (banco.getClienteActivo() == null) return;

        List<String> opciones = new ArrayList<>();
        for (Cuenta c : banco.getClienteActivo().getCuentas()) {
            // 🔹 CAMBIO: sin saldo
            opciones.add(c.getResumen(false));
        }

        selectorCuenta.setItems(opciones);

        if (!opciones.isEmpty()) {
            selectorCuenta.setValue(opciones.get(0));
        }
    }

    private Cuenta buscarCuentaSeleccionada(String resumenSeleccionado) {
        if (banco.getClienteActivo() == null) return null;

        for (Cuenta c : banco.getClienteActivo().getCuentas()) {
            if (c.getResumen(false).equals(resumenSeleccionado)) {
                return c;
            }
        }
        return null;
    }

    private void agregarPagoAlHistorial(String nombre, String fecha,
                                        String monto, boolean completado) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassName("historial_fila");
        fila.setAlignItems(Alignment.CENTER);

        Span punto = new Span();
        punto.addClassName(completado ? "punto_verde" : "punto_gris");

        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        Span spanNombre = new Span(nombre);
        spanNombre.addClassName("historial_nombre");

        Span spanFecha = new Span(fecha);
        spanFecha.addClassName("historial_fecha");

        info.add(spanNombre, spanFecha);

        Span spanMonto = new Span(monto);
        spanMonto.addClassName("historial_monto");

        fila.add(punto, info, spanMonto);
        fila.expand(info);

        contenedorHistorial.addComponentAsFirst(fila);
    }

    private void mostrarError(Span span, String texto) {
        span.setText(texto);
        span.setVisible(true);
    }
}