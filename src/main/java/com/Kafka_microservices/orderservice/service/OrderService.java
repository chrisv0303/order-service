package com.Kafka_microservices.orderservice.service;

import com.Kafka_microservices.orderservice.dto.OrderLineItemsDto;
import com.Kafka_microservices.orderservice.dto.OrderRequest;
import com.Kafka_microservices.orderservice.dto.ProductResponse;
import com.Kafka_microservices.orderservice.exceptions.OrderNotFoundException;
import com.Kafka_microservices.orderservice.model.Order;
import com.Kafka_microservices.orderservice.model.OrderLineItems;
import com.Kafka_microservices.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        ProductResponse[] productResponseArray = webClient.get()
                .uri("http://localhost:8081/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(productResponseArray)
                .allMatch(ProductResponse::isInStock);
        if(allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock!");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    public Iterable<Order> retrieveAllOrders() {
        return orderRepository.findAll();
    }

    public Order retrieveOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
        -> new OrderNotFoundException("Order " + orderId + " not found."));
    }
}
