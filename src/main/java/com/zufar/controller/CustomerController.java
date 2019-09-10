package com.zufar.controller;

import com.zufar.dto.CustomerDTO;
import com.zufar.service.CustomerService;
import com.zufar.service.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "сustomers", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomerController {

    private final DaoService<CustomerDTO> customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/all")
    public @ResponseBody
    Collection<CustomerDTO> getCustomers() {
        return this.customerService.getAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    CustomerDTO getCustomer(@PathVariable Long id) {
        return this.customerService.getById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        this.customerService.deleteById(id);
    }

    @PostMapping
    public @ResponseBody
    CustomerDTO saveCustomer(@RequestBody CustomerDTO customer) {
        return this.customerService.save(customer);
    }

    @PutMapping
    public @ResponseBody
    CustomerDTO updateCustomer(@RequestBody CustomerDTO customer) {
        return this.customerService.update(customer);
    }
}
