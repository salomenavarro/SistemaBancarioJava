package com.example.application.views.principal;

// ── IMPORTS ──────────────────────────────────────────────────────────────────
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;

// ── CLASE ────────────────────────────────────────────────────────────────────
public class TransferenciasSection extends VerticalLayout {

    // ── ATRIBUTOS DE INSTANCIA ────────────────────────────────────────────────
    // "private" = encapsulamiento, solo esta clase los usa.
    // Se declaran aquí para que procesarTransferencia() pueda accederlos.
    private ComboBox<String> selectorOrigen;
    private ComboBox<String> selectorBanco;
    private TextField campoCuentaDestino;
    private TextField campoDestinatario;
    private NumberField campoMonto;
    private TextField campoDescripcion;

    // Panel de confirmación — se muestra/oculta según el flujo
    private Div panelConfirmacion;

    // Lista de transferencias recientes — se actualiza al confirmar
    private VerticalLayout listaRecientes;

    // ── CONSTRUCTOR ──────────────────────────────────────────────────────────
    public TransferenciasSection() {
        setWidth("100%");
        setPadding(false);
        setSpacing(false);
        addClassName("transferencias_section");

        // ── ENCABEZADO ────────────────────────────────────────────────────────
        Div encabezado = new Div();
        encabezado.addClassName("section_encabezado");

        Span etiqueta = new Span("Operaciones");
        etiqueta.addClassName("section_etiqueta");

        H2 titulo = new H2("Transferencias");
        titulo.addClassName("section_titulo");

        encabezado.add(etiqueta, titulo);

        // ── DOS COLUMNAS: formulario | recientes ──────────────────────────────
        HorizontalLayout columnas = new HorizontalLayout();
        columnas.addClassName("transferencias_columnas");
        columnas.setWidth("100%");
        columnas.setAlignItems(Alignment.START);

        columnas.add(crearFormulario(), crearPanelRecientes());

        add(encabezado, columnas);
    }

    // ── MÉTODO: construye el formulario de transferencia ──────────────────────
    private Div crearFormulario() {
        Div formulario = new Div();
        formulario.addClassName("transferencias_formulario");

        H3 tituloForm = new H3("Nueva transferencia");
        tituloForm.addClassName("form_titulo");

        // Cuenta origen
        selectorOrigen = new ComboBox<>("Cuenta origen");
        selectorOrigen.addClassName("campo_full");
        selectorOrigen.setItems(
            "Ahorros **** 4821 — $4.250.000",
            "Corriente **** 1234 — $1.800.000"
        );
        selectorOrigen.setValue("Ahorros **** 4821 — $4.250.000");

        // Fila: banco + cuenta destino
        HorizontalLayout filaBancoYCuenta = new HorizontalLayout();
        filaBancoYCuenta.setWidth("100%");
        filaBancoYCuenta.addClassName("transferencias_fila");

        selectorBanco = new ComboBox<>("Banco destino");
        selectorBanco.setItems("DESS", "Bancolombia", "Davivienda", "Nequi", "Daviplata");
        selectorBanco.setValue("DESS");
        selectorBanco.addClassName("campo_full");

        campoCuentaDestino = new TextField("N° cuenta destino");
        campoCuentaDestino.setPlaceholder("Ej: 1234567890");
        campoCuentaDestino.addClassName("campo_full");

        filaBancoYCuenta.add(selectorBanco, campoCuentaDestino);
        filaBancoYCuenta.expand(campoCuentaDestino);

        // Nombre del destinatario
        campoDestinatario = new TextField("Nombre del destinatario");
        campoDestinatario.setPlaceholder("Nombre completo");
        campoDestinatario.addClassName("campo_full");

        // Monto
        campoMonto = new NumberField("Monto");
        campoMonto.setPlaceholder("$0");
        campoMonto.setMin(0);
        campoMonto.addClassName("campo_full");
        campoMonto.addClassName("campo_monto");

        // Descripción opcional
        campoDescripcion = new TextField("Descripción (opcional)");
        campoDescripcion.setPlaceholder("Ej: Arriendo junio");
        campoDescripcion.addClassName("campo_full");

        // Panel de confirmación — oculto hasta que el usuario haga click
        panelConfirmacion = crearPanelConfirmacion();

        // Mensaje de error
        Span mensajeError = new Span();
        mensajeError.addClassName("transferencias_error");
        mensajeError.setVisible(false);

        // Botón principal
        Button btnTransferir = new Button("Transferir ahora");
        btnTransferir.addClassName("btn_primario_verde");

        // Al hacer click → valida y muestra la confirmación
        btnTransferir.addClickListener(e ->
            mostrarConfirmacion(mensajeError, btnTransferir)
        );

        formulario.add(
            tituloForm,
            selectorOrigen,
            filaBancoYCuenta,
            campoDestinatario,
            campoMonto,
            campoDescripcion,
            panelConfirmacion,
            mensajeError,
            btnTransferir
        );

        return formulario;
    }

