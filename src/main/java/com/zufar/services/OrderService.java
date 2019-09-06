package com.zufar.services;

import com.zufar.dto.OrderDTO;
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
        return (Collection<Order>) this.orderRepository.findAll();
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

    public OrderDTO saveOrder(OrderDTO orderDTO) {
        if (orderDTO == null) {
            final String errorMessage = "Saving an orderDTO is impossible. An orderDTO is absent.";
            IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        Order order = OrderService.convertToOrder(orderDTO);
        order = this.orderRepository.save(order);
        return OrderService.convertToOrderDTO(order);
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) throws OrderNotFoundException {
        if (orderDTO == null || orderDTO.getId() == null) {
            final String errorMessage = "Updating an order is impossible. An orderId is absent.";
            IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        final Long orderId = orderDTO.getId();
        if (!this.orderRepository.existsById(orderId)) {
            final String errorMessage = "Updating an order is impossible. The order with id=" + orderId + " is not found";
            OrderNotFoundException exception = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, exception);
            throw exception;
        }
        Order order = OrderService.convertToOrder(orderDTO);
        order = this.orderRepository.save(order);
        return OrderService.convertToOrderDTO(order);
    }

    private static Order convertToOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(order.getId());
        order.setTitle(order.getTitle());
        order.setCustomer(order.getCustomer());
        order.setStatus(order.getStatus());
        order.setCreationDate(order.getCreationDate());
        order.setLastModifiedDate(order.getLastModifiedDate());
        order.setOrderItems(order.getOrderItems());
        return order;
    }

    private static OrderDTO convertToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTitle(order.getTitle());
        orderDTO.setCustomer(order.getCustomer());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCreationDate(order.getCreationDate());
        orderDTO.setLastModifiedDate(order.getLastModifiedDate());
        orderDTO.setOrderItems(order.getOrderItems());
        return orderDTO;
    }
}
