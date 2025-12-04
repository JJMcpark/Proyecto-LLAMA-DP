package com.dpatrones.proyecto.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.patterns.observer.VentasSubject;
import com.dpatrones.proyecto.repository.VentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public long contarVentas() {
        return ventaRepository.count();
    }

    public BigDecimal getTotalVentas() {
        return ventaRepository.findAll().stream()
            .map(Venta::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Venta sin usuario logueado
    public Venta registrarVentaAnonima(BigDecimal total) {
        Venta venta = new Venta();
        venta.setUsuarioId(null); 
        venta.setTotal(total);
        Venta saved = ventaRepository.save(venta);
        
        // Notificar a los observadores (Swing)
        VentasSubject.getInstance().notificarNuevaVenta(saved.getId(), total.doubleValue());
        
        return saved;
    }
}