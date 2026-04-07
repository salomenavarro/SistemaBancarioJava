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

import java.util.List;
import java.util.stream.Collectors;

public class PagosSection extends VerticalLayout {

    // ── ATRIBUTOS ─────────────────────────────────────────────────────────────
    private Banco banco;
    private ComboBox<String> selectorCuenta;   // cuenta de donde sale el dinero
    private ComboBox<String> selectorServicio;
    private TextField campoReferencia;
    private TextField campoMonto;
    private VerticalLayout contenedorHistorial;

    // ── CONSTRUCTOR ───────────────────────────────────────────────────────────
    // Ahora recibe el banco para poder operar con cuentas reales
    public PagosSection(Banco banco) {
        this.banco = banco;

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

    // ── FORMULARIO ────────────────────────────────────────────────────────────
    private Div crearFormularioPago() {
        Div formulario = new Div();
        formulario.addClassName("pagos_formulario");

        H3 tituloForm = new H3("Nuevo pago");
        tituloForm.addClassName("form_titulo");

        // Selector de cuenta origen — se llena con las cuentas reales del cliente
        selectorCuenta = new ComboBox<>("Cuenta origen");
        selectorCuenta.addClassName("campo_full");
        selectorCuenta.setPlaceholder("Selecciona una cuenta");
        cargarCuentasDelCliente(); // llena el selector con las cuentas reales

        selectorServicio = new ComboBox<>("Servicio");
        selectorServicio.addClassName("campo_full");
        selectorServicio.setItems("Energía", "Agua", "Celular", "Internet", "TV Cable", "Arriendo");
        selectorServicio.setPlaceholder("Selecciona un servicio");

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

        formulario.add(tituloForm, selectorCuenta, selectorServicio,
                       campoReferencia, campoMonto, mensajeError, btnPagar);
        return formulario;
    }

    // ── HISTORIAL ─────────────────────────────────────────────────────────────
    private Div crearHistorial() {
        Div panel = new Div();
        panel.addClassName("pagos_historial");

        H3 titulo = new H3("Pagos realizados");
        titulo.addClassName("form_titulo");

        contenedorHistorial = new VerticalLayout();
        contenedorHistorial.setPadding(false);
        contenedorHistorial.setSpacing(false);

        panel.add(titulo, contenedorHistorial);
        return panel;
    }

    // ── LÓGICA DEL BOTÓN "CONFIRMAR PAGO" ────────────────────────────────────
    private void procesarPago(Span mensajeError) {
        mensajeError.setVisible(false);

        // Validaciones
        if (selectorCuenta.isEmpty()) {
            mostrarError(mensajeError, "Selecciona una cuenta de origen.");
            return;
        }
        if (selectorServicio.isEmpty()) {
            mostrarError(mensajeError, "Selecciona un servicio.");
            return;
        }
        if (campoReferencia.isEmpty()) {
            mostrarError(mensajeError, "Ingresa el número de referencia.");
            return;
        }
        if (campoMonto.isEmpty()) {
            mostrarError(mensajeError, "Ingresa el valor a pagar.");
            return;
        }

        // Valida que el monto sea un número
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

        // Busca la cuenta seleccionada entre las cuentas reales del cliente
        Cuenta cuentaOrigen = buscarCuentaSeleccionada(selectorCuenta.getValue());

        if (cuentaOrigen == null) {
            mostrarError(mensajeError, "No se encontró la cuenta seleccionada.");
            return;
        }

        // Verifica si la cuenta está bloqueada
        if (cuentaOrigen.estaBloqueada()) {
            mostrarError(mensajeError, "La cuenta seleccionada está bloqueada.");
            return;
        }

        // Le pide al banco que realice el pago
        boolean exitoso = banco.realizarPago(cuentaOrigen, monto);

        if (exitoso) {
            String servicio = selectorServicio.getValue();
            String montoFmt = "$" + String.format("%,.0f", monto);

            // Agrega al historial visual
            agregarPagoAlHistorial(servicio, "Hoy", montoFmt, true);

            // Actualiza el selector de cuentas con los nuevos saldos
            cargarCuentasDelCliente();

            // Limpia el formulario
            selectorServicio.clear();
            campoReferencia.clear();
            campoMonto.clear();

            Notification notif = new Notification("Pago de " + montoFmt + " realizado con éxito", 3000);
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.setPosition(Notification.Position.BOTTOM_CENTER);
            notif.open();
        } else {
            mostrarError(mensajeError, "Saldo insuficiente para realizar el pago.");
        }
    }

    // ── AUXILIAR: llena el selector con las cuentas reales del cliente ────────
    // Se llama al abrir la sección y después de cada pago para actualizar saldos
    private void cargarCuentasDelCliente() {
        if (banco.getClienteActivo() == null) return;

        List<String> opciones = banco.getClienteActivo().getCuentas()
            .stream()
            .map(c -> c.getResumen(true)) // "Ahorros 001 — María | Saldo: $4.250.000"
            .collect(Collectors.toList());

        selectorCuenta.setItems(opciones);

        if (!opciones.isEmpty()) {
            selectorCuenta.setValue(opciones.get(0));
        }
    }

    // ── AUXILIAR: busca el objeto Cuenta según el texto seleccionado ──────────
    private Cuenta buscarCuentaSeleccionada(String resumenSeleccionado) {
        if (banco.getClienteActivo() == null) return null;

        for (Cuenta c : banco.getClienteActivo().getCuentas()) {
            if (c.getResumen(true).equals(resumenSeleccionado)) {
                return c;
            }
        }
        return null;
    }

    // ── AUXILIAR: agrega una fila al historial ────────────────────────────────
    /**
     * @param nombre     Nombre del servicio pagado
     * @param fecha      Fecha del pago
     * @param monto      Monto formateado
     * @param completado true = punto verde, false = punto gris
     */
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
        info.add(new Span(nombre) {{ addClassName("historial_nombre"); }},
                 new Span(fecha)  {{ addClassName("historial_fecha"); }});

        Span spanMonto = new Span(monto);
        spanMonto.addClassName("historial_monto");

        fila.add(punto, info, spanMonto);
        fila.expand(info);

        contenedorHistorial.addComponentAsFirst(fila);
    }

    // ── AUXILIAR: muestra un error en el formulario ───────────────────────────
    private void mostrarError(Span span, String texto) {
        span.setText(texto);
        span.setVisible(true);
    }
}