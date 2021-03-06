package com.zufar.order;

import com.zufar.status.StatusDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Collection;

@RestController
@RequestMapping(value = "orders", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public @ResponseBody
    Collection<OrderDTO> getOrders(@RequestParam(required = false) String sortBy) {
        if (sortBy == null) {
            return this.orderService.getAll();
        } else {
            return this.orderService.getAll(sortBy);
        }
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    OrderDTO getOrder(@PathVariable Long id) {
        return this.orderService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteOrder(@PathVariable Long id) {
        this.orderService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody
    OrderDTO saveOrder(@RequestBody OrderDTO order) {
        return this.orderService.save(order);
    }

    @PutMapping
    public @ResponseBody
    OrderDTO updateOrder(@RequestBody OrderDTO order) {
        return this.orderService.update(order);
    }

    @PutMapping(value = "/{orderId}")
    public @ResponseBody
    OrderDTO changeOrderStatus(@RequestBody StatusDTO status, @RequestParam Long orderId) {
        return orderService.updateStatus(status, orderId);
    }
}
