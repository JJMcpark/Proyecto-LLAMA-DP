package com.dpatrones.proyecto.design.decorator;

/**
 * PATRÓN DECORATOR - Decorador Concreto
 * Añade hashtags (#tema) al post
 */
public class HashtagDecorator extends PostDecorator {
    
    public HashtagDecorator(PostContent post) {
        super(post);
    }
    
    @Override
    public String getContent() {
        return post.getContent() + "\n[HASHTAGS INDEXADOS]";
    }
    
    @Override
    public double getCost() {
        return post.getCost() + 0.3; // Costo de indexar hashtags
    }
}
