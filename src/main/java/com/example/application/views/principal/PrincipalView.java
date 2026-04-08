package com.example.application.views.principal;

import com.example.application.modelo.Banco;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("DESS")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class PrincipalView extends Composite<VerticalLayout> {

    // Se obtiene la instancia del banco (Encapsulamiento)
    private Banco banco = Banco.getInstancia();

    public PrincipalView() {
        getContent().setWidth("100%");
        getContent().setPadding(false);
        getContent().setSpacing(false);

        // Verificación de estado para seguridad de la app
        if (!banco.haySesionActiva()) {
            mostrarLogin();
        } else {
            mostrarApp();
        }
    }

    private void mostrarLogin() {
        getContent().removeAll();
        // Cuando el login sea exitoso, ejecutará mostrarApp
        getContent().add(new LoginSection(banco, this::mostrarApp, this::mostrarLogin));
    }

    private void mostrarApp() {
        getContent().removeAll();

        // 1. Instanciación de las secciones que NO dependen del Hero
        UsuariosSection seccUsuarios = new UsuariosSection(banco.getClienteActivo());
        TransferenciasSection seccTransferencias = new TransferenciasSection();
        CreditosSection seccCreditos = new CreditosSection();
        PagosSection seccPagos = new PagosSection();

        // 2. Creamos el Hero PRIMERO (necesitamos los otros componentes para el scroll)
        // Nota: seccCuentas aún no existe, así que lo pasaremos después o usaremos un marcador
        // Pero para que el constructor de TiposDeCuentas funcione, el Hero debe estar listo.
        
        HeroSection hero = new HeroSection(
                banco,
                this::mostrarLogin, 
                new VerticalLayout(), // Marcador temporal para evitar error de parámetros
                seccTransferencias,
                seccPagos,
                seccCreditos);

        // 3. Ahora creamos TiposDeCuentasSection pasándole el hero ya creado
        TiposDeCuentasSection seccCuentas = new TiposDeCuentasSection(hero);

        // 4. Actualización reactiva de la interfaz
        seccTransferencias.setAlActualizar(() -> {
            hero.actualizarInterfaz();
            seccCuentas.actualizarGrid();
        });

        getContent().add(hero, seccCuentas, seccUsuarios, seccTransferencias, seccCreditos, seccPagos);
    }
}