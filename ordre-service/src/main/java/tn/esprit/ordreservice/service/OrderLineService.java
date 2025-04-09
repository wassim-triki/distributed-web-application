package tn.esprit.ordreservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.ordreservice.entity.OrderLine;
import tn.esprit.ordreservice.repository.OrderLineRepository;

import java.util.List;

@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }
    @Transactional
    // Ajouter une orderLine directement depuis l'entité
    public OrderLine saveOrderLine(OrderLine orderLine) {
        return orderLineRepository.save(orderLine);
    }

    // Récupérer toutes les orderLines associées à une commande (orderId)
    public List<OrderLine> findAllByOrderId(Integer orderId) {
        return orderLineRepository.findAllByOrderId(orderId);
    }

    // Supprimer une orderLine
    public void deleteOrderLine(Integer id) {
        orderLineRepository.deleteById(id);
    }

    // Récupérer une orderLine par ID
    public OrderLine findById(Integer id) {
        return orderLineRepository.findById(id).orElse(null);
    }
}
