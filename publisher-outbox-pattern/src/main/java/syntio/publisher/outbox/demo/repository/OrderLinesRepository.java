package syntio.publisher.outbox.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import syntio.publisher.outbox.demo.model.OrderLines;

public interface OrderLinesRepository extends JpaRepository<OrderLines, Integer> {
}