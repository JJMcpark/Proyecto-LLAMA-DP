document.addEventListener("DOMContentLoaded", () => {
    const cartCountSpan = document.getElementById("cart-count");
    const addButtons = document.querySelectorAll(".btn-add-cart");
    const cartItemsContainer = document.getElementById("cart-items");
    const btnPay = document.getElementById("btn-pay");

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

    // ==== agregar productos desde index.html ====
    addButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const id = btn.dataset.id;
            const nombre = btn.dataset.nombre;
            const precio = parseFloat(btn.dataset.precio);

            let cart = getCart();
            const existing = cart.find(p => p.id === id);

            if (existing) {
                existing.quantity += 1;
            } else {
                cart.push({ id, nombre, precio, quantity: 1 });
            }

            saveCart(cart);
            alert("Producto agregado al carrito");
        });
    });

    // ==== mostrar carrito en carrito.html ====
    let totalGlobal = 0;

    if (cartItemsContainer) {
        const cart = getCart();

        if (cart.length === 0) {
            cartItemsContainer.innerHTML = "<p>Tu carrito está vacío.</p>";
        } else {
            let html = `
                <table class="tabla-carrito">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th>Precio</th>
                            <th>Cantidad</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            let total = 0;
            cart.forEach(item => {
                const subtotal = item.precio * item.quantity;
                total += subtotal;
                html += `
                    <tr>
                        <td>${item.nombre}</td>
                        <td>S/ ${item.precio.toFixed(2)}</td>
                        <td>${item.quantity}</td>
                        <td>S/ ${subtotal.toFixed(2)}</td>
                    </tr>
                `;
            });

            totalGlobal = total; // guardamos para usar en Pagar

            html += `
                    </tbody>
                </table>
                <h3>Total: S/ ${total.toFixed(2)}</h3>
            `;

            cartItemsContainer.innerHTML = html;
        }
    }

    // ==== botón Pagar ====
    if (btnPay) {
        btnPay.addEventListener("click", () => {
            const cart = getCart();
            if (cart.length === 0) {
                alert("Tu carrito está vacío");
                return;
            }

            // Llamada al backend
            fetch("/api/ventas", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ total: totalGlobal })
            })
                .then(resp => {
                    if (!resp.ok) {
                        throw new Error("Error al registrar la venta");
                    }
                    return resp.json();
                })
                .then(data => {
                    alert("Compra realizada. Nº de venta: " + data.id);
                    localStorage.removeItem("cart");
                    window.location.href = "/"; // volver al inicio
                })
                .catch(err => {
                    console.error(err);
                    alert("Hubo un problema al procesar el pago.");
                });
        });
    }

    updateCartCount();
});
