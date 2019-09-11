package com.zufar.domain.order;

import com.zufar.dto.OrderDTO;
import com.zufar.dto.StatusDTO;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "orders", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {

    private final DaoService<OrderDTO> orderService;

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
        OrderService service = (orderService instanceof OrderService ? (OrderService)orderService : null);
        if (service == null) {
            throw new ClassCastException("Getting orderService is impossible.");
        }
        return service.updateStatus(status, orderId);
    }
}
