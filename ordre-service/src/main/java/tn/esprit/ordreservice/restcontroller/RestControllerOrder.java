package tn.esprit.ordreservice.restcontroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ordreservice.entity.Order;
import tn.esprit.ordreservice.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class RestControllerOrder {
     private final OrderService service;

    public RestControllerOrder(OrderService service) {
        this.service = service;
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Integer> createOrder(@RequestBody @Valid Order order) {
        return ResponseEntity.ok(service.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        return ResponseEntity.ok(service.findAllOrders());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<Order> findById(  @PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(service.findById(orderId));
    }



}
