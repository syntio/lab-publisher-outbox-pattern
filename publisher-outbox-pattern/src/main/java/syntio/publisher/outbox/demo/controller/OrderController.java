package syntio.publisher.outbox.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import syntio.publisher.outbox.demo.model.Order;
import syntio.publisher.outbox.demo.repository.OrderRepository;
import syntio.publisher.outbox.demo.service.OrderService;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        try {
            orderService.createOrder(order);
            LOG.info("Created order: {}", order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> order = orderRepository.findAll();
            LOG.info("Listed orders: {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable("id") Integer id) {
        try {
            Optional<Order> order = orderRepository.findById(id);
            LOG.info("Listed order: {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer orderId, @RequestBody Order order) {
        try {
            orderService.updateOrder(orderId, order);
            LOG.info("Updated order: {}", order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            LOG.info("Order not found: {}", order);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/orders")
    public ResponseEntity<HttpStatus> deleteAllOrders() {
        try {
            orderRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") Integer id) {
        try {
            orderService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}