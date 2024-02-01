package syntio.orders.demo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import syntio.orders.demo.model.Order;
import syntio.orders.demo.repository.OrderLineRepository;
import syntio.orders.demo.repository.OrderRepository;
import syntio.orders.demo.model.OrderLine;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    public Order createOrder(Order order) {
        // Get the current date and time in UTC
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // Adjust the offset to be one hour earlier
        OffsetDateTime adjustedTime = now.minusHours(1);

        order.setCreatedAt(adjustedTime);
        order.setUpdatedAt(null);
        order.setDeletedAt(null);
        order.setIsActive(true);

        List<OrderLine> orderLines = order.getOrderLines();
        orderLines.forEach(orderLine -> {
            orderLine.setCreatedAt(adjustedTime);
            orderLine.setDeletedAt(null);
            orderLine.setUpdatedAt(null);
            orderLine.setIsActive(true);
            orderLine.setOrder(order);
        });

        return orderRepository.save(order);
    }

    public Order updateOrder(@PathVariable("orderId") Integer id, Order updatedOrder) throws IllegalArgumentException {
        // Get the current date and time in UTC
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // Adjust the offset to be one hour earlier
        OffsetDateTime adjustedTime = now.minusHours(1);

        Order existingOrderOptional = orderRepository.findById(id).orElse(null);
        if (existingOrderOptional == null) {
            throw new IllegalArgumentException(String.format("Order with id %d does not exist", id));
        }

        existingOrderOptional.setUpdatedAt(adjustedTime);
        existingOrderOptional.setPurchaser(updatedOrder.getPurchaser());
        existingOrderOptional.setPaymentMethod(updatedOrder.getPaymentMethod());

        List<OrderLine> updatedOrderLines = new ArrayList<>();

        for (OrderLine lineUpdateRequest : updatedOrder.getOrderLines()) {
            OrderLine existingLine = orderLineRepository.findOrderLineById(lineUpdateRequest.getId());
            if (existingLine == null) {
                continue;
            }

            if (lineUpdateRequest.getProduct() != null) {
                existingLine.setProduct(lineUpdateRequest.getProduct());
            }
            if (lineUpdateRequest.getQuantity() != null) {
                existingLine.setQuantity(lineUpdateRequest.getQuantity());
            }
            existingLine.setPrice(lineUpdateRequest.getPrice());
            existingLine.setUpdatedAt(adjustedTime);
            updatedOrderLines.add(existingLine);
        }

        existingOrderOptional.setOrderLines(updatedOrderLines);

        return orderRepository.save(existingOrderOptional);
    }

    public void deleteById(Integer id) throws IllegalArgumentException {
        // Get the current date and time in UTC
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // Adjust the offset to be one hour earlier
        OffsetDateTime adjustedTime = now.minusHours(1);

        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new IllegalArgumentException(String.format("Order with id %d doesn't exist", id));
        }

        Order existingOrder = order.get();
        existingOrder.setIsActive(false);
        existingOrder.setDeletedAt(adjustedTime);

        List<OrderLine> orderLines = existingOrder.getOrderLines();
        orderLines.forEach(orderLine -> {
            orderLine.setIsActive(false);
            orderLine.setDeletedAt(adjustedTime);
            orderLine.setOrder(existingOrder);
        });

        orderRepository.save(existingOrder);
    }

}
