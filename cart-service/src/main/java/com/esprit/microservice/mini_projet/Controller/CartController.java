package com.esprit.microservice.mini_projet.Controller;

import com.esprit.microservice.mini_projet.Entities.Cart;
import com.esprit.microservice.mini_projet.Entities.CartItem;
import com.esprit.microservice.mini_projet.Service.CartService;
import com.esprit.microservice.mini_projet.Service.EmailDto;
import com.esprit.microservice.mini_projet.Service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final EmailService emailService;

    public CartController(CartService cartService, EmailService emailService) {
        this.cartService = cartService;
        this.emailService = emailService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> getCartTotal(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartTotal(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItem> addToCart(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        CartItem addedItem = cartService.addToCart(userId, cartItem);

        // If needed, you can send an email after adding an item
        // emailService.sendOrderConfirmationEmail(userEmail, orderId, totalAmount);

        return ResponseEntity.ok(addedItem);
    }

    @PutMapping("/{userId}/items/{itemId}/quantity")
    public ResponseEntity<CartItem> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, quantity));
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long userId, @RequestBody Cart cart) {
        if (!userId.equals(cart.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cartService.updateCart(cart));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Void> deleteCart(@PathVariable Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/confirm")
    public ResponseEntity<String> checkout(@PathVariable Long userId, @RequestBody EmailDto email) {

        // Retrieve the user's cart
        Cart cart = cartService.getCartByUserId(userId);

        // Check if the cart is empty
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("The cart is empty.");
        }

        // Generate order ID and total amount
        String orderId = "ORDER-" + cart.getId();
        String total = cart.getTotalPrice() + " DT";
        try {
            System.out.println(email);
            // Send a confirmation email with the order details
            emailService.sendOrderConfirmationEmail(email.getEmail(), orderId, total);
            return ResponseEntity.ok("Order confirmed, confirmation email sent.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error sending confirmation email.");
        }
    }

    @PostMapping("/{userId}/discount")
    public ResponseEntity<Cart> applyPromo(@PathVariable Long userId, @RequestParam String code) {
        try {
            cartService.applyPromoCode(userId, code);
            Cart updatedCart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
