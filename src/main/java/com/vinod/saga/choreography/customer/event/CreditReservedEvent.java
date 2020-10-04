package com.vinod.saga.choreography.customer.event;

import com.vinod.saga.choreography.customer.model.Customer;
import com.vinod.saga.choreography.customer.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CreditReservedEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.customer.credit.reserved}")
    private String snsCustomerCreditReserved;

    public void raiseEventForCustomerCreditReserved(Customer customer) {
        log.trace("Request came to raise event for customer credit reserved: {}",customer);
        try{
            publisherEventService.publish(snsCustomerCreditReserved,customer,"Credit_Reserved");
            log.info("Successfully publish message for customer credit reserved: {}",customer);
        } catch (Exception e) {
            log.trace("Error occurred while raising event for customer credit reserved: {}",customer);
        }
    }

}
