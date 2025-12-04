/**
 * ============================================================
 * TIENDA LLAMA - Frontend JavaScript
 * ============================================================
 * 
 * Este script maneja la interacción del cliente con la tienda:
 * - Agregar productos al carrito con extras (DECORATOR)
 * - Mostrar el carrito con detalles de personalización
 * - Realizar checkout usando el endpoint /api/pedidos/checkout (FACADE)
 * - El backend aplica: Factory (pago), Decorator (extras), Observer (notificaciones)
 * 
 * ============================================================
 */

document.addEventListener("DOMContentLoaded", () => {
    
    // ========== REFERENCIAS DOM ==========
    const cartCountSpan = document.getElementById("cart-count");
    const addButtons = document.querySelectorAll(".btn-add-cart");
    const cartItemsContainer = document.getElementById("cart-items");
    const btnPay = document.getElementById("btn-pay");
    const checkoutOptions = document.getElementById("checkout-options");

    // Precios de extras (deben coincidir con los Decorators del backend)
    const PRECIOS_EXTRAS = {
        'ESTAMPADO': 15.00,
        'BORDADO': 25.00,
        'EMPAQUE_REGALO': 10.00
    };

    // ========== FUNCIONES DE CARRITO ==========
    
    /**
     * Obtiene el carrito del localStorage
     * Estructura: [{ id, nombre, precioBase, extras: [], quantity }]
     */
    function getCart() {
        return JSON.parse(localStorage.getItem("cart")) || [];
    }

    function saveCart(cart) {
        localStorage.setItem("cart", JSON.stringify(cart));
        updateCartCount();
    }

    function updateCartCount() {
        if (!cartCountSpan) return;
        const cart = getCart();
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        cartCountSpan.textContent = totalItems;
    }

    /**
     * Calcula el precio de un item incluyendo extras (Decorator)
     */
    function calcularPrecioConExtras(precioBase, extras) {
        let precio = precioBase;
        extras.forEach(extra => {
            precio += PRECIOS_EXTRAS[extra] || 0;
        });
        return precio;
    }

    // ========== AGREGAR PRODUCTOS (index.html) ==========
    
    addButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.dataset.id;
            const nombre = btn.dataset.nombre;
            const precioBase = parseFloat(btn.dataset.precio);
            
            // Obtener extras seleccionados para este producto
            const productCard = btn.closest('.producto-card');
            const checkboxes = productCard.querySelectorAll('.extra-check:checked');
            const extras = Array.from(checkboxes).map(cb => cb.value);
            
            let cart = getCart();
            
            // Crear clave única para producto + extras
            const itemKey = id + '-' + extras.sort().join('-');
            const existing = cart.find(p => p.itemKey === itemKey);

            if (existing) {
                existing.quantity += 1;
            } else {
                cart.push({ 
                    id,
                    itemKey,
                    nombre, 
                    precioBase, 
                    extras,
                    quantity: 1 
                });
            }

            saveCart(cart);
            
            // Feedback al usuario
            let mensaje = `"${nombre}" agregado al carrito`;
            if (extras.length > 0) {
                mensaje += ` con: ${extras.join(', ')}`;
            }
            alert(mensaje);
            
            // Limpiar checkboxes
            checkboxes.forEach(cb => cb.checked = false);
        });
    });

    // ========== MOSTRAR CARRITO (carrito.html) ==========
    
    if (cartItemsContainer) {
        renderizarCarrito();
    }
    
    function renderizarCarrito() {
        const cart = getCart();

        if (cart.length === 0) {
            cartItemsContainer.innerHTML = `
                <div class="empty-cart">
                    <p>🛒 Tu carrito está vacío</p>
                    <a href="/" class="btn-secondary">Ver productos</a>
                </div>
            `;
            if (checkoutOptions) checkoutOptions.style.display = 'none';
            return;
        }
        
        // Mostrar opciones de checkout
        if (checkoutOptions) checkoutOptions.style.display = 'block';

        let html = `
            <table class="tabla-carrito">
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Extras (Decorator)</th>
                        <th>Precio Base</th>
                        <th>+ Extras</th>
                        <th>Precio Unit.</th>
                        <th>Cant.</th>
                        <th>Subtotal</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
        `;

        let total = 0;
        cart.forEach((item, index) => {
            const precioConExtras = calcularPrecioConExtras(item.precioBase, item.extras);
            const costoExtras = precioConExtras - item.precioBase;
            const subtotal = precioConExtras * item.quantity;
            total += subtotal;
            
            const extrasHtml = item.extras.length > 0 
                ? item.extras.map(e => `<span class="extra-tag">${e}</span>`).join(' ')
                : '<em>Sin extras</em>';
            
            html += `
                <tr>
                    <td><strong>${item.nombre}</strong></td>
                    <td>${extrasHtml}</td>
                    <td>S/ ${item.precioBase.toFixed(2)}</td>
                    <td>+ S/ ${costoExtras.toFixed(2)}</td>
                    <td>S/ ${precioConExtras.toFixed(2)}</td>
                    <td>
                        <button class="btn-qty" onclick="cambiarCantidad(${index}, -1)">-</button>
                        ${item.quantity}
                        <button class="btn-qty" onclick="cambiarCantidad(${index}, 1)">+</button>
                    </td>
                    <td><strong>S/ ${subtotal.toFixed(2)}</strong></td>
                    <td><button class="btn-remove" onclick="eliminarItem(${index})">🗑️</button></td>
                </tr>
            `;
        });

        html += `
                </tbody>
            </table>
            <div class="cart-total">
                <h2>Total: S/ ${total.toFixed(2)}</h2>
            </div>
        `;

        cartItemsContainer.innerHTML = html;
    }
    
    // Funciones globales para botones del carrito
    window.cambiarCantidad = function(index, delta) {
        let cart = getCart();
        cart[index].quantity += delta;
        if (cart[index].quantity <= 0) {
            cart.splice(index, 1);
        }
        saveCart(cart);
        renderizarCarrito();
    };
    
    window.eliminarItem = function(index) {
        let cart = getCart();
        cart.splice(index, 1);
        saveCart(cart);
        renderizarCarrito();
    };

    // ========== CHECKOUT (carrito.html) ==========
    
    if (btnPay) {
        btnPay.addEventListener("click", async () => {
            const cart = getCart();
            if (cart.length === 0) {
                alert("Tu carrito está vacío");
                return;
            }
            
            const metodoPago = document.getElementById("metodo-pago")?.value || "TARJETA";
            const metodoEnvio = document.getElementById("metodo-envio")?.value || "ESTANDAR";
            const direccionEnvio = document.getElementById("direccion-envio")?.value || "Sin especificar";
            
            if (!direccionEnvio || direccionEnvio.trim() === "" || direccionEnvio === "Sin especificar") {
                alert("Por favor ingresa una dirección de envío");
                return;
            }
            
            // Construir payload para el checkout (FACADE)
            const checkoutPayload = {
                usuarioId: 1, // Usuario demo; en producción vendría de la sesión
                items: cart.map(item => ({
                    productoId: parseInt(item.id),
                    cantidad: item.quantity,
                    extras: item.extras
                })),
                metodoPago: metodoPago,
                metodoEnvio: metodoEnvio,
                direccionEnvio: direccionEnvio
            };
            
            console.log("[CHECKOUT] Enviando:", checkoutPayload);
            
            try {
                const resp = await fetch("/api/pedidos/checkout", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(checkoutPayload)
                });
                
                const data = await resp.json();
                
                if (!resp.ok) {
                    throw new Error(data.error || "Error al procesar la compra");
                }
                
                // Éxito
                const pedido = data.pedido;
                alert(
                    `✅ ¡Compra realizada!\n\n` +
                    `Pedido #${pedido.id}\n` +
                    `Total: S/ ${pedido.total.toFixed(2)}\n` +
                    `Estado: ${pedido.estado}\n` +
                    `Código de seguimiento: ${pedido.codigoSeguimiento}\n\n` +
                    `Patrones aplicados:\n` +
                    `- Facade: Orquestó todo el proceso\n` +
                    `- Factory: Procesó pago con ${metodoPago}\n` +
                    `- Decorator: Aplicó extras a productos\n` +
                    `- Observer: Notificó al panel admin`
                );
                
                localStorage.removeItem("cart");
                window.location.href = "/";
                
            } catch (err) {
                console.error("[CHECKOUT] Error:", err);
                alert("Error: " + err.message);
            }
        });
    }

    // ========== INIT ==========
    updateCartCount();
});

