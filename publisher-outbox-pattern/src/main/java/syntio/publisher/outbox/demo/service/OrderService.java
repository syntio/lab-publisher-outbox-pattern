package syntio.publisher.outbox.demo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import syntio.publisher.outbox.demo.model.Order;
import syntio.publisher.outbox.demo.model.OrderLine;
import syntio.publisher.outbox.demo.repository.OrderLinesRepository;
import syntio.publisher.outbox.demo.repository.OrderRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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

        order.setCreatedAt(OffsetDateTime.now());
        order.setUpdatedAt(null);
        order.setDeletedAt(null);
        order.setIsActive(true);
        List<OrderLine> orderLines = order.getOrderLines();
        orderLines.forEach(orderLine -> {
            orderLine.setCreatedAt(OffsetDateTime.now());
            orderLine.setDeletedAt(null);
            orderLine.setUpdatedAt(null);
            orderLine.setIsActive(true);
            orderLine.setOrder(order);
        });
        orderRepository.save(order);
    }

    public void updateOrder(Order updatedOrder) {
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        Optional<Order> existingOrder = orderRepository.findById(updatedOrder.getOrderId());

        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setUpdatedAt(OffsetDateTime.now());
            List<OrderLine> updatedOrderLines = updatedOrder.getOrderLines();
            List<OrderLine> existingOrderLines = order.getOrderLines();

            existingOrderLines.removeIf(orderLine -> !updatedOrderLines.contains(orderLine));
            for (OrderLine orderLine : updatedOrderLines) {
                orderLine.setOrder(order);
                int index = existingOrderLines.indexOf(orderLine);
                if (index != -1) {
                    existingOrderLines.set(index, orderLine);
                } else {
                    existingOrderLines.add(orderLine);
                }
            }
            orderRepository.save(order);
        }
    }

    public void deleteById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order existingOrder = order.get();
            existingOrder.setIsActive(false);
            List<OrderLine> orderLines = existingOrder.getOrderLines();
            orderLines.forEach(orderLine -> {
                orderLine.setIsActive(false);
                orderLine.setOrder(existingOrder);
            });
            orderRepository.save(existingOrder);
        }
    }


   /* public void createOrder(String purchaser, String paymentMethod, OffsetDateTime createdAt, boolean isActive, List<Map<String, Object>> orderItems) {

        // Create Order
        Order order = new Order();
        order.setPurchaser(purchaser);
        order.setPaymentMethod(paymentMethod);
        //order.setCreatedAt(Timestamp.from(createdAt.toInstant()));
        //order.setUpdatedAt(updatedAt);
        //order.setDeletedAt(deletedAt);
        order.setIsActive(isActive);

        // Format the OffsetDateTime with the desired format including UTC offset
        //String formattedTimestamp = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ"));
        //order.setCreatedAt(Timestamp.from(createdAt.toInstant()));

        orderRepository.save(order);

        // Create Order Lines
        for (Map<String, Object> orderLineData : orderItems) {
            OrderLine orderLines = new OrderLine();
            orderLines.setProduct((String) orderLineData.get("product"));
            orderLines.setQuantity((Integer) orderLineData.get("quantity"));
            orderLines.setPrice((double) orderLineData.get("price"));
            orderLines.setIsActive(isActive);
            orderLines.setCreatedAt(createdAt);
            orderLines.setOrder(order);
            orderLinesRepository.save(orderLines);
        }

         order.setOrderLines(orderLinesList);
        for (OrderLines orderItems : orderLinesList) {
            OrderLines orderLines = new OrderLines();
            orderLines.setProduct(orderItems.getProduct());
            orderLines.setQuantity(orderItems.getQuantity());
            orderLines.setPrice(orderItems.getPrice());
            orderLines.setOrder(order);
            orderLinesRepository.save(orderLines);
        } */

}
