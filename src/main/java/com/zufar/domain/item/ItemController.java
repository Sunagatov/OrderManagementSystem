package com.zufar.domain.item;

import com.zufar.dto.ItemDTO;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "items", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ItemController {

    private final DaoService<ItemDTO> orderItemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.orderItemService = itemService;
    }

    @GetMapping
    public @ResponseBody Collection<ItemDTO> getOrderItems() {
        return this.orderItemService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    ItemDTO getItem(@PathVariable Long id) {
        return this.orderItemService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteItem(@PathVariable Long id) {
        this.orderItemService.getById(id);
    }

    @PostMapping
    public @ResponseBody
    ItemDTO saveItem(@RequestBody ItemDTO item) {
        return this.orderItemService.save(item);
    }

    @PutMapping
    public @ResponseBody
    ItemDTO updateItem(@RequestBody ItemDTO item) {
        return  this.orderItemService.update(item);
    }
}
