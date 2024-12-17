package com.txt.stock.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.txt.base.domain.Order;
import com.txt.stock.domain.Product;
import com.txt.stock.repository.ProductRepository;

@Service
public class OrderManageService {

    private static final String SOURCE = "stock";
    private static final Logger LOG = LoggerFactory.getLogger(OrderManageService.class);
    private ProductRepository productRepository;
    private KafkaTemplate<Long, Order> kafkaTemplate;

    @Value("${topic.stock}")
    private String stockTopic;

    public OrderManageService(ProductRepository productRepository, KafkaTemplate<Long, Order> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reserve(Order order) {
        Product product = productRepository.findById(order.getProductId()).orElseThrow();
        LOG.info("Found: {}", product);
        if (order.getStatus().equals("NEW")) {
            if (order.getProductCount() < product.getAvailableItems()) {
                product.setReservedItems(product.getReservedItems() + order.getProductCount());
                product.setAvailableItems(product.getAvailableItems() - order.getProductCount());
                order.setStatus("ACCEPT");
                productRepository.save(product);
            } else {
                order.setStatus("REJECT");
            }
            kafkaTemplate.send(stockTopic, order.getId(), order);
            LOG.info("Sent: {}", order);
        }
    }

    public void confirm(Order order) {
        Product product = productRepository.findById(order.getProductId()).orElseThrow();
        LOG.info("Found: {}", product);
        if (order.getStatus().equals("CONFIRMED")) {
            product.setReservedItems(product.getReservedItems() - order.getProductCount());
            productRepository.save(product);
        } else if (order.getStatus().equals("ROLLBACK") && !order.getSource().equals(SOURCE)) {
            product.setReservedItems(product.getReservedItems() - order.getProductCount());
            product.setAvailableItems(product.getAvailableItems() + order.getProductCount());
            productRepository.save(product);
        }
    }

}
