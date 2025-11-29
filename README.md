# 📚 DOCUMENTACIÓN COMPLETA DEL PROYECTO
## Sistema de Gestión de Pedidos con Patrones de Diseño

**Universidad Tecnológica del Perú - Diseño de Patrones**  
**Proyecto: LLAMA - Tienda de Ropa Online**

---

## 📋 ÍNDICE

1. [Descripción General](#1-descripción-general)
2. [Tecnologías Utilizadas](#2-tecnologías-utilizadas)
3. [Arquitectura del Sistema](#3-arquitectura-del-sistema)
4. [Modelo de Datos (Entidades)](#4-modelo-de-datos-entidades)
5. [Patrones de Diseño Implementados](#5-patrones-de-diseño-implementados)
6. [Flujo de Compra (Checkout)](#6-flujo-de-compra-checkout)
7. [Diagramas UML](#7-diagramas-uml)
8. [Estructura del Proyecto](#8-estructura-del-proyecto)
9. [Cómo Ejecutar el Proyecto](#9-cómo-ejecutar-el-proyecto)
10. [Glosario de Términos](#10-glosario-de-términos)

---

## 1. DESCRIPCIÓN GENERAL

### ¿Qué es este proyecto?
Es un **sistema de e-commerce** para una tienda de ropa que permite:
- 🛒 Realizar compras con personalización de productos (bordados, estampados)
- 💳 Procesar pagos con diferentes métodos (tarjeta, PayPal, contraentrega)
- 📦 Gestionar el flujo logístico de pedidos
- 👨‍💼 Panel de administración con Swing para gestión interna

### Objetivo Académico
Demostrar la implementación práctica de **6 patrones de diseño** en un proyecto real:
- Singleton
- Factory Method
- Decorator
- Observer
- State
- Facade

---

## 2. TECNOLOGÍAS UTILIZADAS

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje principal |
| **Spring Boot** | 3.4.1 | Framework backend |
| **Spring Data JPA** | - | Acceso a base de datos |
| **MySQL** | 8.0 | Base de datos |
| **Lombok** | 1.18.36 | Reducir código boilerplate |
| **Maven** | - | Gestión de dependencias |
| **Swing** | - | Interfaz gráfica de admin |

---

## 3. ARQUITECTURA DEL SISTEMA

```
┌─────────────────────────────────────────────────────────────────┐
│                        CAPA DE PRESENTACIÓN                      │
├──────────────────────────┬──────────────────────────────────────┤
│   REST Controllers       │        Swing (Admin)                 │
│   (Web/API)              │        AdminFrame                    │
│   - ProductoController   │        LogisticaPanel                │
│   - PedidoController     │                                      │
│   - UsuarioController    │                                      │
└──────────────────────────┴──────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                        CAPA DE NEGOCIO                           │
├─────────────────────────────────────────────────────────────────┤
│   Services                    │   Patterns                       │
│   - ProductoService           │   - Decorator (extras producto)  │
│   - PedidoService             │   - Factory (procesadores pago)  │
│   - UsuarioService            │   - State (estados pedido)       │
│   - AdminService              │   - Observer (notificaciones)    │
│   - NotificacionService       │   - Singleton (sesión/conexión)  │
│                               │   - Facade (checkout)            │
└───────────────────────────────┴─────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────┐
│                        CAPA DE DATOS                             │
├─────────────────────────────────────────────────────────────────┤
│   Repositories (JPA)          │   Entities (Modelo)              │
│   - ProductoRepository        │   - Producto                     │
│   - PedidoRepository          │   - Pedido                       │
│   - UsuarioRepository         │   - Usuario                      │
│   - AdminRepository           │   - Admin                        │
│   - DetallePedidoRepository   │   - DetallePedido                │
└───────────────────────────────┴─────────────────────────────────┘
                                    │
                                    ▼
                        ┌─────────────────────┐
                        │      MySQL DB       │
                        │   proyecto_db       │
                        └─────────────────────┘
```

---

## 4. MODELO DE DATOS (ENTIDADES)

### 4.1 Diagrama Entidad-Relación

```
┌──────────────┐       ┌──────────────┐       ┌──────────────────┐
│   USUARIO    │       │    PEDIDO    │       │  DETALLE_PEDIDO  │
├──────────────┤       ├──────────────┤       ├──────────────────┤
│ id (PK)      │──┐    │ id (PK)      │──┐    │ id (PK)          │
│ nombre       │  │    │ fecha        │  │    │ cantidad         │
│ email        │  │    │ total        │  │    │ precioUnitario   │
│ password     │  └───>│ estado       │  └───>│ subtotal         │
│ direccion    │       │ metodoPago   │       │ extrasAplicados  │
│ telefono     │       │ metodoEnvio  │       │ costoExtras      │
└──────────────┘       │ direccion    │       │ producto_id (FK) │
                       │ usuario_id(FK│       │ pedido_id (FK)   │
                       └──────────────┘       └──────────────────┘
                                                      │
┌──────────────┐                              ┌───────▼──────┐
│    ADMIN     │                              │   PRODUCTO   │
├──────────────┤                              ├──────────────┤
│ id (PK)      │                              │ id (PK)      │
│ nombre       │                              │ nombre       │
│ email        │                              │ descripcion  │
│ password     │                              │ precio       │
│ area         │                              │ stock        │
└──────────────┘                              │ talla        │
                                              │ color        │
                                              │ tipoTela     │
                                              │ categoria    │
                                              └──────────────┘
```

### 4.2 Descripción de Entidades

#### 🧑 Usuario
```java
// Representa a un cliente de la tienda
public class Usuario {
    Long id;           // Identificador único
    String nombre;     // "Juan Pérez"
    String email;      // "juan@email.com"
    String password;   // Contraseña encriptada
    String direccion;  // "Av. Lima 123"
    String telefono;   // "999888777"
}
```

#### 👔 Producto
```java
// Representa una prenda de ropa
public class Producto {
    Long id;
    String nombre;      // "Polo Básico"
    String descripcion; // "Polo de algodón cómodo"
    Double precio;      // 49.90
    Integer stock;      // 100 unidades
    String talla;       // "M", "L", "XL"
    String color;       // "Azul", "Rojo"
    String tipoTela;    // "Algodón", "Poliéster"
    String categoria;   // "Camiseta", "Pantalón"
}
```

#### 📦 Pedido
```java
// Representa una orden de compra
public class Pedido {
    Long id;
    LocalDateTime fecha;      // Fecha de creación
    Double total;             // Monto total
    String estado;            // "PENDIENTE", "PAGADO", "ENVIADO", "ENTREGADO"
    String metodoPago;        // "TARJETA", "PAYPAL", "CONTRAENTREGA"
    String metodoEnvio;       // "ESTANDAR", "EXPRESS"
    String direccionEnvio;    // Dirección de entrega
    String codigoSeguimiento; // "TRK-A1B2C3D4"
    Usuario usuario;          // Cliente que hizo el pedido
    List<DetallePedido> detalles; // Productos del pedido
}
```

#### 📋 DetallePedido
```java
// Línea de un pedido (producto + cantidad + extras)
public class DetallePedido {
    Long id;
    Integer cantidad;        // 2 unidades
    Double precioUnitario;   // 69.90 (con extras)
    Double subtotal;         // 139.80
    String extrasAplicados;  // "Bordado,Estampado"
    Double costoExtras;      // 35.00
    Producto producto;       // Producto base
}
```

#### 👨‍💼 Admin
```java
// Administrador del sistema
public class Admin {
    Long id;
    String nombre;   // "Carlos Admin"
    String email;    // "admin@tienda.com"
    String password;
    String area;     // "Logistica", "Ventas", "SUPERVISOR"
}
```

---

## 5. PATRONES DE DISEÑO IMPLEMENTADOS

### 5.1 🔒 SINGLETON - Instancia Única

**Propósito:** Garantizar que una clase tenga una única instancia global.

**Implementación:** `AdminSession` y `AdminDatabaseConnection`

```
┌─────────────────────────────────────────────────────────┐
│                     AdminSession                         │
├─────────────────────────────────────────────────────────┤
│ - adminLogueado: Admin                                  │
│ - sesionActiva: boolean                                 │
├─────────────────────────────────────────────────────────┤
│ + getInstance(): AdminSession    ◄── Única entrada     │
│ + iniciarSesion(admin)                                  │
│ + cerrarSesion()                                        │
│ + getNombreAdmin(): String                              │
│ + tienePermisoArea(area): boolean                       │
└─────────────────────────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┐
         ▼               ▼               ▼
    AdminFrame    LogisticaPanel    Cualquier clase
    (misma instancia en todos lados)
```

**Código clave (Patrón Holder - Bill Pugh):**
```java
public class AdminSession {
    private AdminSession() { } // Constructor privado
    
    // Holder interno - lazy loading thread-safe
    private static class Holder {
        private static final AdminSession INSTANCE = new AdminSession();
    }
    
    public static AdminSession getInstance() {
        return Holder.INSTANCE; // Siempre la misma instancia
    }
}
```

**¿Por qué usarlo?**
- Solo UN admin puede estar logueado a la vez
- Solo UNA conexión a la BD para evitar saturar el servidor

---

### 5.2 🏭 FACTORY METHOD - Fábrica de Objetos

**Propósito:** Crear objetos sin especificar la clase exacta.

**Implementación:** `PaymentFactory` crea procesadores de pago.

```
                    ┌──────────────────────┐
                    │   PaymentFactory     │
                    ├──────────────────────┤
                    │ crearProcesador(tipo)│
                    └──────────┬───────────┘
                               │
           ┌───────────────────┼───────────────────┐
           ▼                   ▼                   ▼
┌──────────────────┐ ┌──────────────────┐ ┌───────────────────┐
│TarjetaProcesador │ │ PayPalProcesador │ │ContraentregaProc. │
├──────────────────┤ ├──────────────────┤ ├───────────────────┤
│ procesarPago()   │ │ procesarPago()   │ │ procesarPago()    │
│ validarDatos()   │ │ validarDatos()   │ │ validarDatos()    │
└──────────────────┘ └──────────────────┘ └───────────────────┘
         │                   │                      │
         └───────────────────┴──────────────────────┘
                             │
                  implements IProcesadorPago
```

**Código clave:**
```java
public class PaymentFactory {
    public static IProcesadorPago crearProcesador(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "TARJETA"      -> new TarjetaProcesador();
            case "PAYPAL"       -> new PayPalProcesador();
            case "CONTRAENTREGA"-> new ContraentregaProcesador();
            default -> throw new IllegalArgumentException("Tipo no soportado");
        };
    }
}

// Uso:
IProcesadorPago procesador = PaymentFactory.crearProcesador("PAYPAL");
procesador.procesarPago(150.00);
```

**¿Por qué usarlo?**
- Agregar nuevos métodos de pago sin modificar el código existente
- El código cliente no necesita conocer las clases concretas

---

### 5.3 🎨 DECORATOR - Añadir Funcionalidad Dinámicamente

**Propósito:** Agregar responsabilidades a objetos de forma dinámica.

**Implementación:** Extras para productos (bordado, estampado, empaque regalo).

```
              ┌─────────────────────────┐
              │   IProductoComponente   │  ◄── Interface
              ├─────────────────────────┤
              │ + getPrecio(): Double   │
              │ + getDescripcion(): Str │
              │ + getExtras(): String   │
              └───────────┬─────────────┘
                          │
          ┌───────────────┴───────────────┐
          ▼                               ▼
┌─────────────────┐             ┌─────────────────────┐
│  ProductoBase   │             │  ProductoDecorator  │ (abstracto)
├─────────────────┤             ├─────────────────────┤
│ - producto      │             │ - productoDecorado  │
│ + getPrecio()   │             └──────────┬──────────┘
│ + getDescripcion│                        │
└─────────────────┘         ┌──────────────┼──────────────┐
                            ▼              ▼              ▼
                 ┌────────────────┐ ┌────────────────┐ ┌────────────────┐
                 │BordadoDecorator│ │EstampadoDecorat│ │EmpaqueRegaloD. │
                 ├────────────────┤ ├────────────────┤ ├────────────────┤
                 │ +S/20.00       │ │ +S/15.00       │ │ +S/10.00       │
                 └────────────────┘ └────────────────┘ └────────────────┘
```

**Código clave:**
```java
// Producto base: Polo a S/50
IProductoComponente producto = new ProductoBase(polo);

// Agregar bordado (+S/20)
producto = new BordadoDecorator(producto, "Mi Nombre");

// Agregar estampado (+S/15)
producto = new EstampadoDecorator(producto, "Logo Cool");

// Precio final: S/85 (50 + 20 + 15)
System.out.println(producto.getPrecio());        // 85.0
System.out.println(producto.getDescripcion());   // "Polo + Bordado + Estampado"
```

**¿Por qué usarlo?**
- Combinar extras de forma flexible
- Evitar explosión de subclases (ProductoConBordado, ProductoConEstampado, etc.)

---

### 5.4 👁️ OBSERVER - Notificaciones Automáticas

**Propósito:** Cuando un objeto cambia, notificar automáticamente a los interesados.

**Implementación:** `VentasSubject` notifica a paneles de dashboard e inventario.

```
                    ┌────────────────────┐
                    │   VentasSubject    │  ◄── Singleton
                    │     (Subject)      │
                    ├────────────────────┤
                    │ - observadores[]   │
                    ├────────────────────┤
                    │ + agregarObservador│
                    │ + notificarNuevaVen│
                    │ + notificarCambioEs│
                    └─────────┬──────────┘
                              │ notifica
            ┌─────────────────┼─────────────────┐
            ▼                 ▼                 ▼
┌────────────────────┐ ┌────────────────────┐ ┌────────────────────┐
│ DashboardObserver  │ │InventarioObserver  │ │ LogisticaPanel     │
├────────────────────┤ ├────────────────────┤ ├────────────────────┤
│ actualizar(mensaje)│ │ actualizar(mensaje)│ │ actualizar(mensaje)│
│ "Actualiza gráficos│ │ "Recarga inventario│ │ "Refresca tabla"   │
└────────────────────┘ └────────────────────┘ └────────────────────┘
```

**Código clave:**
```java
// 1. Registrar observadores
VentasSubject.getInstance().agregarObservador(new DashboardObserver("Panel Principal"));
VentasSubject.getInstance().agregarObservador(new InventarioObserver());

// 2. Cuando hay una venta, todos se enteran automáticamente
VentasSubject.getInstance().notificarNuevaVenta(pedidoId, 150.00);

// Salida:
// [Dashboard] NUEVA_VENTA: Pedido #5 por S/.150.0
// [Inventario] NUEVA_VENTA: Pedido #5 por S/.150.0
```

**¿Por qué usarlo?**
- Los paneles se actualizan sin que el código de ventas los conozca
- Agregar nuevos observadores sin modificar el código existente

---

### 5.5 📦 STATE - Comportamiento según Estado

**Propósito:** Cambiar el comportamiento de un objeto según su estado interno.

**Implementación:** Estados del pedido (Pendiente → Pagado → Enviado → Entregado).

```
                    ┌────────────────────┐
                    │    EstadoPedido    │  ◄── Interface
                    ├────────────────────┤
                    │ + siguienteEstado()│
                    │ + cancelar()       │
                    │ + puedeModificarse │
                    └─────────┬──────────┘
                              │
     ┌────────────────────────┼────────────────────────┐
     ▼                        ▼                        ▼
┌──────────────┐      ┌──────────────┐      ┌──────────────┐
│PendienteState│ ───► │ PagadoState  │ ───► │ EnviadoState │ ───► EntregadoState
├──────────────┤      ├──────────────┤      ├──────────────┤
│cancelar: ✅  │      │cancelar: ❌  │      │cancelar: ❌  │
│modificar: ✅ │      │modificar: ❌ │      │modificar: ❌ │
└──────────────┘      └──────────────┘      └──────────────┘
```

**Flujo de estados:**
```
PENDIENTE ──(pago ok)──► PAGADO ──(enviado)──► ENVIADO ──(entregado)──► ENTREGADO
    │                                                                       │
    └──(cancelar)──► CANCELADO                                              │
                                                          (no se puede cancelar)
```

**Código clave:**
```java
// En la entidad Pedido:
public void avanzarEstado() {
    this.estadoActual.siguienteEstado(this);
}

// En PendienteState:
public void siguienteEstado(Pedido pedido) {
    pedido.setEstadoActual(new PagadoState());
    pedido.setEstado("PAGADO");
    System.out.println("Pedido -> PAGADO");
}

// Uso:
pedido.avanzarEstado(); // PENDIENTE -> PAGADO
pedido.avanzarEstado(); // PAGADO -> ENVIADO
pedido.avanzarEstado(); // ENVIADO -> ENTREGADO
```

**¿Por qué usarlo?**
- Evitar múltiples if-else para verificar estados
- Cada estado tiene su propia lógica encapsulada

---

### 5.6 🎭 FACADE - Simplificar Operaciones Complejas

**Propósito:** Proveer una interfaz simple para un sistema complejo.

**Implementación:** `OrderFacade` simplifica todo el proceso de checkout.

```
┌──────────────────────────────────────────────────────────────────────┐
│                           OrderFacade                                 │
│                    realizarCompra(usuario, carrito, pago)             │
└──────────────────────────────────┬───────────────────────────────────┘
                                   │
     ┌─────────────────────────────┼─────────────────────────────────┐
     │                             │                                 │
     ▼                             ▼                                 ▼
┌──────────┐              ┌───────────────┐               ┌───────────────┐
│ Verificar│              │   Aplicar     │               │   Procesar    │
│  Stock   │              │  Decorators   │               │     Pago      │
│          │              │ (extras)      │               │  (Factory)    │
└──────────┘              └───────────────┘               └───────────────┘
     │                             │                                 │
     ▼                             ▼                                 ▼
┌──────────┐              ┌───────────────┐               ┌───────────────┐
│ Descontar│              │    Crear      │               │   Notificar   │
│  Stock   │              │   Pedido      │               │  (Observer)   │
└──────────┘              └───────────────┘               └───────────────┘
```

**Sin Facade (código complejo):**
```java
// El controlador tendría que hacer TODO esto:
verificarUsuario();
verificarStock();
aplicarDecorators();
crearProcesadorPago();
validarPago();
procesarPago();
descontarStock();
crearPedido();
guardarPedido();
enviarNotificaciones();
notificarObservadores();
```

**Con Facade (código simple):**
```java
// El controlador solo llama un método:
Pedido pedido = orderFacade.realizarCompra(usuarioId, carrito, "PAYPAL", "EXPRESS", "Av Lima 123");
```

**¿Por qué usarlo?**
- El controlador no necesita conocer todos los subsistemas
- Cambios internos no afectan al código cliente

---

## 6. FLUJO DE COMPRA (CHECKOUT)

### Diagrama de Secuencia

```
 Cliente          Controller         OrderFacade        Services           Patterns
    │                  │                  │                 │                  │
    │ POST /checkout   │                  │                 │                  │
    │─────────────────>│                  │                 │                  │
    │                  │ realizarCompra() │                 │                  │
    │                  │─────────────────>│                 │                  │
    │                  │                  │                 │                  │
    │                  │                  │ buscarUsuario() │                  │
    │                  │                  │────────────────>│                  │
    │                  │                  │                 │                  │
    │                  │                  │ verificarStock()│                  │
    │                  │                  │────────────────>│                  │
    │                  │                  │                 │                  │
    │                  │                  │        Aplicar Decorators         │
    │                  │                  │───────────────────────────────────>│
    │                  │                  │        (Bordado, Estampado)        │
    │                  │                  │                 │                  │
    │                  │                  │     PaymentFactory.crear("PAYPAL") │
    │                  │                  │───────────────────────────────────>│
    │                  │                  │                 │                  │
    │                  │                  │ procesarPago()  │                  │
    │                  │                  │───────────────────────────────────>│
    │                  │                  │                 │                  │
    │                  │                  │ guardarPedido() │                  │
    │                  │                  │────────────────>│                  │
    │                  │                  │                 │                  │
    │                  │                  │ pedido.avanzarEstado()             │
    │                  │                  │───────────────────────────────────>│
    │                  │                  │        (State: PENDIENTE->PAGADO)  │
    │                  │                  │                 │                  │
    │                  │                  │  VentasSubject.notificar()         │
    │                  │                  │───────────────────────────────────>│
    │                  │                  │        (Observer notifica paneles) │
    │                  │                  │                 │                  │
    │                  │    Pedido        │                 │                  │
    │                  │<─────────────────│                 │                  │
    │   Response 200   │                  │                 │                  │
    │<─────────────────│                  │                 │                  │
```

---

## 7. DIAGRAMAS UML

### 7.1 Diagrama de Clases - Patrones

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              DIAGRAMA DE CLASES                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

╔═══════════════════════════════════════════════════════════════════════════════╗
║                               SINGLETON PATTERN                                ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  ┌─────────────────────────┐      ┌─────────────────────────────────┐         ║
║  │     AdminSession        │      │    AdminDatabaseConnection      │         ║
║  ├─────────────────────────┤      ├─────────────────────────────────┤         ║
║  │ -adminLogueado: Admin   │      │ -connection: Connection         │         ║
║  │ -sesionActiva: boolean  │      ├─────────────────────────────────┤         ║
║  ├─────────────────────────┤      │ +getInstance(): AdminDBConn     │         ║
║  │ +getInstance(): Session │      │ +getConnection(): Connection    │         ║
║  │ +iniciarSesion(Admin)   │      │ +cerrarConexion(): void         │         ║
║  │ +cerrarSesion(): void   │      └─────────────────────────────────┘         ║
║  └─────────────────────────┘                                                  ║
╚═══════════════════════════════════════════════════════════════════════════════╝

╔═══════════════════════════════════════════════════════════════════════════════╗
║                               FACTORY PATTERN                                  ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                       ┌─────────────────────────┐                             ║
║                       │    PaymentFactory       │                             ║
║                       ├─────────────────────────┤                             ║
║                       │ +crearProcesador(tipo)  │                             ║
║                       └────────────┬────────────┘                             ║
║                                    │ creates                                  ║
║                      ┌─────────────┴─────────────┐                            ║
║                      ▼                           ▼                            ║
║         ┌──────────────────────┐    ┌──────────────────────┐                  ║
║         │ «interface»          │    │                      │                  ║
║         │ IProcesadorPago      │◄───┤ TarjetaProcesador    │                  ║
║         ├──────────────────────┤    ├──────────────────────┤                  ║
║         │ +procesarPago(monto) │    │ PayPalProcesador     │                  ║
║         │ +validarDatos()      │    ├──────────────────────┤                  ║
║         │ +getNombre()         │    │ ContraentregaProc.   │                  ║
║         └──────────────────────┘    └──────────────────────┘                  ║
╚═══════════════════════════════════════════════════════════════════════════════╝

╔═══════════════════════════════════════════════════════════════════════════════╗
║                              DECORATOR PATTERN                                 ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║              ┌─────────────────────────┐                                      ║
║              │ «interface»             │                                      ║
║              │ IProductoComponente     │                                      ║
║              ├─────────────────────────┤                                      ║
║              │ +getPrecio(): Double    │                                      ║
║              │ +getDescripcion(): Str  │                                      ║
║              │ +getExtras(): String    │                                      ║
║              └───────────┬─────────────┘                                      ║
║                          │                                                    ║
║          ┌───────────────┴───────────────┐                                    ║
║          ▼                               ▼                                    ║
║  ┌───────────────────┐        ┌─────────────────────────┐                     ║
║  │   ProductoBase    │        │ «abstract»              │                     ║
║  ├───────────────────┤        │ ProductoDecorator       │                     ║
║  │ -producto         │        ├─────────────────────────┤                     ║
║  └───────────────────┘        │ #productoDecorado       │                     ║
║                               └───────────┬─────────────┘                     ║
║                                           │                                   ║
║             ┌─────────────────────────────┼─────────────────────────────┐     ║
║             ▼                             ▼                             ▼     ║
║  ┌───────────────────┐      ┌───────────────────┐      ┌───────────────────┐  ║
║  │ BordadoDecorator  │      │EstampadoDecorator │      │EmpaqueRegaloDecor.│  ║
║  ├───────────────────┤      ├───────────────────┤      ├───────────────────┤  ║
║  │ COSTO = S/20      │      │ COSTO = S/15      │      │ COSTO = S/10      │  ║
║  │ -textoBordado     │      │ -disenoEstampado  │      │ -mensajeRegalo    │  ║
║  └───────────────────┘      └───────────────────┘      └───────────────────┘  ║
╚═══════════════════════════════════════════════════════════════════════════════╝

╔═══════════════════════════════════════════════════════════════════════════════╗
║                              OBSERVER PATTERN                                  ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║        ┌─────────────────────────┐       ┌─────────────────────────┐          ║
║        │     VentasSubject       │       │ «interface»             │          ║
║        │     (Singleton)         │       │ VentasObserver          │          ║
║        ├─────────────────────────┤       ├─────────────────────────┤          ║
║        │ -observadores: List     │──────>│ +actualizar(mensaje)    │          ║
║        ├─────────────────────────┤       │ +getNombre(): String    │          ║
║        │ +agregarObservador()    │       └───────────┬─────────────┘          ║
║        │ +notificarNuevaVenta()  │                   │                        ║
║        │ +notificarCambioEstado()│                   │ implements             ║
║        └─────────────────────────┘       ┌───────────┴───────────┐            ║
║                                          ▼                       ▼            ║
║                             ┌──────────────────┐    ┌──────────────────┐      ║
║                             │DashboardObserver │    │InventarioObserver│      ║
║                             └──────────────────┘    └──────────────────┘      ║
╚═══════════════════════════════════════════════════════════════════════════════╝

╔═══════════════════════════════════════════════════════════════════════════════╗
║                                STATE PATTERN                                   ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║                      ┌─────────────────────────┐                              ║
║                      │ «interface»             │                              ║
║                      │ EstadoPedido            │                              ║
║                      ├─────────────────────────┤                              ║
║                      │ +siguienteEstado(Pedido)│                              ║
║                      │ +cancelar(Pedido): bool │                              ║
║                      │ +puedeModificarse(): bool│                             ║
║                      └───────────┬─────────────┘                              ║
║                                  │                                            ║
║     ┌──────────────┬─────────────┼─────────────┬──────────────┐               ║
║     ▼              ▼             ▼             ▼              ▼               ║
║ ┌────────┐   ┌────────┐   ┌────────┐   ┌────────┐   ┌────────────┐            ║
║ │PENDIENTE│──>│ PAGADO │──>│ENVIADO │──>│ENTREGADO│  │ CANCELADO  │            ║
║ └────────┘   └────────┘   └────────┘   └────────┘   └────────────┘            ║
║  cancelar:✅  cancelar:❌  cancelar:❌  cancelar:❌                             ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## 8. ESTRUCTURA DEL PROYECTO

```
📁 proyecto/
├── 📁 src/main/java/com/dpatrones/proyecto/
│   │
│   ├── 📄 ProyectoApplication.java          # Punto de entrada Spring Boot
│   │
│   ├── 📁 config/
│   │   └── 📄 DataInitializer.java          # Carga datos de prueba
│   │
│   ├── 📁 controller/                       # API REST
│   │   ├── 📄 ProductoController.java       # CRUD productos
│   │   ├── 📄 PedidoController.java         # Gestión pedidos
│   │   ├── 📄 UsuarioController.java        # Gestión usuarios
│   │   ├── 📄 AdminController.java          # Login admin
│   │   └── 📄 DashboardController.java      # Estadísticas
│   │
│   ├── 📁 model/                            # Entidades JPA
│   │   ├── 📄 Producto.java
│   │   ├── 📄 Pedido.java                   # Usa State Pattern
│   │   ├── 📄 DetallePedido.java
│   │   ├── 📄 Usuario.java
│   │   └── 📄 Admin.java
│   │
│   ├── 📁 repository/                       # Acceso a datos
│   │   ├── 📄 ProductoRepository.java
│   │   ├── 📄 PedidoRepository.java
│   │   ├── 📄 UsuarioRepository.java
│   │   ├── 📄 AdminRepository.java
│   │   └── 📄 DetallePedidoRepository.java
│   │
│   ├── 📁 service/                          # Lógica de negocio
│   │   ├── 📄 ProductoService.java
│   │   ├── 📄 PedidoService.java
│   │   ├── 📄 UsuarioService.java
│   │   ├── 📄 AdminService.java
│   │   └── 📄 NotificacionService.java
│   │
│   ├── 📁 patterns/                         # ⭐ PATRONES DE DISEÑO
│   │   │
│   │   ├── 📁 singleton/                    # 🔒 SINGLETON
│   │   │   ├── 📄 AdminSession.java         # Sesión única de admin
│   │   │   └── 📄 AdminDatabaseConnection.java  # Conexión única BD
│   │   │
│   │   ├── 📁 factory/                      # 🏭 FACTORY
│   │   │   ├── 📄 IProcesadorPago.java      # Interface
│   │   │   ├── 📄 PaymentFactory.java       # Fábrica
│   │   │   ├── 📄 TarjetaProcesador.java    # Producto concreto
│   │   │   ├── 📄 PayPalProcesador.java     # Producto concreto
│   │   │   └── 📄 ContraentregaProcesador.java  # Producto concreto
│   │   │
│   │   ├── 📁 decorator/                    # 🎨 DECORATOR
│   │   │   ├── 📄 IProductoComponente.java  # Interface
│   │   │   ├── 📄 ProductoBase.java         # Componente concreto
│   │   │   ├── 📄 ProductoDecorator.java    # Decorador abstracto
│   │   │   ├── 📄 BordadoDecorator.java     # +S/20
│   │   │   ├── 📄 EstampadoDecorator.java   # +S/15
│   │   │   └── 📄 EmpaqueRegaloDecorator.java  # +S/10
│   │   │
│   │   ├── 📁 observer/                     # 👁️ OBSERVER
│   │   │   ├── 📄 VentasObserver.java       # Interface Observer
│   │   │   ├── 📄 VentasSubject.java        # Subject (Singleton)
│   │   │   ├── 📄 DashboardObserver.java    # Observador concreto
│   │   │   └── 📄 InventarioObserver.java   # Observador concreto
│   │   │
│   │   ├── 📁 state/                        # 📦 STATE
│   │   │   ├── 📄 EstadoPedido.java         # Interface State
│   │   │   ├── 📄 PendienteState.java       # Estado inicial
│   │   │   ├── 📄 PagadoState.java          # Después del pago
│   │   │   ├── 📄 EnviadoState.java         # En camino
│   │   │   └── 📄 EntregadoState.java       # Estado final
│   │   │
│   │   └── 📁 facade/                       # 🎭 FACADE
│   │       └── 📄 OrderFacade.java          # Simplifica checkout
│   │
│   └── 📁 swing/                            # UI Administrador
│       ├── 📄 AdminFrame.java               # Ventana principal
│       └── 📄 LogisticaPanel.java           # Panel de logística
│
├── 📁 src/main/resources/
│   └── 📄 application.properties            # Configuración BD
│
├── 📄 pom.xml                               # Dependencias Maven
├── 📄 docker-compose.yml                    # Docker MySQL
└── 📄 DOCUMENTACION.md                      # Este archivo
```

---

## 9. CÓMO EJECUTAR EL PROYECTO

### Requisitos Previos
- ☕ Java 21
- 🐬 MySQL 8.0 (o Docker)
- 📦 Maven (incluido como wrapper)

### Paso 1: Configurar la Base de Datos

**Opción A - Con Docker:**
```bash
docker-compose up -d
```

**Opción B - MySQL local:**
```sql
CREATE DATABASE proyecto_db;
```

### Paso 2: Configurar `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### Paso 3: Ejecutar la Aplicación

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Paso 4: Probar la API

```bash
# Ver productos
curl http://localhost:8080/api/productos

# Ver pedidos
curl http://localhost:8080/api/pedidos
```

---

## 10. GLOSARIO DE TÉRMINOS

| Término | Definición |
|---------|------------|
| **Patrón de Diseño** | Solución reutilizable a problemas comunes de desarrollo |
| **Singleton** | Clase con una única instancia global |
| **Factory** | Crea objetos sin exponer la lógica de creación |
| **Decorator** | Añade responsabilidades a objetos dinámicamente |
| **Observer** | Notifica cambios a múltiples objetos automáticamente |
| **State** | Cambia comportamiento según estado interno |
| **Facade** | Interfaz simple para un sistema complejo |
| **JPA** | Java Persistence API - mapeo objeto-relacional |
| **DTO** | Data Transfer Object - objeto para transferir datos |
| **Repository** | Patrón que abstrae el acceso a datos |
| **Service** | Capa que contiene la lógica de negocio |
| **Controller** | Recibe peticiones HTTP y retorna respuestas |
| **Entity** | Clase que representa una tabla en la BD |
| **Spring Boot** | Framework para crear aplicaciones Java rápidamente |
| **Lombok** | Librería que genera código automáticamente (@Data, @Builder) |

---

## 📝 NOTAS FINALES

### ¿Por qué estos patrones?

| Patrón | Problema que Resuelve |
|--------|----------------------|
| **Singleton** | Necesitamos UNA sola sesión de admin y UNA conexión a BD |
| **Factory** | Diferentes métodos de pago sin if-else largo |
| **Decorator** | Combinar extras (bordado+estampado) flexiblemente |
| **Observer** | Paneles que se actualizan automáticamente |
| **State** | Estados del pedido con comportamientos distintos |
| **Facade** | Simplificar el proceso de checkout de 10 pasos a 1 |

### Principios SOLID aplicados

- **S**ingle Responsibility: Cada clase tiene una responsabilidad
- **O**pen/Closed: Abierto a extensión, cerrado a modificación (Factory, Decorator)
- **L**iskov Substitution: Los estados pueden sustituirse entre sí
- **I**nterface Segregation: Interfaces pequeñas y específicas
- **D**ependency Inversion: Dependemos de abstracciones (interfaces)

---

**Equipo LLAMA - UTP 2025** 🎓
