package com.zufar.controller;

import com.zufar.dto.OrderItemDTO;
import com.zufar.service.OrderItemService;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "orderItems", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderItemController {

    private final DaoService<OrderItemDTO> orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
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
