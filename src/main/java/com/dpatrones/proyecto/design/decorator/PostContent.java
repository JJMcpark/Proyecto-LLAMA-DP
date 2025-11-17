package com.dpatrones.proyecto.design.decorator;

/**
 * PATRÓN DECORATOR
 * Enriquece un Post con decoraciones (menciones, tags, etc.)
 */
public interface PostContent {
    String getContent();
    double getCost();
}
