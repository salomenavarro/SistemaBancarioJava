package com.example.application.views.principal;

import com.example.application.modelo.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Seccion encargada de la gestion y visualizacion de los diferentes tipos de cuentas bancarias.
 * Permite la creacion de nuevas cuentas y muestra un resumen en una tabla.
 */
public class TiposDeCuentasSection extends VerticalLayout {

    // Instancia compartida de la logica del banco (Singleton)
    private Banco banco = Banco.getInstancia();
    
    // Componente de tabla para mostrar las cuentas del usuario actual
    private Grid<Cuenta> grid = new Grid<>(Cuenta.class, false);
    
    // Referencia a otra seccion de la vista para permitir la comunicacion entre componentes
    private HeroSection heroSection;

    /**
     * Constructor que recibe la referencia de HeroSection para poder actualizarla
     * cuando se cree una cuenta nueva.
     */
    public TiposDeCuentasSection(HeroSection heroSection) {
        this.heroSection = heroSection;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        // Composicion de la interfaz
        add(
                new H2("Tipos de Cuentas"),
                crearTarjetas(),
                crearFormulario(),
                crearTabla());

        // Carga inicial de datos si hay un cliente con sesion activa
        if (banco.getClienteActivo() != null) {
            grid.setItems(banco.getClienteActivo().getCuentas());
        }
    }

    /**
     * Metodo publico para refrescar la tabla desde otros componentes.
     */
    public void actualizarGrid() {
        if (banco.getClienteActivo() != null) {
            grid.setItems(banco.getClienteActivo().getCuentas());
        }
    }

    /**
     * Genera la fila superior de tarjetas informativas (informativas/visuales).
     */
    private HorizontalLayout crearTarjetas() {
        Div ahorro = crearTarjeta("Cuenta Ahorros", "Guarda dinero", "3%");
        Div corriente = crearTarjeta("Cuenta Corriente", "Uso diario", "Frecuente");
        Div premium = crearTarjeta("Cuenta Premium", "Sin límites", "VIP");

        HorizontalLayout layout = new HorizontalLayout(ahorro, corriente, premium);
        layout.setSpacing(true);
        return layout;
    }

    /**
     * Crea una tarjeta individual aplicando estilos directos y variables de CSS.
     */
    private Div crearTarjeta(String titulo, String desc, String dato) {
        Div card = new Div(new H3(titulo), new Paragraph(desc), new Span(dato));
        
        // Uso de variables CSS (var--) para mantener consistencia visual con el tema global
        card.getStyle()
                .set("padding", "20px")
                .set("border-radius", "15px")
                .set("background", "var(--dess-verde-oscuro)")
                .set("color", "white")
                .set("width", "250px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");
        return card;
    }

    /**
     * Define el formulario de registro para nuevas cuentas.
     */
    private VerticalLayout crearFormulario() {
        TextField nombre = new TextField("Nombre");
        TextField cedula = new TextField("Cédula");
        
        ComboBox<String> tipoCuenta = new ComboBox<>("Tipo");
        tipoCuenta.setItems("Ahorros", "Corriente", "Premium");
        
        NumberField saldo = new NumberField("Saldo inicial");
        
        Button crear = new Button("Crear cuenta");
        crear.addClassName("btn-main"); // Estilo definido en el archivo CSS externo

        // Logica al hacer clic en el boton de creacion
        crear.addClickListener(e -> {
            // Validacion de campos vacios
            if (tipoCuenta.getValue() == null || saldo.getValue() == null || nombre.isEmpty() || cedula.isEmpty()) {
                Notification.show("Completa todos los campos");
                return;
            }

            Cliente cliente = banco.getClienteActivo();
            if (cliente == null) {
                Notification.show("Debes iniciar sesión");
                return;
            }

            // Generacion de numero de cuenta unico basado en tiempo actual
            String numeroCuenta = String.valueOf(System.currentTimeMillis());
            Cuenta nuevaCuenta = null;

            // Instanciacion polimorfica segun la seleccion del usuario
            if ("Ahorros".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaAhorros(numeroCuenta, nombre.getValue(), saldo.getValue(), "Ahorros");
            } else if ("Corriente".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaCorriente(numeroCuenta, nombre.getValue(), saldo.getValue(), "Corriente", 500000);
            } else if ("Premium".equals(tipoCuenta.getValue())) {
                nuevaCuenta = new CuentaPremium(numeroCuenta, nombre.getValue(), saldo.getValue(), "Premium");
            }

            // Intento de registro en el modelo de negocio (Banco)
            if (banco.abrirCuenta(nuevaCuenta)) {
                // Actualiza la tabla local
                actualizarGrid();
                
                // Comunicacion entre componentes: ordena a HeroSection actualizar sus balances visuales
                if (heroSection != null) {
                    heroSection.actualizarInterfaz();
                }

                Notification.show("Cuenta creada correctamente");
            }

            // Limpieza de campos tras la operacion
            nombre.clear();
            cedula.clear();
            saldo.clear();
            tipoCuenta.clear();
        });

        // Organizacion visual del formulario
        FormLayout form = new FormLayout(nombre, cedula, tipoCuenta, saldo, crear);
        VerticalLayout contenedor = new VerticalLayout(form);
        contenedor.addClassName("box"); 
        return contenedor;
    }

    /**
     * Configura el Grid (tabla) para mostrar las propiedades de los objetos Cuenta.
     */
    private Div crearTabla() {
        // Vincula las columnas con los metodos de la clase Cuenta
        grid.addColumn(Cuenta::getTipoCuenta).setHeader("Tipo");
        grid.addColumn(Cuenta::getSaldo).setHeader("Saldo");

        Div contenedorGrid = new Div(grid);
        contenedorGrid.addClassName("grid-box");
        contenedorGrid.setWidthFull();
        return contenedorGrid;
    }
}