    // ── MÉTODO: construye el panel de confirmación ────────────────────────────
    // Se construye vacío y se llena con datos reales al momento de confirmar
    private Div crearPanelConfirmacion() {
        Div panel = new Div();
        panel.addClassName("transferencias_confirmacion");
        panel.setVisible(false); // oculto por defecto

        Paragraph tituloPan = new Paragraph("¿Confirmas esta transferencia?");
        tituloPan.addClassName("confirmacion_titulo");

        // Filas de resumen — se actualizan en mostrarConfirmacion()
        Div filaDestinatario = crearFilaResumen("Destinatario", "—", "conf_nombre");
        Div filaBanco        = crearFilaResumen("Banco",        "—", "conf_banco");
        Div filaCuenta       = crearFilaResumen("Cuenta",       "—", "conf_cuenta");
        Div filaMonto        = crearFilaResumen("Monto",        "—", "conf_monto");

        // Botones de acción dentro del panel
        HorizontalLayout botones = new HorizontalLayout();
        botones.addClassName("confirmacion_botones");

        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.addClassName("btn_primario_verde");
        btnConfirmar.addClickListener(e -> ejecutarTransferencia());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClassName("btn_cancelar");
        btnCancelar.addClickListener(e -> {
            panel.setVisible(false);
        });

        botones.add(btnConfirmar, btnCancelar);

        panel.add(tituloPan, filaDestinatario, filaBanco, filaCuenta, filaMonto, botones);
        return panel;
    }

    // ── MÉTODO: construye el panel de transferencias recientes ────────────────
    private Div crearPanelRecientes() {
        Div panel = new Div();
        panel.addClassName("transferencias_recientes");

        H3 titulo = new H3("Transferencias recientes");
        titulo.addClassName("form_titulo");

        listaRecientes = new VerticalLayout();
        listaRecientes.setPadding(false);
        listaRecientes.setSpacing(false);

        // Datos de ejemplo — se reemplazarán con lógica real
        agregarItemReciente("Juan Rodríguez", "Bancolombia", "-$350.000", "26 mar 2026", false);
        agregarItemReciente("Ana Pérez",      "DESS",        "+$800.000", "24 mar 2026", true);
        agregarItemReciente("Carlos Martínez","Davivienda",  "-$120.000", "20 mar 2026", false);
        agregarItemReciente("Laura Torres",   "Nequi",       "-$250.000", "18 mar 2026", false);

        panel.add(titulo, listaRecientes);
        return panel;
    }

    // ── MÉTODO: lógica del botón "Transferir ahora" ───────────────────────────
    // Valida los campos y si todo está bien muestra el panel de confirmación
    private void mostrarConfirmacion(Span mensajeError, Button btnTransferir) {
        mensajeError.setVisible(false);

        // Validaciones — si algo falla, mostramos el error y detenemos
        if (campoDestinatario.isEmpty()) {
            mensajeError.setText("Ingresa el nombre del destinatario.");
            mensajeError.setVisible(true);
            return;
        }
        if (campoCuentaDestino.isEmpty()) {
            mensajeError.setText("Ingresa el número de cuenta destino.");
            mensajeError.setVisible(true);
            return;
        }
        if (campoMonto.isEmpty() || campoMonto.getValue() <= 0) {
            mensajeError.setText("El monto debe ser mayor a $0.");
            mensajeError.setVisible(true);
            return;
        }

        // Llenamos el panel de confirmación con los datos ingresados
        actualizarTextoConfirmacion("conf_nombre", campoDestinatario.getValue());
        actualizarTextoConfirmacion("conf_banco",  selectorBanco.getValue());
        actualizarTextoConfirmacion("conf_cuenta", campoCuentaDestino.getValue());
        actualizarTextoConfirmacion("conf_monto",
            "$" + String.format("%,.0f", campoMonto.getValue()));

        panelConfirmacion.setVisible(true);
        btnTransferir.setEnabled(false);
    }

    // ── MÉTODO: lógica del botón "Confirmar" ──────────────────────────────────
    // Registra la transferencia y actualiza la lista de recientes
    private void ejecutarTransferencia() {
        String destinatario = campoDestinatario.getValue();
        String banco        = selectorBanco.getValue();
        String montoFmt     = "$" + String.format("%,.0f", campoMonto.getValue());

        // Agrega la transferencia al inicio de la lista de recientes
        agregarItemReciente(destinatario, banco, "-" + montoFmt, "Hoy", false);

        // Oculta la confirmación y limpia los campos
        panelConfirmacion.setVisible(false);
        limpiarFormulario();

        // Mensaje de éxito
        Notification notif = new Notification(
            "Transferencia de " + montoFmt + " realizada con éxito", 3000
        );
        notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notif.setPosition(Notification.Position.BOTTOM_CENTER);
        notif.open();
    }

