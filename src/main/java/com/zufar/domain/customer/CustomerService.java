package com.zufar.domain.customer;

import com.zufar.dto.CustomerDTO;
import com.zufar.exception.CustomerNotFoundException;
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
public class CustomerService implements DaoService<CustomerDTO> {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Collection<CustomerDTO> getAll() {
        return ((Collection<Customer>) this.customerRepository.findAll())
                .stream()
                .map(CustomerService::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CustomerDTO> getAll(String sortBy) {
        return ((Collection<Customer>) this.customerRepository.findAll(Sort.by(sortBy)))
                .stream()
                .map(CustomerService::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getById(Long id) {
        Customer customerEntity = this.customerRepository.findById(id).orElseThrow(() -> {
            final String errorMessage = "The customer with id = " + id + " not found.";
            CustomerNotFoundException customerNotFoundException = new CustomerNotFoundException(errorMessage);
            LOGGER.error(errorMessage, customerNotFoundException);
            return customerNotFoundException;
        });
        return CustomerService.convertToCustomerDTO(customerEntity);
    }

    @Override
    public CustomerDTO save(CustomerDTO customer) {
        Customer customerEntity = CustomerService.convertToCustomer(customer);
        customerEntity = this.customerRepository.save(customerEntity);
        return CustomerService.convertToCustomerDTO(customerEntity);
    }

    @Override
    public CustomerDTO update(CustomerDTO customer) {
        this.isExists(customer.getId());
        Customer customerEntity = CustomerService.convertToCustomer(customer);
        customerEntity = this.customerRepository.save(customerEntity);
        return CustomerService.convertToCustomerDTO(customerEntity);
    }

    @Override
    public void deleteById(Long id) {
        this.isExists(id);
        this.customerRepository.deleteById(id);
    }

    @Override
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
        Objects.requireNonNull(customer, "There is no customer to convert.");
        return new Customer(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getLogin(),
                customer.getPassword());
    }

    public static CustomerDTO convertToCustomerDTO(Customer customer) {
        Objects.requireNonNull(customer, "There is no customer to convert.");
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getLogin(),
                customer.getPassword());
    }
}
