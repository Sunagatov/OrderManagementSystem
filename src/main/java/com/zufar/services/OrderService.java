package com.zufar.services;

import com.zufar.exceptions.OrderNotFoundException;
import com.zufar.models.Order;
import com.zufar.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
public class OrderService {
    
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrder(Long orderId) throws OrderNotFoundException {
        return this.orderRepository
                .findById(orderId).orElseThrow(() -> new OrderNotFoundException("An order with id = " + orderId));
    }

    public Collection<Order> getOrders() {
        return (Collection<Order>)this.orderRepository.findAll();
    }
    
    public void deleteOrder(Long orderId) throws OrderNotFoundException {
        if (!this.orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException("An order with id = " + orderId);
        }
        this.orderRepository.deleteById(orderId);
    }

    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public Order updateOrder(Order order) throws OrderNotFoundException {
        if (order == null || order.getId() == null) {
            throw new IllegalArgumentException();
        }
        if (!this.orderRepository.existsById(order.getId())) {
            throw new OrderNotFoundException("An order with id = " + order.getId());
        }
        return this.orderRepository.save(order);
    }
}
