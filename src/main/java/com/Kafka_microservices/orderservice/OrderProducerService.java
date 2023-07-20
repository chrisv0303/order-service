package com.Kafka_microservices.orderservice;

import com.Kafka_microservices.orderservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void placeOrder(Order order) {
        OrderEvent orderEvent = new OrderEvent(order.getId(), "ORDER_PLACED");
        kafkaTemplate.send("order-events", orderEvent);
    }
}
