# 🦙 TIENDA LLAMA - Sistema E-Commerce con Patrones de Diseño

**Proyecto de Patrones de Diseño - Universidad Tecnológica del Perú**

Sistema de e-commerce para tienda de ropa que implementa **6 patrones de diseño GoF** con dos interfaces:
- **Frontend Web (Thymeleaf)**: Catálogo, carrito y checkout para clientes
- **Panel Admin (Swing)**: Gestión de pedidos y logística

---

## 🎯 Patrones de Diseño Implementados

### 1. SINGLETON

**Propósito:** Garantizar que una clase tenga una única instancia y proporcionar un punto de acceso global.

**Implementación en el proyecto:**

| Clase | Ubicación | Función |
|-------|-----------|---------|
| `AdminSession` | `patterns/singleton/` | Mantiene la sesión única del administrador logueado |
| `VentasSubject` | `patterns/observer/` | Único Subject que gestiona todos los observadores |
| `AdminDatabaseConnection` | `patterns/singleton/` | Una sola conexión a BD para evitar saturación |

**Cómo se demuestra en Swing:**
```
Al abrir el panel Admin, se muestra en el header:
"Usuario: Admin | Sesión activa (Singleton)"

Solo puede haber UN admin logueado a la vez.
```

**Código clave:**
```java
// Constructor privado - nadie puede usar "new"
private AdminSession() { }

// Holder interno (Bill Pugh Singleton) - Thread-safe
private static class Holder {
    private static final AdminSession INSTANCE = new AdminSession();
}

// Único punto de acceso
public static AdminSession getInstance() {
    return Holder.INSTANCE;
}
```

---

### 2. FACTORY METHOD

**Propósito:** Definir una interfaz para crear objetos, pero dejar que las subclases decidan qué clase instanciar.

**Implementación en el proyecto:**

| Clase | Función |
|-------|---------|
| `PaymentFactory` | Fábrica que crea el procesador de pago correcto |
| `TarjetaProcesador` | Procesa pagos con tarjeta de crédito/débito |
| `PayPalProcesador` | Procesa pagos vía PayPal |
| `ContraentregaProcesador` | Procesa pagos al momento de entrega |

**Cómo se demuestra en el Front Web:**
```
En el checkout (carrito.html), el usuario selecciona:
- TARJETA → Se crea TarjetaProcesador
- PAYPAL → Se crea PayPalProcesador  
- CONTRAENTREGA → Se crea ContraentregaProcesador

La Factory decide qué clase instanciar según la selección.
```

**Código clave:**
```java
public static IProcesadorPago crearProcesador(String tipoPago) {
    return switch (tipoPago.toUpperCase()) {
        case "TARJETA" -> new TarjetaProcesador();
        case "PAYPAL" -> new PayPalProcesador();
        case "CONTRAENTREGA" -> new ContraentregaProcesador();
        default -> throw new IllegalArgumentException("Tipo no soportado");
    };
}
```

**Output en consola al comprar:**
```
[FACTORY] Procesando S/.89.90 con TARJETA...
[FACTORY] Tarjeta: ****1234
[FACTORY] Pago con tarjeta APROBADO.
```

---

### 3. DECORATOR

**Propósito:** Añadir responsabilidades adicionales a un objeto de forma dinámica, sin modificar su clase.

**Implementación en el proyecto:**

| Clase | Costo Extra | Función |
|-------|-------------|---------|
| `ProductoBase` | - | Envuelve el producto original |
| `EstampadoDecorator` | +S/15.00 | Añade estampado personalizado |
| `BordadoDecorator` | +S/25.00 | Añade bordado con texto |
| `EmpaqueRegaloDecorator` | +S/10.00 | Añade empaque de regalo |

**Cómo se demuestra en el Front Web:**
```
En index.html, cada producto tiene checkboxes:
☐ Estampado (+S/15)
☐ Bordado (+S/25)
☐ Empaque Regalo (+S/10)

Al agregar al carrito, los extras se acumulan al precio base.
Polo S/49.90 + Estampado + Bordado = S/89.90
```

**Cómo se demuestra en Swing:**
```
En LogisticaPanel, botón "Ver Detalles (Decorator)" muestra:

═══════════════════════════════════════
       DETALLES DEL PEDIDO #5
═══════════════════════════════════════
• Polo Básico x1
  Precio unitario: S/. 89.90
  ✨ EXTRAS (Decorator): Estampado,Bordado
  Costo extras: S/. 40.00
  Subtotal: S/. 89.90
═══════════════════════════════════════
```

