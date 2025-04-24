package tn.esprit.ordreservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ordreservice.entity.OrderLine;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findAllByOrderId(int orderId);
}
