package com.dpatrones.proyecto.patterns.decorator;

public class BordadoDecorator extends ProductoDecorator {
    
    private static final Double COSTO_BORDADO = 25.00;
    private static final int MAX_CARACTERES = 50;
    
    private final String textoBordado;
    
    public BordadoDecorator(IProductoComponente producto) {
        super(producto);
        this.textoBordado = "Texto personalizado";
    }
    
    public BordadoDecorator(IProductoComponente producto, String textoBordado) {
        super(producto);
        if (textoBordado == null || textoBordado.trim().isEmpty()) {
            this.textoBordado = "Texto personalizado";
        } else if (textoBordado.length() > MAX_CARACTERES) {
            this.textoBordado = textoBordado.substring(0, MAX_CARACTERES);
        } else {
            this.textoBordado = textoBordado.trim();
        }
    }

    @Override
    public Double getPrecio() {
        return super.getPrecio() + COSTO_BORDADO;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Bordado ('" + textoBordado + "')";
    }
    
    @Override
    public String getExtras() {
        String extrasAnteriores = super.getExtras();
        String nuevoExtra = "Bordado";
        return extrasAnteriores.isEmpty() ? nuevoExtra : extrasAnteriores + "," + nuevoExtra;
    }
    
    public String getTextoBordado() {
        return textoBordado;
    }
    
    public static Double getCostoBordado() {
        return COSTO_BORDADO;
    }
}
