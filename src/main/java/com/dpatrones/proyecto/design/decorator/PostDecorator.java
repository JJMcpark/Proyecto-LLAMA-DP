package com.dpatrones.proyecto.design.decorator;

/**
 * PATRÓN DECORATOR - Decorador Abstracto
 */
public abstract class PostDecorator implements PostContent {
    
    protected PostContent post;
    
    public PostDecorator(PostContent post) {
        this.post = post;
    }
    
    @Override
    public String getContent() {
        return post.getContent();
    }
    
    @Override
    public double getCost() {
        return post.getCost();
    }
}
