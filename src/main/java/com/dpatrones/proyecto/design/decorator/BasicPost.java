package com.dpatrones.proyecto.design.decorator;

/**
 * PATRÓN DECORATOR - Componente Concreto
 * Post base sin decoraciones
 */
public class BasicPost implements PostContent {
    
    private String content;
    
    public BasicPost(String content) {
        this.content = content;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public double getCost() {
        return 0;
    }
}