    // ── MÉTODO AUXILIAR: limpia todos los campos del formulario ───────────────
    private void limpiarFormulario() {
        campoCuentaDestino.clear();
        campoDestinatario.clear();
        campoMonto.clear();
        campoDescripcion.clear();
    }

    // ── MÉTODO AUXILIAR: agrega un item a la lista de recientes ──────────────
    /**
     * Crea y agrega una fila a la lista de transferencias recientes.
     * @param nombre    Nombre del destinatario/remitente
     * @param banco     Banco de la transacción
     * @param monto     Monto formateado (ej: "-$350.000")
     * @param fecha     Fecha de la transacción
     * @param entrada   true = monto en verde (recibido), false = rojo (enviado)
     */
    private void agregarItemReciente(String nombre, String banco,
                                     String monto, String fecha, boolean entrada) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassName("transferencia_item");
        fila.setAlignItems(Alignment.CENTER);

        // Avatar con iniciales
        String iniciales = generarIniciales(nombre);
        Span avatar = new Span(iniciales);
        avatar.addClassName("transferencia_avatar");

        // Info: nombre y banco
        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        Span spanNombre = new Span(nombre);
        spanNombre.addClassName("transferencia_nombre");

        Span spanBanco = new Span(banco);
        spanBanco.addClassName("transferencia_banco");

        info.add(spanNombre, spanBanco);

        // Monto y fecha
        VerticalLayout montoCol = new VerticalLayout();
        montoCol.setPadding(false);
        montoCol.setSpacing(false);
        montoCol.setAlignItems(Alignment.END);

        Span spanMonto = new Span(monto);
        spanMonto.addClassName(entrada ? "monto_entrada" : "monto_salida");

        Span spanFecha = new Span(fecha);
        spanFecha.addClassName("transferencia_fecha");

        montoCol.add(spanMonto, spanFecha);

        fila.add(avatar, info, montoCol);
        fila.expand(info);

        // Inserta al inicio de la lista (más reciente arriba)
        listaRecientes.addComponentAsFirst(fila);
    }

    // ── MÉTODO AUXILIAR: genera iniciales del nombre ──────────────────────────
    /**
     * Extrae las primeras letras de un nombre completo.
     * Ej: "Juan Rodríguez" → "JR"
     * @param nombre Nombre completo
     * @return Iniciales en mayúscula (máximo 2 caracteres)
     */
    private String generarIniciales(String nombre) {
        String[] partes = nombre.trim().split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if (!partes[i].isEmpty()) {
                iniciales.append(partes[i].charAt(0));
            }
        }
        return iniciales.toString().toUpperCase();
    }

    // ── MÉTODO AUXILIAR: crea una fila de resumen para la confirmación ────────
    /**
     * Crea una fila label-valor para el panel de confirmación.
     * @param label      Etiqueta (ej: "Banco")
     * @param valor      Valor inicial (se actualiza después)
     * @param idValor    ID del Span del valor para poder actualizarlo
     * @return Div con la fila armada
     */
    private Div crearFilaResumen(String label, String valor, String idValor) {
        Div fila = new Div();
        fila.addClassName("confirmacion_fila");

        Span spanLabel = new Span(label);
        spanLabel.addClassName("confirmacion_label");

        Span spanValor = new Span(valor);
        spanValor.setId(idValor);
        spanValor.addClassName("confirmacion_valor");

        fila.add(spanLabel, spanValor);
        return fila;
    }

    // ── MÉTODO AUXILIAR: actualiza el texto de un Span por ID ────────────────
    /**
     * Busca un componente por ID dentro del panel de confirmación
     * y actualiza su texto.
     * @param id    ID del Span a actualizar
     * @param texto Nuevo texto
     */
    private void actualizarTextoConfirmacion(String id, String texto) {
        panelConfirmacion.getChildren()
            .filter(c -> c.getId().map(cId -> cId.equals(id)).orElse(false))
            .findFirst()
            .ifPresent(c -> ((Span) c).setText(texto));

        // Busca dentro de los Div hijos (filas de resumen)
        panelConfirmacion.getChildren()
            .filter(c -> c instanceof Div)
            .forEach(div -> ((Div) div).getChildren()
                .filter(c -> c.getId().map(cId -> cId.equals(id)).orElse(false))
                .findFirst()
                .ifPresent(c -> ((Span) c).setText(texto)));
    }
}