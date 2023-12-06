package syntio.publisher.outbox.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import syntio.publisher.outbox.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByPurchaserContaining(String purchaser);
}