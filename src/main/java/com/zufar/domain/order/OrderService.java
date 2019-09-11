package com.zufar.domain.order;

import com.zufar.domain.customer.CustomerService;
import com.zufar.domain.item.Item;
import com.zufar.dto.CustomerDTO;
import com.zufar.dto.OrderDTO;
import com.zufar.dto.OrderItemDTO;
import com.zufar.dto.StatusDTO;
import com.zufar.exception.OrderNotFoundException;
import com.zufar.domain.customer.Customer;
import com.zufar.domain.status.Status;
import com.zufar.service.DaoService;
import com.zufar.domain.item.ItemService;
import com.zufar.domain.status.StatusService;
import com.zufar.service.UtilService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService implements DaoService<OrderDTO> {

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Collection<OrderDTO> getAll() {
        return ((Collection<Order>) this.orderRepository.findAll())
                .stream()
                .map(OrderService::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public Collection<OrderDTO> getAll(String sortBy) {
        return ((Collection<Order>) this.orderRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(OrderService::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getById(Long id) {
        Order statusEntity = this.orderRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "Getting an order with id=" + id + " is impossible. There is no a sort attribute.";
            OrderNotFoundException orderNotFoundException = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderNotFoundException);
            return orderNotFoundException;
        });
        return OrderService.convertToOrderDTO(statusEntity);
    }

    public OrderDTO save(OrderDTO order) {
        Order orderEntity = OrderService.convertToOrder(order);
        orderEntity = this.orderRepository.save(orderEntity);
        return OrderService.convertToOrderDTO(orderEntity);
    }

    public OrderDTO update(OrderDTO order) {
        this.isExists(order.getId());
        Order orderEntity = OrderService.convertToOrder(order);
        orderEntity = this.orderRepository.save(orderEntity);
        return OrderService.convertToOrderDTO(orderEntity);
    }

    public OrderDTO updateStatus(StatusDTO status, Long id) {
        OrderDTO order = this.getById(id);
        order.setStatus(status);
        order = this.save(order);
        return order;
    }

    public void deleteById(Long id) {
        this.isExists(id);
        this.orderRepository.deleteById(id);
    }

    public Boolean isExists(Long id) {
        if (!this.orderRepository.existsById(id)) {
            final String errorMessage = "The order with id = " + id + " not found.";
            OrderNotFoundException orderNotFoundException = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderNotFoundException);
            throw orderNotFoundException;
        }
        return true;
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
        final Set<OrderItemDTO> orderItems = order.getItems()
                .stream()
                .map(ItemService::convertToOrderItemDTO)
                .collect(Collectors.toSet());
        orderDTO.setOrderItems(orderItems);
        return orderDTO;
    }

    public static Order convertToOrder(OrderDTO order) {
        UtilService.isObjectNull(order, LOGGER, "There is no order to convert.");
        Order orderEntity = new Order();
        Set<OrderItemDTO> orderItems = order.getOrderItems();
        if (orderItems == null) {
            final String errorMessage = "No order items in the order";
            final IllegalArgumentException illegalArgumentException = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, illegalArgumentException);
            throw illegalArgumentException;
        }
        Set<Item> orderEntityItems = orderItems
                .stream()
                .map(ItemService::convertToOrderItem)
                .collect(Collectors.toSet());
        orderEntity.setItems(orderEntityItems);
        orderEntity.setTitle(order.getTitle());
        final Status status = StatusService.convertToStatus(order.getStatus());
        orderEntity.setId(order.getId());
        orderEntity.setStatus(status);
        final Customer customerEntity = CustomerService.convertToCustomer(order.getCustomer());
        orderEntity.setCustomer(customerEntity);
        orderEntity.setCreationDate(order.getCreationDate());
        orderEntity.setLastModifiedDate(order.getLastModifiedDate());
        orderEntity.setId(order.getId());
        return orderEntity;
    }
}
