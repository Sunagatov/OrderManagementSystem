package com.zufar.service;

import com.zufar.dto.CustomerDTO;
import com.zufar.exception.CustomerNotFoundException;
import com.zufar.model.Customer;
import com.zufar.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService implements DaoService<CustomerDTO> {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Collection<CustomerDTO> getAll() {
        return ((Collection<Customer>) this.customerRepository.findAll())
                .stream()
                .map(CustomerService::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getById(Long id) {
        Customer customerEntity = this.customerRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The customer with id = " + id + " not found.";
            CustomerNotFoundException customerNotFoundException = new CustomerNotFoundException(errorMessage);
            LOGGER.error(errorMessage, customerNotFoundException);
            return customerNotFoundException;
        });
        return CustomerService.convertToCustomerDTO(customerEntity);
    }

    public CustomerDTO save(CustomerDTO customer) {
        Customer customerEntity = CustomerService.convertToCustomer(customer);
        customerEntity = this.customerRepository.save(customerEntity);
        return CustomerService.convertToCustomerDTO(customerEntity);
    }

    public CustomerDTO update(CustomerDTO customer) {
        this.isExists(customer.getId());
        Customer customerEntity = CustomerService.convertToCustomer(customer);
        customerEntity = this.customerRepository.save(customerEntity);
        return CustomerService.convertToCustomerDTO(customerEntity);
    }

    public void deleteById(Long id) {
        this.isExists(id);
        this.customerRepository.deleteById(id);
    }

    public Boolean isExists(Long id) {
        if (!this.customerRepository.existsById(id)) {
            final String errorMessage = "The customer with id = " + id + " not found.";
            CustomerNotFoundException customerNotFoundException = new CustomerNotFoundException(errorMessage);
            LOGGER.error(errorMessage, customerNotFoundException);
            throw customerNotFoundException;
        }
        return true;
    }

    public static Customer convertToCustomer(CustomerDTO customer) {
        UtilService.isObjectNull(customer, LOGGER, "There is no customer to convert.");
        Customer customerEntity = new Customer();
        customerEntity.setId(customer.getId());
        customerEntity.setName(customer.getName());
        customerEntity.setEmail(customer.getEmail());
        customerEntity.setLogin(customer.getLogin());
        customerEntity.setPassword(customer.getPassword());
        return customerEntity;
    }

    public static CustomerDTO convertToCustomerDTO(Customer customer) {
        UtilService.isObjectNull(customer, LOGGER, "There is no customer to convert.");
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setLogin(customer.getLogin());
        customerDTO.setPassword(customer.getPassword());
        return customerDTO;
    }
}