**Código clave:**
```java
// Envolver producto base con decoradores
IProductoComponente producto = new ProductoBase(polo);  // S/49.90
producto = new EstampadoDecorator(producto);            // +S/15.00
producto = new BordadoDecorator(producto);              // +S/25.00
// producto.getPrecio() = S/89.90
```

---

### 4. OBSERVER

**Propósito:** Definir una dependencia uno-a-muchos para que cuando un objeto cambie, todos sus dependientes sean notificados.

**Implementación en el proyecto:**

| Clase | Rol | Función |
|-------|-----|---------|
| `VentasSubject` | Subject | Mantiene lista de observadores y los notifica |
| `VentasObserver` | Observer Interface | Contrato que implementan los paneles |
| `DashboardPanel` | Observer Concreto | Se actualiza cuando hay ventas |
| `LogisticaPanel` | Observer Concreto | Se actualiza cuando cambian pedidos |
| `InventarioPanel` | Observer Concreto | Se actualiza cuando cambia stock |

**Cómo se demuestra en Swing:**
```
1. La barra de estado muestra: "Observadores: 2"
   (DashboardPanel y LogisticaPanel registrados)

2. Al hacer una compra desde el front web, aparece en el Log:
   [10:30:45] OBSERVER: NUEVA_VENTA: Pedido #5 por S/.89.90
   
3. Las tablas se recargan automáticamente sin refresh manual.

4. Botón "Simular Notificación" dispara manualmente el Observer.
```

**Código clave:**
```java
// Registrar observador
VentasSubject.getInstance().agregarObservador(this);

// Notificar a todos
VentasSubject.getInstance().notificarNuevaVenta(pedidoId, total);

// Cada observador recibe la notificación
@Override
public void actualizar(String mensaje) {
    agregarLog("OBSERVER: " + mensaje);
    cargarDatos(); // Refresca los datos automáticamente
}
```

---

### 5. STATE

**Propósito:** Permitir que un objeto altere su comportamiento cuando cambia su estado interno.

**Implementación en el proyecto:**

| Estado | Siguiente | ¿Cancelable? | ¿Modificable? |
|--------|-----------|--------------|---------------|
| `PendienteState` | PAGADO | ✅ Sí | ✅ Sí |
| `PagadoState` | ENVIADO | ❌ No | ❌ No |
| `EnviadoState` | ENTREGADO | ❌ No | ❌ No |
| `EntregadoState` | (final) | ❌ No | ❌ No |

**Flujo de estados:**
```
PENDIENTE → PAGADO → ENVIADO → ENTREGADO
```

**Cómo se demuestra en Swing:**
```
1. En LogisticaPanel, seleccionar un pedido
2. Click en "Avanzar Estado (State)"
3. El pedido cambia de estado automáticamente
4. Output en consola:
   [STATE] Pedido #5: PENDIENTE -> PAGADO
   
5. Si está en ENTREGADO y se intenta avanzar:
   [STATE] Pedido #5 ya entregado. Ciclo completado.
```

**Código clave:**
```java
// El pedido delega al estado actual
public void avanzarEstado() {
    estadoActual.siguienteEstado(this);
}

// Cada estado sabe a cuál transicionar
public class PendienteState implements EstadoPedido {
    @Override
    public void siguienteEstado(Pedido pedido) {
        pedido.setEstadoActual(new PagadoState());
        pedido.setEstado("PAGADO");
    }
}
```

---

### 6. FACADE

**Propósito:** Proporcionar una interfaz unificada para un conjunto de interfaces en un subsistema.

**Implementación en el proyecto:**

| Clase | Función |
|-------|---------|
| `OrderFacade` | Orquesta todo el proceso de checkout |

**Subsistemas que orquesta:**
1. `UsuarioService` - Verificar usuario
2. `ProductoService` - Verificar stock
3. **Decorator** - Aplicar extras al precio
4. **Factory** - Crear procesador de pago
5. `PedidoService` - Guardar pedido
6. `NotificacionService` - Enviar emails
7. **Observer** - Notificar a paneles Swing

