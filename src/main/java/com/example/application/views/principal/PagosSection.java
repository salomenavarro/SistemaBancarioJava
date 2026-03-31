package com.example.application.views.principal;

// ── IMPORTS ──────────────────────────────────────────────────────────────────
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;       // lista desplegable
import com.vaadin.flow.component.textfield.TextField;     // campo de texto
import com.vaadin.flow.component.notification.Notification; // mensaje emergente
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
 
import java.util.ArrayList;  // lista dinámica de Java (para el historial)
import java.util.List;
 
// ── CLASE ─────
public class PagosSection extends VerticalLayout {

     // ── ATRIBUTOS DE INSTANCIA ────────────────────────────────────────────────
    // Se declaran aquí (fuera del constructor) para que los métodos auxiliares
    // también puedan acceder a ellos. Esto es encapsulamiento: son "private"
    // porque solo esta clase los necesita.
 
    private ComboBox<String> selectorServicio;
    private TextField campoReferencia;
    private TextField campoMonto;
    private VerticalLayout contenedorHistorial; // se actualiza al pagar
 


    public PagosSection() {
        setWidth("100%");
        setPadding(false);
        setSpacing(false);
        addClassName("pagos_section");
 
        // ── ENCABEZADO DE SECCIÓN ─────────────────────────────────────────────
        Div encabezado = new Div();
        encabezado.addClassName("section_encabezado");
 
        Span etiqueta = new Span("Servicios");
        etiqueta.addClassName("section_etiqueta");
 
        H2 titulo = new H2("Pagos");
        titulo.addClassName("section_titulo");
 
        encabezado.add(etiqueta, titulo);
 
        // ── CONTENEDOR DE DOS COLUMNAS ────────────────────────────────────────
        // Izquierda: formulario de pago
        // Derecha: historial de pagos
        HorizontalLayout columnas = new HorizontalLayout();
        columnas.addClassName("pagos_columnas");
        columnas.setWidth("100%");
 
        columnas.add(crearFormularioPago(), crearHistorial());
 
        add(encabezado, columnas);
    }
 
    // ── MÉTODO: construye el formulario de pago ───────────────────────────────
    // "private" porque solo se usa aquí dentro.
    // Retorna un Div que contiene todo el formulario.
    private Div crearFormularioPago() {
 
        Div formulario = new Div();
        formulario.addClassName("pagos_formulario");
 
        H3 tituloForm = new H3("Nuevo pago");
        tituloForm.addClassName("form_titulo");
 
        // ── SELECTOR DE SERVICIO ──────────────────────────────────────────────
        // ComboBox = lista desplegable con opciones
        selectorServicio = new ComboBox<>("Servicio");
        selectorServicio.addClassName("campo_full");
 
        // Estas son las opciones disponibles
        selectorServicio.setItems(
            "Energía",
            "Agua",
            "Celular",
            "Internet",
            "TV Cable",
            "Arriendo"
        );
        selectorServicio.setPlaceholder("Selecciona un servicio");
 
        // ── CAMPOS DE TEXTO ───────────────────────────────────────────────────
        campoReferencia = new TextField("N° de referencia");
        campoReferencia.addClassName("campo_full");
        campoReferencia.setPlaceholder("Número de factura o contrato");
 
        campoMonto = new TextField("Valor a pagar");
        campoMonto.addClassName("campo_full");
        campoMonto.setPlaceholder("Ej: 85000");
        campoMonto.setPrefixComponent(new Span("$")); // muestra $ al inicio
 
        // ── BOTÓN DE PAGO ─────────────────────────────────────────────────────
        Button btnPagar = new Button("Confirmar pago");
        btnPagar.addClassName("btn_primario_verde");
 
 
        formulario.add(tituloForm, selectorServicio, campoReferencia, campoMonto, btnPagar);
        return formulario;
    }
 
    // ── MÉTODO: construye el panel del historial ──────────────────────────────
    private Div crearHistorial() {
 
        Div panel = new Div();
        panel.addClassName("pagos_historial");
 
        H3 titulo = new H3("Pagos realizados");
        titulo.addClassName("form_titulo");
 
        // contenedorHistorial es un atributo de clase (no variable local)
        contenedorHistorial = new VerticalLayout();
        contenedorHistorial.setPadding(false);
        contenedorHistorial.setSpacing(false);
 
        // Datos de ejemplo para mostrar algo al cargar
        agregarPagoAlHistorial(" Energía — EPM",        "25 mar 2026", "$85.000",  true);
        agregarPagoAlHistorial(" Agua — Acueducto",     "22 mar 2026", "$42.000",  true);
        agregarPagoAlHistorial(" Celular — Claro",      "15 mar 2026", "$60.000",  true);
        agregarPagoAlHistorial(" Internet — UNE",       "Pendiente",   "$75.000",  false);
 
        panel.add(titulo, contenedorHistorial);
        return panel;
    }
 
  
    // ── MÉTODO AUXILIAR: agrega una fila al historial ─────────────────────────
    /**
     * Crea y agrega una fila de pago al historial visual.
     * @param nombre       Nombre del servicio pagado
     * @param fecha        Fecha del pago
     * @param monto        Valor pagado formateado
     * @param completado   true = verde, false = gris (pendiente)
     */
    private void agregarPagoAlHistorial(String nombre, String fecha,
                                        String monto, boolean completado) {
        // Fila horizontal: ícono | info | monto
        HorizontalLayout fila = new HorizontalLayout();
        fila.addClassName("historial_fila");
        fila.setAlignItems(Alignment.CENTER);
 
        // Punto de color
        Span punto = new Span();
        punto.addClassName(completado ? "punto_verde" : "punto_gris");
 
        // Info: nombre y fecha
        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);
 
        Span spanNombre = new Span(nombre);
        spanNombre.addClassName("historial_nombre");
 
        Span spanFecha = new Span(fecha);
        spanFecha.addClassName("historial_fecha");
 
        info.add(spanNombre, spanFecha);
 
        // Monto a la derecha
        Span spanMonto = new Span(monto);
        spanMonto.addClassName("historial_monto");
 
        fila.add(punto, info, spanMonto);
        fila.expand(info); // info ocupa el espacio sobrante en el centro
 
        contenedorHistorial.add(fila);
    }

 
}
