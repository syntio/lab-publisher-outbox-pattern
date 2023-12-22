package syntio.publisher.outbox.demo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import syntio.publisher.outbox.demo.model.Order;
import syntio.publisher.outbox.demo.model.OrderLine;
import syntio.publisher.outbox.demo.repository.OrderLineRepository;
import syntio.publisher.outbox.demo.repository.OrderRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    public void createOrder(Order order) {
        // Get the current date and time in UTC
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // Format the timestamp as a string
        String formattedTimestamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println("Formatted Timestamp (UTC): " + formattedTimestamp);
        // Extract timezone offset in the format ±HH:mm
        String timezoneOffset = now.getOffset().toString();

        order.setCreatedAt(now);
        order.setUpdatedAt(null);
        order.setDeletedAt(null);
        order.setIsActive(true);
        List<OrderLine> orderLines = order.getOrderLines();
        orderLines.forEach(orderLine -> {
            orderLine.setCreatedAt(now);
            orderLine.setDeletedAt(null);
            orderLine.setUpdatedAt(null);
            orderLine.setIsActive(true);
            orderLine.setOrder(order);
        });
        orderRepository.save(order);
    }

    public ResponseEntity<Order> updateOrder(@PathVariable("orderId") Integer id, Order updatedOrder) {
        // Get the current date and time in UTC
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // Format the timestamp as a string
        String formattedTimestamp = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println("Formatted Timestamp (UTC): " + formattedTimestamp);
        // Extract timezone offset in the format ±HH:mm
        String timezoneOffset = now.getOffset().toString();

        Order existingOrderOptional = orderRepository.findById(id).orElse(null);

        if (existingOrderOptional == null) {
            return ResponseEntity.notFound().build();
        }

        existingOrderOptional.setUpdatedAt(now);
        existingOrderOptional.setPurchaser(updatedOrder.getPurchaser());
        existingOrderOptional.setPaymentMethod(updatedOrder.getPaymentMethod());

        List<OrderLine> updatedOrderLines = new ArrayList<>();
        for (OrderLine lineUpdateRequest : updatedOrder.getOrderLines()) {
            OrderLine existingLine = orderLineRepository.findOrderLineById(lineUpdateRequest.getId());
            if (existingLine != null) {
                existingLine.setProduct(lineUpdateRequest.getProduct());
                existingLine.setQuantity(lineUpdateRequest.getQuantity());
                existingLine.setPrice(lineUpdateRequest.getPrice());
                existingLine.setUpdatedAt(now);
                updatedOrderLines.add(existingLine);
            }
        }

        existingOrderOptional.setOrderLines(updatedOrderLines);
        Order newOrder = orderRepository.save(existingOrderOptional);
        return ResponseEntity.ok(newOrder);

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

}
