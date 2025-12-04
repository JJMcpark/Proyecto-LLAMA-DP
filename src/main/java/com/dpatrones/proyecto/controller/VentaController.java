package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.model.Venta;
import com.dpatrones.proyecto.service.VentaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.listarTodas();
    }

    @GetMapping("/total")
    public Map<String, BigDecimal> totalVentas() {
        BigDecimal total = ventaService.getTotalVentas();
        Map<String, BigDecimal> resp = new HashMap<>();
        resp.put("total", total);
        return resp;
    }

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