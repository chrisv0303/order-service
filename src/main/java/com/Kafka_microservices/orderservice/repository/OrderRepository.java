package com.Kafka_microservices.orderservice.repository;

import com.Kafka_microservices.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
