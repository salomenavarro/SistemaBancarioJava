package com.example.application.views.principal;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Principal")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
public class PrincipalView extends Composite<VerticalLayout> {

    public PrincipalView() {
       getContent().setWidth("100%");
        getContent().setPadding(false);
        getContent().setSpacing(false);

        getContent().add(new HeroSection());
        getContent().add(new TiposDeCuentasSection());
        getContent().add(new UsuariosSection());
        getContent().add(new TransferenciasSection());
        getContent().add(new CreditosSection());
        getContent().add(new PagosSection());
    }
}
