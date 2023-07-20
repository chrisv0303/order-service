package com.Kafka_microservices.orderservice.service;

import com.Kafka_microservices.orderservice.exceptions.OrderNotFoundException;
import com.Kafka_microservices.orderservice.model.Order;
import com.Kafka_microservices.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Iterable<Order> retrieveAllOrders() {
        return orderRepository.findAll();
    }

    public Order retrieveOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
        -> new OrderNotFoundException("Order " + orderId + " not found."));
    }

    public Order orderToBeCreated(Order createAnOrder) {
        return orderRepository.save(createAnOrder);
    }
}
