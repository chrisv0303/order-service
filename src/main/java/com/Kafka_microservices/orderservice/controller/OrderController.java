package com.Kafka_microservices.orderservice.controller;

import com.Kafka_microservices.orderservice.exceptions.OrderNotFoundException;
import com.Kafka_microservices.orderservice.model.Order;
import com.Kafka_microservices.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<Iterable<Order>> retrieveAllOrders() {
        Iterable<Order> ordersRetrieved = orderService.retrieveAllOrders();
        log.info("Retrieved all orders");
        return new ResponseEntity<>(ordersRetrieved, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> retrieveOrderById(@PathVariable Long orderId) {
        try {
            Order retrieveSpecificOrder = orderService.retrieveOrderById(orderId);
            log.info("Retrieved order #" + orderId);
            return new ResponseEntity<>(retrieveSpecificOrder, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            log.info("Order #" + orderId + " does not exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order #" + orderId + " does not exist.");
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> orderToBeCreated(@RequestBody Order createAnOrder) {
        Order createOrder = orderService.orderToBeCreated(createAnOrder);
        log.info("Successfully created an order");
        return new ResponseEntity<>(createOrder, HttpStatus.CREATED);
    }
}
