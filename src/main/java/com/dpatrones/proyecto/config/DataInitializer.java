package com.dpatrones.proyecto.config;

import com.dpatrones.proyecto.model.*;
import com.dpatrones.proyecto.patterns.observer.*;
import com.dpatrones.proyecto.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdminRepository adminRepository;

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            cargarProductos();
            cargarUsuarios();
            cargarAdmins();
            configurarObservers();
        }
    }

    private void cargarProductos() {
        productoRepository.save(Producto.builder()
                .nombre("Camiseta Básica").descripcion("Camiseta de algodón 100%")
                .precio(29.90).stock(50).talla("M").color("Blanco")
                .tipoTela("Algodón").categoria("Camiseta").build());

        productoRepository.save(Producto.builder()
                .nombre("Camiseta Premium").descripcion("Camiseta de algodón pima")
                .precio(49.90).stock(30).talla("L").color("Negro")
                .tipoTela("Algodón Pima").categoria("Camiseta").build());

        productoRepository.save(Producto.builder()
                .nombre("Polo Casual").descripcion("Polo con cuello y botones")
                .precio(59.90).stock(25).talla("M").color("Azul")
                .tipoTela("Piqué").categoria("Polo").build());

        productoRepository.save(Producto.builder()
                .nombre("Jean Clásico").descripcion("Jean de corte recto")
                .precio(89.90).stock(40).talla("32").color("Azul Oscuro")
                .tipoTela("Denim").categoria("Pantalón").build());

        productoRepository.save(Producto.builder()
                .nombre("Pantalón Casual").descripcion("Pantalón de vestir")
                .precio(79.90).stock(20).talla("30").color("Beige")
                .tipoTela("Gabardina").categoria("Pantalón").build());

        productoRepository.save(Producto.builder()
                .nombre("Vestido Elegante").descripcion("Vestido para ocasiones especiales")
                .precio(129.90).stock(15).talla("S").color("Rojo")
                .tipoTela("Seda").categoria("Vestido").build());
    }

    private void cargarUsuarios() {
        usuarioRepository.save(Usuario.builder()
                .nombre("Juan Pérez").email("juan@email.com").password("123456")
                .direccion("Av. Arequipa 123, Lima").telefono("987654321").build());

        usuarioRepository.save(Usuario.builder()
                .nombre("María García").email("maria@email.com").password("123456")
                .direccion("Jr. Cusco 456, Lima").telefono("912345678").build());
    }

    private void cargarAdmins() {
        adminRepository.save(Admin.builder()
                .nombre("Admin Principal").email("admin@tienda.com")
                .password("admin123").area("Gerencia").build());

        adminRepository.save(Admin.builder()
                .nombre("Operador Logística").email("logistica@tienda.com")
                .password("log123").area("Logística").build());
    }

    private void configurarObservers() {
        VentasSubject subject = VentasSubject.getInstance();
        subject.agregarObservador(new DashboardObserver("Dashboard"));
        subject.agregarObservador(new InventarioObserver());
    }
}
