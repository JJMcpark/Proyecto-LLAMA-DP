package com.dpatrones.proyecto.patterns.factory;

/**
 * PATRÓN FACTORY - Procesador de Pago Contraentrega (Yape/Plin/Efectivo)
 */
public class ContraentregaProcesador implements IProcesadorPago {

    @Override
    public boolean procesarPago(Double monto) {
        System.out.println("[PAGO] Pago CONTRAENTREGA registrado por S/." + monto);
        System.out.println("[PAGO] El cliente pagará al recibir el producto.");
        return true;
    }

    @Override
    public String getNombre() {
        return "CONTRAENTREGA";
    }

    @Override
    public boolean validarDatos() {
        return true;
    }
}