**Cómo se demuestra:**
```
El Front Web solo hace UNA llamada:
POST /api/pedidos/checkout

El Facade internamente:
1. Valida usuario
2. Verifica stock de todos los productos
3. Aplica Decorators (extras) a cada producto
4. Crea procesador de pago (Factory)
5. Procesa el pago
6. Descuenta stock
7. Crea el pedido en BD
8. Envía notificaciones
9. Notifica a Observer (Swing se actualiza)
```

**Output en consola:**
```
========== INICIANDO CHECKOUT ==========
[CHECKOUT] Usuario: Juan Pérez
[CHECKOUT] Verificando inventario...
[CHECKOUT] Inventario OK
[CHECKOUT] Polo Básico + Estampado + Bordado x1 = S/.89.90
[FACTORY] Procesando S/.89.90 con TARJETA...
[FACTORY] Pago con tarjeta APROBADO.
[OBSERVER] Notificando a 2 observadores...
========== CHECKOUT COMPLETADO ==========
[RESULTADO] Pedido #5 creado exitosamente
[RESULTADO] Total: S/.89.90
```

---

## 🗂️ Estructura de Patrones

```
src/main/java/com/dpatrones/proyecto/patterns/
├── singleton/
│   ├── AdminSession.java          # Sesión única del admin
│   └── AdminDatabaseConnection.java # Conexión única a BD
├── factory/
│   ├── IProcesadorPago.java       # Interface del producto
│   ├── PaymentFactory.java        # Fábrica de procesadores
│   ├── TarjetaProcesador.java     # Producto concreto
│   ├── PayPalProcesador.java      # Producto concreto
│   └── ContraentregaProcesador.java # Producto concreto
├── decorator/
│   ├── IProductoComponente.java   # Interface componente
│   ├── ProductoBase.java          # Componente concreto
│   ├── ProductoDecorator.java     # Decorador abstracto
│   ├── EstampadoDecorator.java    # Decorador concreto
│   ├── BordadoDecorator.java      # Decorador concreto
│   └── EmpaqueRegaloDecorator.java # Decorador concreto
├── observer/
│   ├── VentasObserver.java        # Interface Observer
│   ├── VentasSubject.java         # Subject (Singleton)
│   ├── DashboardObserver.java     # Observer concreto
│   └── InventarioObserver.java    # Observer concreto
├── state/
│   ├── EstadoPedido.java          # Interface State
│   ├── PendienteState.java        # Estado concreto
│   ├── PagadoState.java           # Estado concreto
│   ├── EnviadoState.java          # Estado concreto
│   └── EntregadoState.java        # Estado concreto
└── facade/
    └── OrderFacade.java           # Fachada de checkout
```

---

## 🚀 Cómo Ejecutar

### Requisitos
- Java 21
- MySQL 8.0
- Maven

### Base de Datos
```sql
CREATE DATABASE proyecto_db;
```

### Configuración
Editar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto_db
spring.datasource.username=root
spring.datasource.password=tu_password
```

### Ejecutar
```bash
./mvnw spring-boot:run
```

### Acceder
- **Frontend Web**: http://localhost:8080/
- **Panel Admin (Swing)**: Se abre automáticamente

---

## 🧪 Cómo Probar Cada Patrón

### SINGLETON
1. Abrir Swing → Ver header con sesión única
2. La barra inferior muestra "Observadores: N"

### FACTORY
1. Frontend → Agregar producto al carrito
2. Checkout → Seleccionar método de pago (TARJETA/PAYPAL/CONTRAENTREGA)
3. Ver en consola qué procesador se creó

### DECORATOR
1. Frontend → Marcar checkboxes de extras en un producto
2. Agregar al carrito → Ver precio modificado
3. Swing → LogisticaPanel → Ver Detalles → Ver extras aplicados

### OBSERVER
1. Swing → LogisticaPanel → Ver log vacío
2. Frontend → Realizar una compra
3. Swing → Log muestra notificación automática
4. Las tablas se actualizan sin refresh

### STATE
1. Swing → LogisticaPanel → Seleccionar pedido
2. Click "Avanzar Estado"
3. Ver transición en consola: PENDIENTE→PAGADO→ENVIADO→ENTREGADO
4. Intentar avanzar un pedido ENTREGADO → Ver mensaje de ciclo completado

### FACADE
1. Frontend → Realizar checkout completo
2. Ver en consola cómo se orquestan todos los subsistemas
3. Un solo endpoint hace todo el trabajo

---

## 👥 Equipo

Universidad Tecnológica del Perú - Patrones de Diseño 2025
