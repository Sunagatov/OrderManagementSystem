package com.zufar.order;

import com.zufar.user.UserService;
import com.zufar.item.Item;
import com.zufar.user.UserDTO;
import com.zufar.item.ItemDTO;
import com.zufar.status.StatusDTO;
import com.zufar.exception.OrderNotFoundException;
import com.zufar.user.User;
import com.zufar.status.Status;
import com.zufar.service.DaoService;
import com.zufar.item.ItemService;
import com.zufar.status.StatusService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
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

    @Override
    public Collection<OrderDTO> getAll() {
        return ((Collection<Order>) this.orderRepository.findAll())
                .stream()
                .map(OrderService::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<OrderDTO> getAll(String sortBy) {
        return ((Collection<Order>) this.orderRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(OrderService::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getById(Long id) {
        Order statusEntity = this.orderRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "Getting an order with id=" + id + " is impossible. There is no a sort attribute.";
            OrderNotFoundException orderNotFoundException = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderNotFoundException);
            return orderNotFoundException;
        });
        return OrderService.convertToOrderDTO(statusEntity);
    }

    @Override
    public OrderDTO save(OrderDTO order) {
        Order orderEntity = OrderService.convertToOrder(order);
        orderEntity = this.orderRepository.save(orderEntity);
        return OrderService.convertToOrderDTO(orderEntity);
    }

    @Override
    public OrderDTO update(OrderDTO order) {
        this.isExists(order.getId());
        Order orderEntity = OrderService.convertToOrder(order);
        orderEntity = this.orderRepository.save(orderEntity);
        return OrderService.convertToOrderDTO(orderEntity);
    }

    @Override
    public void deleteById(Long id) {
        this.isExists(id);
        this.orderRepository.deleteById(id);
    }

    @Override
    public Boolean isExists(Long id) {
        if (!this.orderRepository.existsById(id)) {
            final String errorMessage = "The order with id = " + id + " not found.";
            OrderNotFoundException orderNotFoundException = new OrderNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderNotFoundException);
            throw orderNotFoundException;
        }
        return true;
    }

    public OrderDTO updateStatus(StatusDTO status, Long id) {
        OrderDTO order = this.getById(id);
        order.setStatus(status);
        order = this.save(order);
        return order;
    }

    public static OrderDTO convertToOrderDTO(Order order) {
        Objects.requireNonNull(order, "There is no order to convert.");
        final StatusDTO status = StatusService.convertToStatusDTO(order.getStatus());
        final UserDTO user = UserService.convertToUserDTO(order.getUser());
        final Set<ItemDTO> items = order.getItems()
                .stream()
                .map(ItemService::convertToOrderItemDTO)
                .collect(Collectors.toSet());
        return new OrderDTO(
                order.getId(),
                order.getTitle(),
                order.getCreationDate(),
                order.getLastModifiedDate(),
                status,
                user,
                items
        );
    }

    public static Order convertToOrder(OrderDTO order) {
        Objects.requireNonNull(order, "There is no order to convert.");
        Set<ItemDTO> dtoItems = order.getItems();
        if (dtoItems == null) {
            final String errorMessage = "No order items in the order";
            final IllegalArgumentException illegalArgumentException = new IllegalArgumentException(errorMessage);
            LOGGER.error(errorMessage, illegalArgumentException);
            throw illegalArgumentException;
        }
        Set<Item> items = dtoItems
                .stream()
                .map(ItemService::convertToOrderItem)
                .collect(Collectors.toSet());
        final Status status = StatusService.convertToStatus(order.getStatus());
        final User user = UserService.convertToUser(order.getUser());
        return new Order(
                order.getId(),
                order.getTitle(),
                order.getCreationDate(),
                order.getLastModifiedDate(),
                status,
                user,
                items
        );
    }
}
