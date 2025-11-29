package com.dpatrones.proyecto.patterns.factory;

/**
 * PATRÓN FACTORY - Procesador de Pago Contraentrega (Yape/Plin/Efectivo)
 * 
 * Permite al cliente pagar al momento de recibir el pedido.
 * Soporta múltiples métodos: efectivo, Yape, Plin.
 */
public class ContraentregaProcesador implements IProcesadorPago {
    
    private final String metodoContraentrega; // EFECTIVO, YAPE, PLIN
    private final String telefonoContacto;
    
    public ContraentregaProcesador() {
        this.metodoContraentrega = "EFECTIVO";
        this.telefonoContacto = null;
    }
    
    public ContraentregaProcesador(String metodoContraentrega, String telefonoContacto) {
        this.metodoContraentrega = metodoContraentrega != null ? metodoContraentrega.toUpperCase() : "EFECTIVO";
        this.telefonoContacto = telefonoContacto;
    }

    @Override
    public boolean procesarPago(Double monto) {
        System.out.println("[PAGO] Pago CONTRAENTREGA registrado por S/." + String.format("%.2f", monto));
        System.out.println("[PAGO] Método de pago al recibir: " + metodoContraentrega);
        if (telefonoContacto != null && !telefonoContacto.isEmpty()) {
            System.out.println("[PAGO] Teléfono de contacto: " + telefonoContacto);
        }
        System.out.println("[PAGO] El cliente pagará al recibir el producto.");
        return true;
    }

    @Override
    public String getNombre() {
        return "CONTRAENTREGA";
    }

    @Override
    public boolean validarDatos() {
        System.out.println("[PAGO] Validando datos de contraentrega...");
        // Para contraentrega, solo validamos que haya una dirección de envío (se valida en el facade)
        return true;
    }
    
    public String getMetodoContraentrega() {
        return metodoContraentrega;
    }
}
