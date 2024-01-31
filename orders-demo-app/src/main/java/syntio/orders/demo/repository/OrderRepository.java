package syntio.orders.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syntio.orders.demo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}