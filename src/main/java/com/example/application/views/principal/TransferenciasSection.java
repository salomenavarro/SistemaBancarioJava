package com.example.application.views.principal;

import com.example.application.modelo.Banco;
import com.example.application.modelo.Cuenta;
import com.example.application.modelo.Cliente;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.List;

public class TransferenciasSection extends VerticalLayout {

    // Instancia única compartida en toda la app
    private Banco banco = Banco.getInstancia();

    private ComboBox<Cuenta> selectorOrigen;
    private ComboBox<Cuenta> selectorDestino;

    private TextField campoDestinatario;
    private NumberField campoMonto;

    private Div panelConfirmacion;
    private VerticalLayout listaRecientes;

    public TransferenciasSection() {
        setWidth("100%");
        setPadding(false);
        setSpacing(false);

        Div encabezado = new Div();
        encabezado.add(new Span("Operaciones"), new H2("Transferencias"));

        HorizontalLayout columnas = new HorizontalLayout();
        columnas.setWidth("100%");
        columnas.add(crearFormulario(), crearPanelRecientes());

        add(encabezado, columnas);
    }

    private Div crearFormulario() {
        Div formulario = new Div();

        selectorOrigen = new ComboBox<>("Cuenta origen");
        selectorDestino = new ComboBox<>("Cuenta destino");

        // Cuando cambia origen, se actualiza destino para excluirla
        selectorOrigen.addValueChangeListener(e -> actualizarDestino());

        cargarCuentas();

        campoDestinatario = new TextField("Nombre del destinatario");
        campoMonto = new NumberField("Monto");

        panelConfirmacion = crearPanelConfirmacion();

        Button btnTransferir = new Button("Transferir ahora");
        btnTransferir.addClickListener(e -> mostrarConfirmacion());

        formulario.add(
                new H3("Nueva transferencia"),
                selectorOrigen,
                selectorDestino,
                campoDestinatario,
                campoMonto,
                panelConfirmacion,
                btnTransferir
        );

        return formulario;
    }

    private void cargarCuentas() {
        if (!banco.haySesionActiva()) {
            Notification.show("Debes iniciar sesión");
            return;
        }

        Cliente cliente = banco.getClienteActivo();
        List<Cuenta> cuentas = cliente.getCuentas();

        selectorOrigen.setItems(cuentas);
        selectorDestino.setItems(cuentas);

        // Muestra información útil para identificar cuentas
        selectorOrigen.setItemLabelGenerator(c ->
                c.getTipoCuenta() + " - $" + c.getSaldo() + " (" + c.getNumeroCuenta() + ")"
        );

        selectorDestino.setItemLabelGenerator(c ->
                c.getTipoCuenta() + " - $" + c.getSaldo() + " (" + c.getNumeroCuenta() + ")"
        );
    }

    // Filtra cuentas destino excluyendo la cuenta origen seleccionada
    private void actualizarDestino() {

        if (!banco.haySesionActiva()) return;

        List<Cuenta> cuentas = banco.getClienteActivo().getCuentas();
        Cuenta origen = selectorOrigen.getValue();

        if (origen == null) {
            selectorDestino.setItems(cuentas);
            return;
        }

        selectorDestino.setItems(
                cuentas.stream()
                        .filter(c -> c != origen)
                        .toList()
        );
    }

    private Div crearPanelConfirmacion() {
        Div panel = new Div();
        panel.setVisible(false);

        Button confirmar = new Button("Confirmar");
        confirmar.addClickListener(e -> ejecutarTransferencia());

        panel.add(new Paragraph("¿Confirmas la transferencia?"), confirmar);
        return panel;
    }

    private void mostrarConfirmacion() {

        if (!banco.haySesionActiva()) {
            Notification.show("Debes iniciar sesión");
            return;
        }

        if (selectorOrigen.isEmpty() || selectorDestino.isEmpty()) {
            Notification.show("Selecciona cuentas");
            return;
        }

        if (campoDestinatario.isEmpty()) {
            Notification.show("Ingresa destinatario");
            return;
        }

        if (campoMonto.isEmpty() || campoMonto.getValue() == null || campoMonto.getValue() <= 0) {
            Notification.show("Monto inválido");
            return;
        }

        // Seguridad lógica adicional
        if (selectorOrigen.getValue() == selectorDestino.getValue()) {
            Notification.show("No puedes transferir a la misma cuenta");
            return;
        }

        panelConfirmacion.setVisible(true);
    }

    private void ejecutarTransferencia() {

        Cuenta origen = selectorOrigen.getValue();
        Cuenta destino = selectorDestino.getValue();
        double monto = campoMonto.getValue();

        // Delega operación al modelo
        boolean ok = banco.realizarTransferencia(origen, destino, monto);

        if (ok) {
            agregarItemReciente(
                    campoDestinatario.getValue(),
                    origen.getTipoCuenta(),
                    "-$" + monto,
                    "Hoy",
                    false
            );

            Notification.show("Transferencia realizada")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            limpiarFormulario();

            // Refresca datos para mostrar saldos actualizados
            cargarCuentas();

        } else {
            Notification.show("Error en la transferencia")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        panelConfirmacion.setVisible(false);
    }

    private void limpiarFormulario() {
        campoDestinatario.clear();
        campoMonto.clear();
        selectorOrigen.clear();
        selectorDestino.clear();
    }

    private Div crearPanelRecientes() {
        listaRecientes = new VerticalLayout();
        return new Div(new H3("Transferencias recientes"), listaRecientes);
    }

    private void agregarItemReciente(String nombre, String banco,
                                     String monto, String fecha, boolean entrada) {

        // Solo representación visual
        HorizontalLayout fila = new HorizontalLayout(
                new Span(nombre),
                new Span(monto)
        );

        listaRecientes.addComponentAsFirst(fila);
    }
}