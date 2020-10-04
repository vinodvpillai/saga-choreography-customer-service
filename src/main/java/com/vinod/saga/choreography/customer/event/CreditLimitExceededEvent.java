package com.vinod.saga.choreography.customer.event;

import com.vinod.saga.choreography.customer.model.Customer;
import com.vinod.saga.choreography.customer.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CreditLimitExceededEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.customer.credit.limit.exceeded}")
    private String snsCustomerCreditLimitExceeded;

    public void raiseEventForCustomerCreditLimitExceeded(Customer customer) {
        log.trace("Request came to raise event for customer credit limit exceeded: {}",customer);
        try{
            publisherEventService.publish(snsCustomerCreditLimitExceeded,customer,"Credit_Limit_Exceeded");
            log.info("Successfully publish message for customer credit limit exceeded: {}",customer);
        } catch (Exception e) {
            log.trace("Error occurred while raising event for credit limit exceeded: {}",customer);
        }
    }

}
