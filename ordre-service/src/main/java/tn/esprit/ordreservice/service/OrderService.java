package tn.esprit.ordreservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
        order.setTotalAmount(totalAmount);

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

    // Méthode planifiée pour appliquer le solde
   // @Scheduled(cron = "0 0 2 * * *") // chaque jour à 2h du matin
    @Scheduled(fixedRate = 10000) // toutes les 10 secondes
    @Transactional
    public void applyDiscountToOrders() {
        List<Order> orders = findAllOrders();

        for (Order order : orders) {
            // Vérifier si la commande n'a pas déjà été soldée
            if (Boolean.FALSE.equals(order.isDiscounted())) {
                // Calculer le montant total de la commande
                BigDecimal initialAmount = order.getTotalAmount(); // Calcul basé sur les orderLines
                order.setInitialAmount(initialAmount); // Sauvegarder le montant initial dans la base de données

                // Log pour vérifier le montant avant réduction
                System.out.println("Montant initial pour la commande ID " + order.getId() + " : " + initialAmount);

                // Si le montant total de la commande est supérieur à 100, appliquer la réduction
                if (initialAmount.compareTo(new BigDecimal("100")) > 0) {
                    // Appliquer la réduction de 20% (par exemple)
                    BigDecimal discountAmount = initialAmount.multiply(new BigDecimal("0.20")); // 20% de réduction
                    BigDecimal finalAmount = initialAmount.subtract(discountAmount); // Montant après réduction

                    // Mettre à jour les champs discount, finalAmount et totalAmount
                    order.setDiscount(new BigDecimal("0.20")); // 20% de réduction
                    order.setFinalAmount(finalAmount); // Montant final après réduction
                    order.setTotalAmount(finalAmount); // Total après réduction
                    order.setDiscounted(true); // Marquer la commande comme soldée

                    // Sauvegarder la commande après modification
                    repository.save(order);

                    // Log pour indiquer que le solde a été appliqué
                    System.out.println("✅ Solde appliqué à la commande ID : " + order.getId() +
                            "\nMontant avant réduction : " + initialAmount +
                            "\nMontant après réduction : " + finalAmount);
                } else {
                    System.out.println("❌ La commande ID : " + order.getId() + " n'atteint pas le seuil de réduction.");
                }
            } else {
                // Si la commande est déjà soldée, on log un message
                BigDecimal initialAmount = order.getInitialAmount();
                BigDecimal discountedAmount = order.getFinalAmount();

                System.out.println("❌ La commande ID : " + order.getId() + " a déjà été soldée." +
                        "\nMontant avant réduction : " + initialAmount +
                        "\nMontant après réduction : " + discountedAmount);
            }
        }
    }


}
