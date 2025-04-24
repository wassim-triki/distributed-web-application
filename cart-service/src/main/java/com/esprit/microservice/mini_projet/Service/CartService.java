package com.esprit.microservice.mini_projet.Service;
import com.esprit.microservice.mini_projet.Entities.Cart;
import com.esprit.microservice.mini_projet.Entities.CartItem;
import com.esprit.microservice.mini_projet.Repository.CartItemRepository;
import com.esprit.microservice.mini_projet.Repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setTotalPrice(0.0);
                    return cartRepository.save(newCart);
                });
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }


    public CartItem addToCart(Long userId, CartItem cartItem) {
        Cart cart = getOrCreateCart(userId);
        cartItem.setUserId(userId);
        CartItem savedItem = cartItemRepository.save(cartItem);
        cart.getItems().add(savedItem);
        updateCartTotal(cart);
        cartRepository.save(cart);
        return savedItem;
    }

    public void removeFromCart(Long userId, Long itemId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        updateCartTotal(cart);
        cartRepository.save(cart);
        cartItemRepository.deleteById(itemId);
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> items = cart.getItems();
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
        for (CartItem item : items) {
            cartItemRepository.deleteById(item.getId());
        }
    }

    public Cart updateCart(Cart cart) {
        updateCartTotal(cart);
        return cartRepository.save(cart);
    }

    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
        List<CartItem> items = cart.getItems();
        for (CartItem item : items) {
            cartItemRepository.deleteById(item.getId());
        }
        cartRepository.deleteById(cartId);
    }

    public void addItemToCart(Cart cart, CartItem item) {
        cart.getItems().add(item);
        updateCartTotal(cart);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(Cart cart, CartItem item) {
        cart.getItems().remove(item);
        updateCartTotal(cart);
        cartRepository.save(cart);
    }

    public void updateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double discount = total * (cart.getDiscountPercentage() / 100);
        total -= discount;

        cart.setTotalPrice(total);
    }


    public double getCartTotal(Long userId) {
        Cart cart = getCartByUserId(userId);
        return cart.getTotalPrice();
    }

    public CartItem updateItemQuantity(Long userId, Long itemId, int newQuantity) {
        Cart cart = getCartByUserId(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        item.setQuantity(newQuantity);
        CartItem updatedItem = cartItemRepository.save(item);
        updateCartTotal(cart);
        cartRepository.save(cart);
        return updatedItem;
    }
    public void applyPromoCode(Long userId, String promoCode) {
        Cart cart = getOrCreateCart(userId);

        switch (promoCode.toUpperCase()) {
            case "ABC":
                cart.setDiscountPercentage(10.0);
                break;
            case "DEF":
                cart.setDiscountPercentage(20.0);
                break;
            default:
                cart.setDiscountPercentage(0.0);
                throw new RuntimeException("Invaid promo code");
        }

        updateCartTotal(cart);
        cartRepository.save(cart);
    }

}
