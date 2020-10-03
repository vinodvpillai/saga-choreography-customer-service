package com.vinod.saga.choreography.customer.service.impl;

import com.vinod.saga.choreography.customer.model.Customer;
import com.vinod.saga.choreography.customer.repository.CustomerRepository;
import com.vinod.saga.choreography.customer.service.ICustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Add new customer.
     *
     * @param customer  - Customer object.
     * @return          - Persisted customer object.
     */
    @Override
    public Customer addCustomer(Customer customer) {
        log.trace("Request came to add new customer : {}",customer);
        Customer persistedCustomer=customerRepository.save(customer);
        log.trace("Successfully saved customer object and persisted object: {}",persistedCustomer);
        return persistedCustomer;
    }

    /**
     * Get customer object by customer id.
     *
     * @param id    - Customer ID.
     * @return      - Customer object.
     */
    @Override
    public Customer getCustomerById(Long id) {
        log.trace("Request came to fetch the customer having customer id : {}",id);
        Optional<Customer> optionalCustomer=customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            Customer customer=optionalCustomer.get();
            log.trace("Successfully fetched customer object : {} having customer id: {}",customer,id);
            return customer;
        }
        return null;
    }

    /**
     * Get customer object by email id.
     *
     * @param emailId    - Customer Email ID.
     * @return      - Customer object.
     */
    @Override
    public Customer getCustomerByEmailId(String emailId) {
        log.trace("Request came to fetch the customer having customer email id : {}",emailId);
        Optional<Customer> optionalCustomer=customerRepository.findByEmailId(emailId);
        if(optionalCustomer.isPresent()){
            Customer customer=optionalCustomer.get();
            log.trace("Successfully fetched customer object : {} having customer email id: {}",customer,emailId);
            return customer;
        }
        return null;
    }
}
