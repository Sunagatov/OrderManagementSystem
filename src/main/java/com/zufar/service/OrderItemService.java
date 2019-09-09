package com.zufar.service;

import com.zufar.dto.OrderItemDTO;
import com.zufar.dto.ProductDTO;
import com.zufar.exception.OrderItemNotFoundException;
import com.zufar.exception.StatusNotFoundException;
import com.zufar.model.OrderItem;
import com.zufar.model.Product;
import com.zufar.repository.OrderItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderItemService implements DaoService<OrderItemDTO> {

    private static final Logger LOGGER = LogManager.getLogger(OrderItemService.class);
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public Collection<OrderItemDTO> getAll() {
        return ((Collection<OrderItem>) this.orderItemRepository.findAll())
                .stream()
                .map(OrderItemService::convertToOrderItemDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO getById(Long id) {
        OrderItem orderEntity = this.orderItemRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The orderItem with id = " + id + " not found.";
            OrderItemNotFoundException orderItemNotFoundException = new OrderItemNotFoundException(errorMessage);
            LOGGER.error(errorMessage, orderItemNotFoundException);
            return orderItemNotFoundException;
        });
        return OrderItemService.convertToOrderItemDTO(orderEntity);
    }

    public OrderItemDTO save(OrderItemDTO orderItem) {
        OrderItem orderItemEntity = OrderItemService.convertToOrderItem(orderItem);
        orderItemEntity = this.orderItemRepository.save(orderItemEntity);
        return OrderItemService.convertToOrderItemDTO(orderItemEntity);
    }

    public OrderItemDTO update(OrderItemDTO orderItem) {
        this.isExists(orderItem.getId());
        OrderItem orderItemEntity = OrderItemService.convertToOrderItem(orderItem);
        orderItemEntity = this.orderItemRepository.save(orderItemEntity);
        return OrderItemService.convertToOrderItemDTO(orderItemEntity);
    }

    public void deleteById(Long id) {
        this.isExists(id);
        this.orderItemRepository.deleteById(id);
    }

    public Boolean isExists(Long id) {
        if (!this.orderItemRepository.existsById(id)) {
            final String errorMessage = "The orderItem with id = " + id + " not found.";
            StatusNotFoundException statusNotFoundException = new StatusNotFoundException(errorMessage);
            LOGGER.error(errorMessage, statusNotFoundException);
            throw statusNotFoundException;
        }
        return true;
    }

    public static OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        UtilService.isObjectNull(orderItem, LOGGER, "There is no orderItem to convert.");
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        final ProductDTO product = ProductService.convertToProductDTO(orderItem.getProduct());
        orderItemDTO.setProduct(product);
        orderItemDTO.setQuantity(orderItem.getQuantity());
        return orderItemDTO;
    }

    public static OrderItem convertToOrderItem(OrderItemDTO orderItem) {
        UtilService.isObjectNull(orderItem, LOGGER, "There is no orderItem to convert.");
        OrderItem orderItemEntity = new OrderItem();
        orderItemEntity.setId(orderItem.getId());
        final Product product = ProductService.convertToProduct(orderItem.getProduct());
        orderItemEntity.setProduct(product);
        orderItemEntity.setQuantity(orderItem.getQuantity());
        return orderItemEntity;
    }
}
