package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;

    // Venta sin usuario logueado
    public Venta registrarVentaAnonima(BigDecimal total) {
        Venta venta = new Venta();
        venta.setUsuarioId(null); 
        venta.setTotal(total);
        return ventaRepository.save(venta);
    }
}