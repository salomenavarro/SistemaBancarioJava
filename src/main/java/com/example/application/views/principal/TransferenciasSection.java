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

/**
 * Seccion encargada de gestionar la transferencia de fondos entre cuentas.
 * Utiliza componentes de Vaadin para la interfaz y se comunica con el Singleton Banco.
 */
public class TransferenciasSection extends VerticalLayout {

    // Acceso a la instancia unica de la logica de negocio
    private Banco banco = Banco.getInstancia();

    // Componentes de entrada de datos
    private ComboBox<Cuenta> selectorOrigen;
    private ComboBox<Cuenta> selectorDestino;
    private TextField campoDestinatario;
    private NumberField campoMonto;
    
    // Paneles de estado y realimentacion
    private Div panelConfirmacion;
    private VerticalLayout listaRecientes;
    
    // Interfaz funcional para notificar cambios a otras secciones (ej. actualizar saldos en el Header)
    private Runnable alActualizar;

    public TransferenciasSection() {
        addClassName("transferencias_section");
        setWidth("100%");
        setPadding(false);
        setSpacing(false);

        // Estructura de encabezado con clases CSS personalizadas
        Div encabezado = new Div();
        encabezado.addClassName("section_encabezado");
        
        Span etiqueta = new Span("Operaciones");
        etiqueta.addClassName("section_etiqueta");
        
        H2 titulo = new H2("Transferencias");
        titulo.addClassName("section_titulo");
        
        encabezado.add(etiqueta, titulo);

        // Organizacion en columnas para separar formulario de historial
        HorizontalLayout columnas = new HorizontalLayout();
        columnas.addClassName("transferencias_columnas");
        columnas.setWidth("100%");
        columnas.add(crearFormulario(), crearPanelRecientes());

        add(encabezado, columnas);
    }

    /**
     * Permite registrar una accion que se ejecutara tras una transferencia exitosa.
     */
    public void setAlActualizar(Runnable alActualizar) {
        this.alActualizar = alActualizar;
    }

    private Div crearFormulario() {
        VerticalLayout form = new VerticalLayout();
        form.addClassName("transferencias_formulario");

        // Configuracion de selectores de cuenta
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

        // Boton de accion principal que dispara la fase de confirmacion
        Button btnTransferir = new Button("Transferir ahora", e -> prepararConfirmacion());
        btnTransferir.addClassName("btn_primario_verde");
        btnTransferir.setWidthFull();

        cargarCuentas();

        form.add(selectorOrigen, campoDestinatario, selectorDestino, campoMonto, btnTransferir);
        return new Div(form, crearPanelConfirmacion());
    }

    /**
     * Poblado de datos: origen (cuentas del usuario actual) y destino (todas las cuentas del sistema).
     */
    private void cargarCuentas() {
        if (banco.getClienteActivo() != null) {
            List<Cuenta> cuentasCliente = banco.getClienteActivo().getCuentas();
            selectorOrigen.setItems(cuentasCliente);
            
            // Se utiliza flatMap para obtener todas las cuentas de todos los clientes registrados
            selectorDestino.setItems(banco.getClientes().stream()
                    .flatMap(c -> c.getCuentas().stream())
                    .toList());
        }
    }

    /**
     * Crea un panel oculto inicialmente para solicitar confirmacion final al usuario.
     */
    private Div crearPanelConfirmacion() {
        panelConfirmacion = new Div();
        panelConfirmacion.addClassName("transferencias_confirmacion");
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

    /**
     * Validaciones previas antes de mostrar el panel de confirmacion.
     */
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

    /**
     * Ejecuta la transaccion a traves del Banco y gestiona la respuesta.
     */
    private void ejecutarTransferencia() {
        Cuenta origen = selectorOrigen.getValue();
        Cuenta destino = selectorDestino.getValue();
        double monto = campoMonto.getValue();

        // Llamada al metodo de negocio. Retorna true si los saldos y cuentas son validos.
        if (banco.realizarTransferencia(origen, destino, monto)) {
            // Actualizacion de la interfaz tras exito
            agregarItemReciente(campoDestinatario.getValue(), origen.getTipoCuenta(), "-$" + monto, "Hoy", false);
            Notification.show("Transferencia realizada").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            // Ejecuta el refresco de componentes externos si el callback esta definido
            if (alActualizar != null) alActualizar.run();
            
            limpiarFormulario();
            cargarCuentas(); // Recarga cuentas para mostrar saldos actualizados en los selectores
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
        contenedor.addClassName("transferencias_recientes");
        return contenedor;
    }

    /**
     * Agrega dinamicamente una fila al historial visual de transferencias.
     */
    private void agregarItemReciente(String nombre, String bancoNombre, String monto, String fecha, boolean entrada) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassName("transferencia_item");

        Div info = new Div();
        Span n = new Span(nombre);
        n.addClassName("transferencia_nombre");
        Span b = new Span(bancoNombre);
        b.addClassName("transferencia_banco");
        info.add(n, b);

        Span m = new Span(monto);
        // Aplica clase CSS segun si el dinero entra o sale para cambiar el color (verde/rojo)
        m.addClassName(entrada ? "monto_entrada" : "monto_salida");

        fila.add(info, m);
        listaRecientes.addComponentAsFirst(fila);
    }
}