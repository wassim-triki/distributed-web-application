package tn.esprit.ordreservice.restcontroller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ordreservice.entity.OrderLine;
import tn.esprit.ordreservice.service.OrderLineService;

import java.util.List;

@RestController
@RequestMapping("/orders-line")

public class RestControllerOrderLine {

    private final OrderLineService service;

    // Injection par constructeur
    public RestControllerOrderLine(OrderLineService service) {
        this.service = service;
    }

    // Ajouter une ligne de commande
    @PostMapping
    public ResponseEntity<OrderLine> saveOrderLine(@RequestBody OrderLine orderLine) {
        return ResponseEntity.ok(service.saveOrderLine(orderLine));
    }

    // Récupérer toutes les lignes pour un ordre donné
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderLine>> findAllByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.findAllByOrderId(orderId));
    }

    // Récupérer une ligne spécifique
    @GetMapping("/{id}")
    public ResponseEntity<OrderLine> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Supprimer une ligne
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderLine(@PathVariable Integer id) {
        service.deleteOrderLine(id);
        return ResponseEntity.noContent().build();
    }

}
