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

    // El banco existe una sola vez y lo comparten todas las secciones
    private Banco banco = new Banco("DESS");

    public PrincipalView() {
        getContent().setWidth("100%");
        getContent().setPadding(false);
        getContent().setSpacing(false);

        // Si no hay sesión → login. Si hay sesión → app
        if (!banco.haySesionActiva()) {
            mostrarLogin();
        } else {
            mostrarApp();
        }
    }

    private void mostrarLogin() {
        getContent().removeAll();
        getContent().add(new LoginSection(banco, this::mostrarApp, this::mostrarLogin));
    }

    private void mostrarApp() {
        getContent().removeAll();
        getContent().add(new HeroSection(banco, this::mostrarLogin));
        getContent().add(new TiposDeCuentasSection());
        getContent().add(new UsuariosSection(banco.getClienteActivo()));
        getContent().add(new TransferenciasSection());
        getContent().add(new CreditosSection());
        getContent().add(new PagosSection(banco));
    }
}