package syntio.publisher.outbox.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syntio.publisher.outbox.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}