package com.dpatrones.proyecto.patterns.factory;

public class ContraentregaProcesador implements IProcesadorPago {
    
    private final String metodoContraentrega;
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
        System.out.println("[FACTORY] Pago CONTRAENTREGA registrado por S/." + String.format("%.2f", monto));
        System.out.println("[FACTORY] Método al recibir: " + metodoContraentrega);
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
    
    public String getMetodoContraentrega() {
        return metodoContraentrega;
    }
}
