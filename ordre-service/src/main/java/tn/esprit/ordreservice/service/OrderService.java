package tn.esprit.ordreservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ordreservice.entity.Order;
import tn.esprit.ordreservice.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;

    @Autowired  // Injection explicite via constructeur
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Integer createOrder(Order order) {
        // Étape 1 : lier chaque OrderLine à l'Order parent
        if (order.getOrderLines() != null) {
            order.getOrderLines().forEach(line -> line.setOrder(order));
        }

        // Étape 2 : Calcul du total de la commande
        BigDecimal totalAmount = order.getTotalAmount();
        System.out.println("✅ Montant total de la commande : " + totalAmount);

        // (Optionnel) Tu peux aussi stocker totalAmount dans un champ Order s'il est persisté

        // Étape 3 : Sauvegarder la commande
        var savedOrder = repository.save(order);
        return savedOrder.getId();
    }

    public List<Order> findAllOrders() {
        return repository.findAll();
    }

    public Order findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No order found with ID: " + id));
    }
}
