package com.vinod.saga.choreography.customer.service;

import com.vinod.saga.choreography.customer.dto.CustomerQueryDto;
import com.vinod.saga.choreography.customer.dto.CustomerRegisterDto;
import com.vinod.saga.choreography.customer.dto.OrderCreatedEventData;

import java.math.BigDecimal;

public interface ICustomerService {

    /**
     * Add new customer.
     *
     * @param customerRegisterDto  - Customer object.
     * @return          - Persisted customer object.
     */
    CustomerQueryDto addCustomer(CustomerRegisterDto customerRegisterDto);

    /**
     * Get customer object by customer id.
     *
     * @param id    - Customer ID.
     * @return      - Customer object.
     */
    CustomerQueryDto getCustomerById(Long id);

    /**
     * Validate the customer current limit for the given purchase amount.
     *
     * @param customerId    - Customer ID.
     * @param amount        - Purchase amount.
     * @return              - True or False.
     */
    boolean havingEnoughCreditLimitForPurchase(Long customerId, BigDecimal amount);

    /**
     * Deduct the customer credit limit amount base on the current purchase amount.
     *
     * @param customerId    - Customer ID.
     * @param amount        - Purchase amount.
     * @return              - Customer query object.
     */
    CustomerQueryDto deductCreditLimitForPurchaseAmt(Long customerId, BigDecimal amount);

    /**
     * Validate the purchase transaction and raise concern event.
     *
     * @param orderCreatedEventData - OrderCreatedEventData object.
     */
    void validatePurchaseAndRaiseEvent(OrderCreatedEventData orderCreatedEventData);
}
