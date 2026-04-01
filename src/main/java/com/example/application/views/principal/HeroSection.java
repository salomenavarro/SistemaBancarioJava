package com.example.application.views.principal;

// ── IMPORTS ──────────────────────────────────────────────────────────────────
// Cada import trae una "herramienta" de Vaadin que vamos a usar.
// Si no importas algo, Java no sabe qué es y da error.

import com.vaadin.flow.component.html.Div; // como un <div> en HTML
import com.vaadin.flow.component.html.H1; // título grande <h1>
import com.vaadin.flow.component.html.H2; // título mediano <h2>
import com.vaadin.flow.component.html.Paragraph; // texto normal <p>
import com.vaadin.flow.component.html.Span; // texto en línea <span>
import com.vaadin.flow.component.button.Button; // botón clickeable
import com.vaadin.flow.component.orderedlayout.HorizontalLayout; // fila →
import com.vaadin.flow.component.orderedlayout.VerticalLayout; // columna ↓

// ── CLASE ────────────────────────────────────────────────────────────────────
// "extends VerticalLayout" significa que HeroSection ES un layout vertical.

// Todo lo que agreguemos con add(item1, item2, item3) va aparecer apilado de arriba hacia abajo. ⬇️

public class HeroSection extends VerticalLayout {

    // ── CONSTRUCTOR ──────────────────────────────────────────────────────────
    // Esto se ejecuta cuando PrincipalView hace un: new HeroSection()
    // Creamos un constructor donde vamos construir todo lo visual
    public HeroSection() {
        // Configuración del contenedor principal (esta sección)

        // Configuración del contenedor principal (esta sección)
        setWidth("100%"); // ocupa todo el ancho disponible
        setPadding(false); // sin padding propio (lo maneja el CSS)
        setSpacing(false); // sin espacio entre elementos hijos
        addClassName("hero_section"); // nombre CSS para estilizar desde el .css

        // ── PARTE IZQUIERDA: texto y botones
        // ──────────────────────────────────────────────────────────
        // VerticalLayout = columna, los elementos van uno debajo del otro ↓
        VerticalLayout contenidoIzquierdo = new VerticalLayout(); // Creamos una variable para los contenidos
        contenidoIzquierdo.addClassName("hero_contenido_izquierdo"); // Agregamos una clase para los estilos del
        contenidoIzquierdo.setPadding(false); // Sin Padding propio
        contenidoIzquierdo.setSpacing(false); // Sin espacio entre elementos hijos

        // Etiqueta pequeña arriba del título
        // contenedor en línea (inline) genérico utilizado para agrupar elementos o texto
        Span miniEtiqueta = new Span("Bienvenido a DESS");
        miniEtiqueta.addClassName("hero_etiqueta");

        // Título
        H1 tituloPrincipal = new H1("Tu banco, simple y seguro");
        tituloPrincipal.addClassName("hero_titulo");

        // Descripcion debajo del titulo
        Paragraph descripcion = new Paragraph(
                "Administra tus finanzas, realiza transferencias " +
                        "y paga tus servicios desde un solo lugar.");
        descripcion.addClassName("hero_descripcion");

        // ── BOTONES ──────────────────────────────────────────────────────────
        // HorizontalLayout = fila, los elementos van uno al lado del otro →
        HorizontalLayout filaBotones = new HorizontalLayout(); // creamos una variable para los botones de la seccion
                                                               // izquierda
        filaBotones.addClassName("hero_botones");
        filaBotones.setSpacing(true); // Con espacio entre los elementos hijos

        Button btnAbrir = new Button("Abrir cuenta");
        btnAbrir.addClassName("btn_primario");
        // Cuando implementemos la logica aqui se va a conectar
        Button btnConocer = new Button("Conocer más");
        btnConocer.addClassName("btn_secundario");

        filaBotones.add(btnAbrir, btnConocer);
        // agregamos los botones parqa que se alienen uno al lado del otro

        // ── SECCIÓN DE ACCESOS RÁPIDOS (BOTONES PEQUEÑOS) ─────────────────────
        HorizontalLayout contenedorAccesoRapido = new HorizontalLayout();
        contenedorAccesoRapido.addClassName("chip_acceso_rapido");

        // Usamos el método auxiliar "crearBotonAccesoRapido" para no repetir código
        contenedorAccesoRapido.add(
                crearAccesoRapido("Abrir cuenta"),
                crearAccesoRapido("Transferir"),
                crearAccesoRapido("Pagar servicios"),
                crearAccesoRapido("Mis créditos")
            );

        // Agregamos todo las variables que creamos en el contenidoPrincipal izquierdo
        contenidoIzquierdo.add(miniEtiqueta, tituloPrincipal, descripcion, filaBotones, contenedorAccesoRapido  );

        // ── PARTE DERECHA: tarjeta bancaria ──────────────────────────────────
        // Creamos un div para contenener la parte visual de la tarjeta del cliente 
        Div tarjeta = new Div();

        
        tarjeta.addClassName("hero_tarjeta");

        Paragraph labelTarjeta = new Paragraph("Tarjeta DESS");
        labelTarjeta.addClassName("tarjeta_label");

        Paragraph nombreTitular = new Paragraph("María García López");
        nombreTitular.addClassName("tarjeta_nombre");
 
        Paragraph labelSaldo = new Paragraph("Saldo disponible");
        labelSaldo.addClassName("tarjeta_saldo_label");

        //  — se actualizará con lógica real más adelante

         H2 saldo = new H2("$4.250.000");
        saldo.addClassName("tarjeta_saldo");

         // Fila inferior de la tarjeta: número, tipo, vigencia
        HorizontalLayout filaTarjeta = new HorizontalLayout();
        filaTarjeta.addClassName("tarjeta_fila_info");

         filaTarjeta.add(
            crearItemTarjeta("N° Cuenta", "**** 4821"),
            crearItemTarjeta("Tipo", "Ahorros"),
            crearItemTarjeta("Válida", "12/28")
        );
                
        tarjeta.add(labelTarjeta, nombreTitular, labelSaldo, saldo, filaTarjeta);

        // ── ENSAMBLADO FINAL ─────────────────────────────────────────────────
        // HorizontalLayout pone izquierda y derecha uno al lado del otro →
        HorizontalLayout contenedorPrincipal = new HorizontalLayout();
        contenedorPrincipal.addClassName("hero_contenedor");
        contenedorPrincipal.setWidth("100%");
 
        contenedorPrincipal.add(contenidoIzquierdo, tarjeta);
 
        // add() agrega el contenedor a ESTA sección (HeroSection)
        add(contenedorPrincipal);
    }

    
        // ── MÉTODOS AUXILIARES ────────────────────────────────────────────────────
        // Estos métodos privados ayudan a no repetir código (buena práctica).
        // "private" = solo se usan dentro de esta clase (encapsulamiento).

        /**
         * Creamos un botón de acceso rápido con el texto dado.
         * 
         * @param texto El texto que muestra el chip
         * @return un Span estilizado como chip
         */

    private Span crearAccesoRapido(String texto) {
        Span boton = new Span(texto);
        boton.addClassName("hero_chip"); // Nombre claro para CSS
        return boton;
    }

    /**
     * Crea un elemento de info para la tarjeta (label + valor).
     * @param label La etiqueta (ej: "Tipo")
     * @param valor El valor (ej: "Ahorros")
     * @return un Div con label y valor apilados
     */

      private Div crearItemTarjeta(String label, String valor) {
        Div item = new Div();
        item.addClassName("tarjeta_item");
 
        Span spanLabel = new Span(label);
        spanLabel.addClassName("tarjeta_item_label");
 
        Span spanValor = new Span(valor);
        spanValor.addClassName("tarjeta_item_valor");
 
        item.add(spanLabel, spanValor);
        return item;
    }

}