package com.zufar.domain.item;

import com.zufar.domain.product.Product;
import com.zufar.dto.OrderItemDTO;
import com.zufar.dto.ProductDTO;
import com.zufar.exception.ItemNotFoundException;
import com.zufar.exception.StatusNotFoundException;
import com.zufar.domain.product.ProductService;
import com.zufar.service.DaoService;
import com.zufar.service.UtilService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService implements DaoService<OrderItemDTO> {

    private static final Logger LOGGER = LogManager.getLogger(ItemService.class);
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Collection<OrderItemDTO> getAll() {
        return ((Collection<Item>) this.itemRepository.findAll())
                .stream()
                .map(ItemService::convertToOrderItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<OrderItemDTO> getAll(String sortBy) {
        return ((Collection<Item>) this.itemRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(ItemService::convertToOrderItemDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO getById(Long id) {
        Item orderEntity = this.itemRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The orderItem with id = " + id + " not found.";
            ItemNotFoundException itemNotFoundException = new ItemNotFoundException(errorMessage);
            LOGGER.error(errorMessage, itemNotFoundException);
            return itemNotFoundException;
        });
        return ItemService.convertToOrderItemDTO(orderEntity);
    }

    public OrderItemDTO save(OrderItemDTO orderItem) {
        Item itemEntity = ItemService.convertToOrderItem(orderItem);
        itemEntity = this.itemRepository.save(itemEntity);
        return ItemService.convertToOrderItemDTO(itemEntity);
    }

    public OrderItemDTO update(OrderItemDTO orderItem) {
        this.isExists(orderItem.getId());
        Item itemEntity = ItemService.convertToOrderItem(orderItem);
        itemEntity = this.itemRepository.save(itemEntity);
        return ItemService.convertToOrderItemDTO(itemEntity);
    }

    public void deleteById(Long id) {
        this.isExists(id);
        this.itemRepository.deleteById(id);
    }

    public Boolean isExists(Long id) {
        if (!this.itemRepository.existsById(id)) {
            final String errorMessage = "The orderItem with id = " + id + " not found.";
            StatusNotFoundException statusNotFoundException = new StatusNotFoundException(errorMessage);
            LOGGER.error(errorMessage, statusNotFoundException);
            throw statusNotFoundException;
        }
        return true;
    }

    public static OrderItemDTO convertToOrderItemDTO(Item item) {
        UtilService.isObjectNull(item, LOGGER, "There is no orderItem to convert.");
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(item.getId());
        final ProductDTO product = ProductService.convertToProductDTO(item.getProduct());
        orderItemDTO.setProduct(product);
        orderItemDTO.setQuantity(item.getQuantity());
        return orderItemDTO;
    }

    public static Item convertToOrderItem(OrderItemDTO orderItem) {
        UtilService.isObjectNull(orderItem, LOGGER, "There is no orderItem to convert.");
        Item itemEntity = new Item();
        itemEntity.setId(orderItem.getId());
        final Product product = ProductService.convertToProduct(orderItem.getProduct());
        itemEntity.setProduct(product);
        itemEntity.setQuantity(orderItem.getQuantity());
        return itemEntity;
    }
}
