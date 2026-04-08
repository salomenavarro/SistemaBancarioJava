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

    private Banco banco = Banco.getInstancia();
    private ComboBox<Cuenta> selectorOrigen;
    private ComboBox<Cuenta> selectorDestino;
    private TextField campoDestinatario;
    private NumberField campoMonto;
    private Div panelConfirmacion;
    private VerticalLayout listaRecientes;
    private Runnable alActualizar;

    public TransferenciasSection() {
        // CAMBIO: Se añade la clase principal para el CSS
        addClassName("transferencias_section");
        setWidth("100%");
        setPadding(false);
        setSpacing(false);

        // CAMBIO: Clases para el encabezado
        Div encabezado = new Div();
        encabezado.addClassName("section_encabezado");
        
        Span etiqueta = new Span("Operaciones");
        etiqueta.addClassName("section_etiqueta");
        
        H2 titulo = new H2("Transferencias");
        titulo.addClassName("section_titulo");
        
        encabezado.add(etiqueta, titulo);

        HorizontalLayout columnas = new HorizontalLayout();
        columnas.addClassName("transferencias_columnas"); // CAMBIO: Clase para layout
        columnas.setWidth("100%");
        columnas.add(crearFormulario(), crearPanelRecientes());

        add(encabezado, columnas);
    }

    public void setAlActualizar(Runnable alActualizar) {
        this.alActualizar = alActualizar;
    }

    private Div crearFormulario() {
        VerticalLayout form = new VerticalLayout();
        form.addClassName("transferencias_formulario"); // CAMBIO: Clase del CSS

        selectorOrigen = new ComboBox<>("Cuenta de origen");
        selectorOrigen.setItemLabelGenerator(Cuenta::getResumen);
        selectorOrigen.addClassName("campo_full");

        campoDestinatario = new TextField("Nombre del destinatario");
        campoDestinatario.addClassName("campo_full");

        selectorDestino = new ComboBox<>("Cuenta de destino");
        selectorDestino.setItemLabelGenerator(Cuenta::getResumen);
        selectorDestino.addClassName("campo_full");

        campoMonto = new NumberField("Monto a transferir");
        campoMonto.setPrefixComponent(new Span("$"));
        campoMonto.addClassName("campo_full");

        Button btnTransferir = new Button("Transferir ahora", e -> prepararConfirmacion());
        btnTransferir.addClassName("btn_primario_verde"); // CAMBIO: Clase del CSS
        btnTransferir.setWidthFull();

        cargarCuentas();

        form.add(selectorOrigen, campoDestinatario, selectorDestino, campoMonto, btnTransferir);
        return new Div(form, crearPanelConfirmacion());
    }

    private void cargarCuentas() {
        if (banco.getClienteActivo() != null) {
            List<Cuenta> cuentasCliente = banco.getClienteActivo().getCuentas();
            selectorOrigen.setItems(cuentasCliente);
            selectorDestino.setItems(banco.getClientes().stream()
                    .flatMap(c -> c.getCuentas().stream())
                    .toList());
        }
    }

    private Div crearPanelConfirmacion() {
        panelConfirmacion = new Div();
        panelConfirmacion.addClassName("transferencias_confirmacion"); // CAMBIO: Clase del CSS
        panelConfirmacion.setVisible(false);

        H3 titulo = new H3("¿Confirmar transferencia?");
        titulo.addClassName("confirmacion_titulo");

        Button btnSi = new Button("Confirmar", e -> ejecutarTransferencia());
        btnSi.addClassName("btn_primario_verde");

        Button btnNo = new Button("Cancelar", e -> panelConfirmacion.setVisible(false));
        btnNo.addClassName("btn_cancelar");

        panelConfirmacion.add(titulo, new HorizontalLayout(btnSi, btnNo));
        return panelConfirmacion;
    }

    private void prepararConfirmacion() {
        if (selectorOrigen.isEmpty() || selectorDestino.isEmpty() || campoMonto.getValue() == null) {
            Notification.show("Complete todos los campos");
            return;
        }
        if (campoMonto.getValue() <= 0) {
            Notification.show("Monto inválido");
            return;
        }
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

        if (banco.realizarTransferencia(origen, destino, monto)) {
            agregarItemReciente(campoDestinatario.getValue(), origen.getTipoCuenta(), "-$" + monto, "Hoy", false);
            Notification.show("Transferencia realizada").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            if (alActualizar != null) alActualizar.run();
            limpiarFormulario();
            cargarCuentas();
        } else {
            Notification.show("Error en la transferencia").addThemeVariants(NotificationVariant.LUMO_ERROR);
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
        listaRecientes.setPadding(false);
        
        Div contenedor = new Div(new H3("Transferencias recientes"), listaRecientes);
        contenedor.addClassName("transferencias_recientes"); // CAMBIO: Clase del CSS
        return contenedor;
    }

    private void agregarItemReciente(String nombre, String bancoNombre, String monto, String fecha, boolean entrada) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassName("transferencia_item"); // CAMBIO: Estilo de fila

        Div info = new Div();
        Span n = new Span(nombre);
        n.addClassName("transferencia_nombre");
        Span b = new Span(bancoNombre);
        b.addClassName("transferencia_banco");
        info.add(n, b);

        Span m = new Span(monto);
        m.addClassName(entrada ? "monto_entrada" : "monto_salida"); // CAMBIO: Color según tipo

        fila.add(info, m);
        listaRecientes.addComponentAsFirst(fila);
    }
}