package com.zufar.service;

import com.zufar.dto.CustomerDTO;
import com.zufar.dto.OrderDTO;
import com.zufar.dto.OrderItemDTO;
import com.zufar.dto.StatusDTO;
import com.zufar.exception.OrderNotFoundException;
import com.zufar.model.Customer;
import com.zufar.model.Order;
import com.zufar.model.OrderItem;
import com.zufar.model.Status;
import com.zufar.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Collection<OrderDTO> getAllOrders() {
        return ((Collection<Order>) this.orderRepository.findAll())
                .stream()
                .map(OrderService::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrder(Long id) {
        Order statusEntity = this.orderRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "Getting an order with id=" + id + " is impossible. There is no a sort attribute.";
            OrderNotFoundException orderNotFoundException = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderNotFoundException);
            return orderNotFoundException;
        });
        return OrderService.convertToOrderDTO(statusEntity);
    }

    public OrderDTO saveOrder(OrderDTO order) {
        Order orderEntity = OrderService.convertToOrder(order);
        orderEntity = this.orderRepository.save(orderEntity);
        return OrderService.convertToOrderDTO(orderEntity);
    }

    public OrderDTO updateOrder(OrderDTO status) {
        this.isOrderExists(status.getId());
        Order orderEntity = OrderService.convertToOrder(status);
        orderEntity = this.orderRepository.save(orderEntity);
        return OrderService.convertToOrderDTO(orderEntity);
    }

    public void deleteOrder(Long id) {
        this.isOrderExists(id);
        this.orderRepository.deleteById(id);
    }

    private void isOrderExists(Long id) {
        if (!this.orderRepository.existsById(id)) {
            final String errorMessage = "The order with id = " + id + " not found.";
            OrderNotFoundException orderNotFoundException = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderNotFoundException);
            throw orderNotFoundException;
        }
    }

    public static OrderDTO convertToOrderDTO(Order order) {
        UtilService.isObjectNull(order, LOGGER, "There is no order to convert.");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTitle(order.getTitle());
        final StatusDTO statusDTO = StatusService.convertToStatusDTO(order.getStatus());
        orderDTO.setStatus(statusDTO);
        orderDTO.setCreationDate(order.getCreationDate());
        orderDTO.setLastModifiedDate(order.getLastModifiedDate());
        orderDTO.setId(order.getId());
        final CustomerDTO customer = CustomerService.convertToCustomerDTO(order.getCustomer());
        orderDTO.setCustomer(customer);
        final Set<OrderItemDTO> orderItems = order.getOrderItems()
                .stream()
                .map(OrderItemService::convertToOrderItemDTO)
                .collect(Collectors.toSet());
        orderDTO.setOrderItems(orderItems);
        return orderDTO;
    }

    public static Order convertToOrder(OrderDTO order) {
        UtilService.isObjectNull(order, LOGGER, "There is no order to convert.");
        Order orderEntity = new Order();
        orderEntity.setTitle(order.getTitle());
        final Status status = StatusService.convertToStatus(order.getStatus());
        orderEntity.setId(order.getId());
        orderEntity.setStatus(status);
        final Customer customerEntity = CustomerService.convertToCustomer(order.getCustomer());
        orderEntity.setCustomer(customerEntity);
        orderEntity.setCreationDate(order.getCreationDate());
        orderEntity.setLastModifiedDate(order.getLastModifiedDate());
        orderEntity.setId(order.getId());
        Set<OrderItemDTO> orderItems = order.getOrderItems();
        if (orderItems == null) {
            final String errorMessage = "No order items in the order";
            final IllegalArgumentException illegalArgumentException = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, illegalArgumentException);
            throw illegalArgumentException;
        }
        Set<OrderItem> orderEntityItems = orderItems
                .stream()
                .map(OrderItemService::convertToOrderItem)
                .collect(Collectors.toSet());
        orderEntity.setOrderItems(orderEntityItems);
        return orderEntity;
    }
}
