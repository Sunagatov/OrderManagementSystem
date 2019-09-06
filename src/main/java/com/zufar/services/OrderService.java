package com.zufar.services;

import com.zufar.exceptions.OrderNotFoundException;
import com.zufar.models.Order;
import com.zufar.repositories.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrder(Long orderId) throws OrderNotFoundException {
        return this.orderRepository.findById(orderId).orElseThrow(() -> {
            final String errorMessage = "Updating an order is impossible. The order with id=" + orderId + " is not found";
            OrderNotFoundException exception = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, exception);
            return exception;
        });
    }

    public Collection<Order> getOrders() {
        return (Collection<Order>)this.orderRepository.findAll();
    }
    
    public void deleteOrder(Long orderId) throws OrderNotFoundException {
        if (!this.orderRepository.existsById(orderId)) {
            final String errorMessage = "Deleting an order is impossible. The order with id=" + orderId + " is not found";
            OrderNotFoundException exception = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        this.orderRepository.deleteById(orderId);
    }

    public Order saveOrder(Order order) {
        if (order == null) {
            final String errorMessage = "Saving an order is impossible. An order is absent.";
            IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        return this.orderRepository.save(order);
    }

    public Order updateOrder(Order order) throws OrderNotFoundException {
        if (order == null || order.getId() == null) {
            final String errorMessage = "Updating an order is impossible. An orderId is absent.";
            IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        final Long orderId = order.getId();
        if (!this.orderRepository.existsById(orderId)) {
            final String errorMessage = "Updating an order is impossible. The order with id=" + orderId + " is not found";
            OrderNotFoundException exception = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        return this.orderRepository.save(order);
    }
}
