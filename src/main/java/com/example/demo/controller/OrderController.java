package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class OrderController {

  @Autowired
  OrderRepository orderRepository;

  @GetMapping("/orders")
  public ResponseEntity<List<Order>> getAllOrders(@RequestParam(required = false) String purchaser) {
    try {
      List<Order> orders = new ArrayList<Order>();

      if (purchaser == null)
        orderRepository.findAll().forEach(orders::add);
      else
        orderRepository.findByPurchaserContaining(purchaser).forEach(orders::add);

      if (orders.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(orders, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/orders/{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable("id") long id) {
    Optional<Order> orderData = orderRepository.findById(id);

    if (orderData.isPresent()) {
      return new ResponseEntity<>(orderData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/orders")
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    try {
      Order _order = orderRepository
          .save(new Order(order.getPurchaser(), order.getQuantity(), order.getProduct(), order.getCreated_at(), order.getOrderId(), order.getProducer(), order.getCountry(), order.getEmail()));
      return new ResponseEntity<>(_order, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/orders/{id}")
  public ResponseEntity<Order> updateOrder(@PathVariable("id") long id, @RequestBody Order order) {
    Optional<Order> orderData = orderRepository.findById(id);

    if (orderData.isPresent()) {
      Order _order = orderData.get();
      _order.setPurchaser(order.getPurchaser());
      _order.setQuantity(order.getQuantity());
      _order.setProduct(order.getProduct());
      _order.setCreated_at(order.getCreated_at());
      _order.setOrderId(order.getOrderId());
      _order.setProducer(order.getProducer());
      _order.setCountry(order.getCountry());
      _order.setEmail(order.getEmail());
      return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/orders/{id}")
  public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") long id) {
    try {
      orderRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/orders")
  public ResponseEntity<HttpStatus> deleteAllOrders() {
    try {
      orderRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

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