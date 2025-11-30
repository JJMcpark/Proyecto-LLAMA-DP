package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.service.VentaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public Venta crearVenta(@RequestBody VentaRequest request) {
        // Por ahora confiamos en el total que viene del frontend
        // (en algo más serio, recalcularíamos el total en el backend)
        return ventaService.registrarVentaAnonima(request.getTotal());
    }

    @Data
    public static class VentaRequest {
        private BigDecimal total;
    }
}