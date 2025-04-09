package tn.esprit.ordreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ordreservice.entity.Order;


public interface OrderRepository extends JpaRepository<Order, Integer> {
}
