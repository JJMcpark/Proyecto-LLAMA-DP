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

    public boolean descontarStock(Long productoId, int cantidad) {
        return productoRepository.findById(productoId)
                .filter(p -> p.getStock() >= cantidad)
                .map(p -> {
                    p.setStock(p.getStock() - cantidad);
                    productoRepository.save(p);
                    return true;
                })
                .orElse(false);
    }

    public boolean hayStock(Long productoId, int cantidad) {
        return productoRepository.findById(productoId)
                .map(p -> p.getStock() >= cantidad)
                .orElse(false);
    }
}
