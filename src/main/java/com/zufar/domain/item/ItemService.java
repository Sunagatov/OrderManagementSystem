package com.zufar.domain.item;

import com.zufar.domain.product.Product;
import com.zufar.dto.ItemDTO;
import com.zufar.dto.ProductDTO;
import com.zufar.exception.ItemNotFoundException;
import com.zufar.exception.StatusNotFoundException;
import com.zufar.domain.product.ProductService;
import com.zufar.service.DaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService implements DaoService<ItemDTO> {

    private static final Logger LOGGER = LogManager.getLogger(ItemService.class);
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Collection<ItemDTO> getAll() {
        return ((Collection<Item>) this.itemRepository.findAll())
                .stream()
                .map(ItemService::convertToOrderItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDTO> getAll(String sortBy) {
        return ((Collection<Item>) this.itemRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(ItemService::convertToOrderItemDTO)
                .collect(Collectors.toList());
    }

    public ItemDTO getById(Long id) {
        Item orderEntity = this.itemRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The orderItem with id = " + id + " not found.";
            ItemNotFoundException itemNotFoundException = new ItemNotFoundException(errorMessage);
            LOGGER.error(errorMessage, itemNotFoundException);
            return itemNotFoundException;
        });
        return ItemService.convertToOrderItemDTO(orderEntity);
    }

    public ItemDTO save(ItemDTO item) {
        Item itemEntity = ItemService.convertToOrderItem(item);
        itemEntity = this.itemRepository.save(itemEntity);
        return ItemService.convertToOrderItemDTO(itemEntity);
    }

    public ItemDTO update(ItemDTO item) {
        this.isExists(item.getId());
        Item itemEntity = ItemService.convertToOrderItem(item);
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

    public static ItemDTO convertToOrderItemDTO(Item item) {
        Objects.requireNonNull(item, "There is no orderItem to convert.");
        ItemDTO orderItemDTO = new ItemDTO();
        orderItemDTO.setId(item.getId());
        final ProductDTO product = ProductService.convertToProductDTO(item.getProduct());
        orderItemDTO.setProduct(product);
        orderItemDTO.setQuantity(item.getQuantity());
        return orderItemDTO;
    }

    public static Item convertToOrderItem(ItemDTO item) {
        Objects.requireNonNull(item, "There is no item to convert.");
        Item itemEntity = new Item();
        itemEntity.setId(item.getId());
        final Product product = ProductService.convertToProduct(item.getProduct());
        itemEntity.setProduct(product);
        itemEntity.setQuantity(item.getQuantity());
        return itemEntity;
    }
}
