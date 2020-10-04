package com.vinod.saga.choreography.customer.event;

import com.vinod.saga.choreography.customer.model.Customer;
import com.vinod.saga.choreography.customer.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerCreatedEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.customer.created}")
    private String snsCustomerCreated;

    public void raiseEventForNewCustomerCreated(Customer customer) {
        log.trace("Request came to raise event for created new customer: {}",customer);
        try{
            publisherEventService.publish(snsCustomerCreated,customer,"Customer_Created");
            log.info("Successfully publish message for created new customer: {}",customer);
        } catch (Exception e) {
            log.trace("Error occurred while raising event for new customer created: {}",customer);
        }
    }

}
