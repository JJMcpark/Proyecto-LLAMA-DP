package com.dpatrones.proyecto.service;

import com.dpatrones.proyecto.model.Producto;
import com.dpatrones.proyecto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
    
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public List<Producto> buscarConStock() {
        return productoRepository.findByStockGreaterThan(0);
    }
    
    /**
     * Descuenta stock del producto
     * @return true si había stock suficiente
     */
    public boolean descontarStock(Long productoId, int cantidad) {
        Optional<Producto> opt = productoRepository.findById(productoId);
        if (opt.isPresent()) {
            Producto p = opt.get();
            if (p.getStock() >= cantidad) {
                p.setStock(p.getStock() - cantidad);
                productoRepository.save(p);
                System.out.println("[INVENTARIO] Stock actualizado: " + p.getNombre() + " -> " + p.getStock());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica si hay stock disponible
     */
    public boolean hayStock(Long productoId, int cantidad) {
        Optional<Producto> opt = productoRepository.findById(productoId);
        return opt.map(p -> p.getStock() >= cantidad).orElse(false);
    }
}
