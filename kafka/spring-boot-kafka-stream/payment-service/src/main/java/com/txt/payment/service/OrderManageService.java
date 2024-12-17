package com.txt.payment.service;

import com.txt.payment.domain.Customer;
import com.txt.payment.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.txt.base.domain.Order;

@Service
public class OrderManageService {

    private static final String SOURCE = "payment";
    private static final Logger LOG = LoggerFactory.getLogger(OrderManageService.class);
    private CustomerRepository customerRepository;
    private KafkaTemplate<Long, Order> kafkaTemplate;

    @Value("${topic.payment}")
    private String paymentTopic;

    public OrderManageService(CustomerRepository customerRepository, KafkaTemplate<Long, Order> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void reserve(Order order) {
        Customer customer = customerRepository.findById(order.getCustomerId()).orElseThrow();
        LOG.info("Found: {}", customer);

        if (order.getPrice() < customer.getAmountAvailable()) {
            order.setStatus("ACCEPT");
            customer.setAmountReserved(customer.getAmountReserved() + order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getPrice());
        } else {
            order.setStatus("REJECT");
        }
        order.setSource(SOURCE);
        customerRepository.save(customer);
        kafkaTemplate.send(paymentTopic, order.getId(), order);
        LOG.info("Sent: {}", order);
    }

    public void confirm(Order order) {
        Customer customer = customerRepository.findById(order.getCustomerId()).orElseThrow();
        LOG.info("Found: {}", customer);

        if (order.getStatus().equals("CONFIRMED")) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customerRepository.save(customer);
        } else if (order.getStatus().equals("ROLLBACK") && !order.getSource().equals(SOURCE)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() + order.getPrice());
            customerRepository.save(customer);
        }
    }

}
