package com.dpatrones.proyecto.patterns.factory;

public interface IProcesadorPago {
    boolean procesarPago(Double monto);
    String getNombre();
    boolean validarDatos();
}
