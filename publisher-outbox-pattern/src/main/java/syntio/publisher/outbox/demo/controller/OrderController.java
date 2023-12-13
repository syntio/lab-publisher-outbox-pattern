package syntio.publisher.outbox.demo.controller;

import java.util.List;
import java.util.Map;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import syntio.publisher.outbox.demo.model.Order;
import syntio.publisher.outbox.demo.model.OrderLines;
import syntio.publisher.outbox.demo.repository.OrderRepository;
import syntio.publisher.outbox.demo.service.OrderService;
import syntio.publisher.outbox.demo.repository.OrderLinesRepository;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderLinesRepository orderLinesRepository;

    /* @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(required = false) String purchaser) {
        List<Order> orders = new ArrayList<>();
        try {
            if (purchaser == null || purchaser.isEmpty()) {
                orders.addAll(orderRepository.findAll());
            } else {
                orders.addAll(orderRepository.findByPurchaserContaining(purchaser));
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Integer id) {
        Optional<Order> orderData = orderRepository.findById(id);
        return orderData.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } */

    /*
    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> requestData) {
        String purchaser = (String) requestData.get("purchaser");
        String paymentMethod = (String) requestData.get("paymentMethod");
        String timestampString = (String) requestData.get("createdAt");
        // Timestamp updatedAt = Timestamp.valueOf((String)requestData.get("updatedAt"));
        // Timestamp deletedAt = Timestamp.valueOf((String)requestData.get("deletedAt"));
        Boolean isActive = (Boolean) requestData.get("isActive");

        // Parse the timestamp string to OffsetDateTime using a custom format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        OffsetDateTime createdAt = OffsetDateTime.parse(timestampString, formatter);

        List<Map<String, Object>> orderLines = (List<Map<String, Object>>) requestData.get("orderLines");

        orderService.createOrder(purchaser, paymentMethod, createdAt, isActive, orderLines);

        return ResponseEntity.ok("Order processed successfully");
    }*/


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

    /* @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") Integer id, @RequestBody Order order) {
        Optional<Order> orderData = orderRepository.findById(id);

        if (orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setPurchaser(order.getPurchaser());
            _order.setPaymentMethod(order.getPaymentMethod());
            _order.setCreatedAt(order.getCreatedAt());
            _order.setUpdatedAt(order.getUpdatedAt());
            _order.setDeletedAt(order.getDeletedAt());
            _order.setIsActive(order.getIsActive());
            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") Integer id) {
        try {
            orderRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
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
    } */

  /* @GetMapping("/orders/published")
  public ResponseEntity<List<Order>> findByPublished() {
    try {
      List<Order> orders = orderRepository.findByPublished(true);

      if (orders.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(orders, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  } */

}