package com.zufar.user;

import com.zufar.service.DaoService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "customers", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    private final DaoService<UserDTO> customerService;

    @Autowired
    public UserController(UserService userService) {
        this.customerService = userService;
    }

    @GetMapping
    public @ResponseBody
    Collection<UserDTO> getCustomers() {
        return this.customerService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    UserDTO getCustomer(@PathVariable Long id) {
        return this.customerService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        this.customerService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody
    UserDTO saveCustomer(@RequestBody UserDTO customer) {
        return this.customerService.save(customer);
    }

    @PutMapping
    public @ResponseBody
    UserDTO updateCustomer(@RequestBody UserDTO customer) {
        return this.customerService.update(customer);
    }
}
