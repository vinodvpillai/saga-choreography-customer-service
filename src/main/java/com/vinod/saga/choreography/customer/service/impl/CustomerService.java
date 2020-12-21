package com.vinod.saga.choreography.customer.service.impl;

import com.vinod.saga.choreography.customer.dto.*;
import com.vinod.saga.choreography.customer.event.CreditLimitExceededEvent;
import com.vinod.saga.choreography.customer.event.CreditReservedEvent;
import com.vinod.saga.choreography.customer.model.Customer;
import com.vinod.saga.choreography.customer.repository.CustomerRepository;
import com.vinod.saga.choreography.customer.service.ICustomerService;
import com.vinod.saga.choreography.customer.util.ApplicationConstant.CustomerStatus;
import com.vinod.saga.choreography.customer.util.GlobalUtility;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.vinod.saga.choreography.customer.util.ApplicationConstant.MAX_CREDIT_LIMIT;
import static com.vinod.saga.choreography.customer.util.GlobalUtility.reformatId;

@Slf4j
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CreditReservedEvent creditReservedEvent;
    @Autowired
    private CreditLimitExceededEvent creditLimitExceededEvent;

    /**
     * Add new customer.
     *
     * @param customerRegisterDto  - Customer object.
     * @return          - Persisted customer object.
     */
    @Override
    public CustomerQueryDto addCustomer(CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to add new customer with details: {}", customerRegisterDto);
        Customer customer = saveCustomer(customerRegisterDto);
        CustomerQueryDto customerQueryDto=mapCustomerToCustomerQueryDto(customer);
        log.trace("Successfully registered customer with details: {}",customerQueryDto);
        return customerQueryDto;
    }

    /**
     * Get customer object by customer id.
     *
     * @param id    - Customer ID.
     * @return      - Customer object.
     */
    @Override
    public CustomerQueryDto getCustomerById(Long id) {
        log.trace("Request came to fetch the customer details for customer id : {}",id);
        Optional<Customer> optionalCustomer=customerRepository.findById(id);
        if(optionalCustomer.isPresent()){
            CustomerQueryDto customerQueryDto=mapCustomerToCustomerQueryDto(optionalCustomer.get());
            log.trace("Successfully fetched customer details : {} for customer id: {}",customerQueryDto,id);
            return customerQueryDto;
        }
        return null;
    }

    @Override
    public boolean havingEnoughCreditLimitForPurchase(Long customerId, BigDecimal amount) {
        log.trace("Request came to validate the customer having enough credit limit or not for customer id : {}",customerId, amount);
        Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
        if(optionalCustomer.isPresent()) {
            BigDecimal currentCreditLimit=optionalCustomer.get().getCurrentCreditLimit();
            log.debug("Customer id: {} current credit limit: {} and the purchase amount: {}",customerId, currentCreditLimit,amount);
            return currentCreditLimit.subtract(amount).compareTo(BigDecimal.ZERO) >=0 ? true : false;
        }
        return false;
    }

    @Override
    public CustomerQueryDto deductCreditLimitForPurchaseAmt(Long customerId, BigDecimal amount) {
        log.trace("Request came for customer id: {}, to deduct the customer credit limit with purchase amount: {}",customerId, amount);
        if(havingEnoughCreditLimitForPurchase(customerId, amount)) {
            Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
            Customer customer = optionalCustomer.get();
            customer.setCurrentCreditLimit(customer.getCurrentCreditLimit().subtract(amount));
            customerRepository.save(customer);
            CustomerQueryDto customerQueryDto=mapCustomerToCustomerQueryDto(optionalCustomer.get());
            log.trace("Successfully deducted the purchase amount for customer details : {} for customer id: {}, purchase amount: {}",customerQueryDto,customerId,amount);
            return customerQueryDto;
        }
        return null;
    }

    @Override
    public void validatePurchaseAndRaiseEvent(OrderCreatedEventData orderCreatedEventData) {
        log.trace("Request came for validate the purchase and raise event : {}",orderCreatedEventData);
        Long customerId = reformatId(orderCreatedEventData.getCustomerId());
        CustomerQueryDto customerQueryDto = deductCreditLimitForPurchaseAmt(customerId,orderCreatedEventData.getAmount());
        if(GlobalUtility.isNotNull(customerQueryDto)) {
            raiseEventForPurchaseApproved(orderCreatedEventData, customerQueryDto);
        } else {
            raiseEventForPurchaseDeclined(orderCreatedEventData, customerId);
        }
    }

    /**
     * Raise event for purchase declined.
     *
     * @param orderCreatedEventData
     * @param customerId
     */
    private void raiseEventForPurchaseDeclined(OrderCreatedEventData orderCreatedEventData, Long customerId) {
        log.trace("Request came to raise event for purchase declined for Customer id: {}, orderCreatedEventData : {} ", customerId, orderCreatedEventData);
        Optional<Customer> optionalCustomer=customerRepository.findById(customerId);
        Customer customer = optionalCustomer.get();
        CreditLimitExceededEventData creditLimitExceededEventData=CreditLimitExceededEventData.builder()
                .customerId(orderCreatedEventData.getCustomerId())
                .orderId(orderCreatedEventData.getOrderId())
                .currentCreditLimit(customer.getCurrentCreditLimit())
                .build();
        creditLimitExceededEvent.raiseEventForCustomerCreditLimitExceeded(creditLimitExceededEventData);
    }

    /**
     * Raise event for purchase approved.
     *
     * @param orderCreatedEventData
     * @param customerQueryDto
     */
    private void raiseEventForPurchaseApproved(OrderCreatedEventData orderCreatedEventData, CustomerQueryDto customerQueryDto) {
        log.trace("Request came to raise event for purchase approved for Customer id: {}, orderCreatedEventData : {} ", customerQueryDto.getId(), orderCreatedEventData);
        CreditReservedEventData creditReservedEventData = CreditReservedEventData.builder()
                .orderId(orderCreatedEventData.getOrderId())
                .customerId(orderCreatedEventData.getCustomerId())
                .customerAddress(customerQueryDto.getAddress())
                .emailId(customerQueryDto.getEmailId())
                .build();
        creditReservedEvent.raiseEventForCustomerCreditReserved(creditReservedEventData);
    }

    /**
     * Save the customer object to database.
     *
     * @param customerRegisterDto - Customer register command dto object.
     * @return  - Customer object.
     */
    private Customer saveCustomer(CustomerRegisterDto customerRegisterDto) {
        log.trace("Request came to save the customer object with customer details: {}", customerRegisterDto);
        Customer customer = mapDataToCustomer(customerRegisterDto);
        return customerRepository.save(customer);
    }

    /**
     * Map CustomerRegisterCommandDto to Customer object.
     *
     * @param customerRegisterDto - CustomerRegisterCommandDto object.
     * @return  - Customer object.
     */
    private Customer mapDataToCustomer(CustomerRegisterDto customerRegisterDto) {
        modelMapper.typeMap(CustomerRegisterDto.class, Customer.class).addMappings(mapper -> mapper.skip(Customer::setId));
        Customer customer = modelMapper.map(customerRegisterDto, Customer.class);
        customer.setMaxCreditLimit(MAX_CREDIT_LIMIT);
        customer.setCurrentCreditLimit(MAX_CREDIT_LIMIT);
        customer.setStatus(CustomerStatus.REGISTERED.value());
        return customer;
    }

    /**
     * Map CustomerRegisterCommandDto to Customer object.
     *
     * @param customer - CustomerRegisterCommandDto object.
     * @return  - Customer Query object.
     */
    private CustomerQueryDto mapCustomerToCustomerQueryDto(Customer customer) {
        CustomerQueryDto customerQueryDto = modelMapper.map(customer, CustomerQueryDto.class);
        return customerQueryDto;
    }

}
