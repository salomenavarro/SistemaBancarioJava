package com.example.application.views.principal;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeroSection extends VerticalLayout {
    

    //Prueba
    public HeroSection() {
        setWidth("100%");
        setPadding(false);
        
        // código visual va aquí adentro
        H1 titulo = new H1("Tu banco, simple y seguro");
        add(titulo);
    }
}
