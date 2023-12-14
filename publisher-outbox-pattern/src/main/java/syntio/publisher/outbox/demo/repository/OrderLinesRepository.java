package syntio.publisher.outbox.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syntio.publisher.outbox.demo.model.OrderLine;

public interface OrderLinesRepository extends JpaRepository<OrderLine, Integer> {
}