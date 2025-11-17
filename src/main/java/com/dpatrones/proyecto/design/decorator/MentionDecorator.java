package com.dpatrones.proyecto.design.decorator;

/**
 * PATRÓN DECORATOR - Decorador Concreto
 * Añade menciones (@usuario) al post
 */
public class MentionDecorator extends PostDecorator {
    
    public MentionDecorator(PostContent post) {
        super(post);
    }
    
    @Override
    public String getContent() {
        return post.getContent() + "\n[MENCIONES PROCESADAS]";
    }
    
    @Override
    public double getCost() {
        return post.getCost() + 0.5; // Costo de procesar menciones
    }
}
