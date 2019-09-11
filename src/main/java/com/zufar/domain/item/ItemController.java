package com.zufar.domain.item;

import com.zufar.dto.OrderItemDTO;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "orderItems", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ItemController {

    private final DaoService<OrderItemDTO> orderItemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.orderItemService = itemService;
    }

    @GetMapping(value = "/all")
    public @ResponseBody Collection<OrderItemDTO> getOrderItems() {
        return this.orderItemService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody  OrderItemDTO getOrderItem(@PathVariable Long id) {
        return this.orderItemService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteOrderItem(@PathVariable Long id) {
        this.orderItemService.getById(id);
    }

    @PostMapping
    public @ResponseBody OrderItemDTO saveOrderItem(@RequestBody OrderItemDTO orderItem) {
        return this.orderItemService.save(orderItem);
    }

    @PutMapping
    public @ResponseBody OrderItemDTO updateOrderItem(@RequestBody OrderItemDTO orderItem) {
        return  this.orderItemService.update(orderItem);
    }
}
