package com.dpatrones.proyecto.controller;

import com.dpatrones.proyecto.model.Producto;
import com.dpatrones.proyecto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {
    
    private final ProductoService productoService;
    
    @GetMapping
    public List<Producto> listarTodos() {
        return productoService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/disponibles")
    public List<Producto> listarDisponibles() {
        return productoService.buscarConStock();
    }
    
    @GetMapping("/categoria/{categoria}")
    public List<Producto> buscarPorCategoria(@PathVariable String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }
    
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }
    
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.buscarPorId(id)
            .map(p -> {
                producto.setId(id);
                return ResponseEntity.ok(productoService.guardar(producto));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoService.buscarPorId(id).isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
