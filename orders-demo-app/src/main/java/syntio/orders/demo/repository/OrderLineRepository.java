package syntio.orders.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syntio.orders.demo.model.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    OrderLine findOrderLineById(Integer orderLineId);
}