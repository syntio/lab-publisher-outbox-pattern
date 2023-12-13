package syntio.publisher.outbox.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.List;
import java.util.Map;
import syntio.publisher.outbox.demo.model.Order;
import syntio.publisher.outbox.demo.model.OrderLines;
import syntio.publisher.outbox.demo.repository.OrderRepository;
import syntio.publisher.outbox.demo.repository.OrderLinesRepository;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLinesRepository orderLinesRepository;

    private final Timestamp unsetTimestamp = Timestamp.valueOf("1000-01-01 00:00:00.0");

    public void createOrder(Order order) {
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        order.setCreatedAt(currentTimestamp);
        order.setUpdatedAt(unsetTimestamp);
        order.setDeletedAt(unsetTimestamp);
        List<OrderLines> orderLines = order.getOrderLines();
        orderLines.forEach(orderLine -> {
            orderLine.setCreatedAt(OffsetDateTime.now());
            orderLine.setDeletedAt(unsetTimestamp);
            orderLine.setUpdatedAt(unsetTimestamp);
        });
        orderRepository.save(order);
    }

    public void createOrder(String purchaser, String paymentMethod, OffsetDateTime createdAt, boolean isActive, List<Map<String, Object>> orderItems) {

        // Create Order
        Order order = new Order();
        order.setPurchaser(purchaser);
        order.setPaymentMethod(paymentMethod);
        order.setCreatedAt(Timestamp.from(createdAt.toInstant()));
        //order.setUpdatedAt(updatedAt);
        //order.setDeletedAt(deletedAt);
        order.setIsActive(isActive);

        // Format the OffsetDateTime with the desired format including UTC offset
        String formattedTimestamp = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ"));
        order.setCreatedAt(Timestamp.from(createdAt.toInstant()));

        orderRepository.save(order);

        // Create Order Lines
        for (Map<String, Object> orderLineData : orderItems) {
            OrderLines orderLines = new OrderLines();
            orderLines.setProduct((String) orderLineData.get("product"));
            orderLines.setQuantity((Integer) orderLineData.get("quantity"));
            orderLines.setPrice((double) orderLineData.get("price"));
            orderLines.setIsActive(isActive);
            orderLines.setCreatedAt(createdAt);
            orderLines.setOrder(order);
            orderLinesRepository.save(orderLines);
        }

        /* order.setOrderLines(orderLinesList);
        for (OrderLines orderItems : orderLinesList) {
            OrderLines orderLines = new OrderLines();
            orderLines.setProduct(orderItems.getProduct());
            orderLines.setQuantity(orderItems.getQuantity());
            orderLines.setPrice(orderItems.getPrice());
            orderLines.setOrder(order);
            orderLinesRepository.save(orderLines);
        } */
    }
}
