package com.vinod.saga.choreography.customer.event;

import com.vinod.saga.choreography.customer.dto.CreditReservedEventData;
import com.vinod.saga.choreography.customer.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.vinod.saga.choreography.customer.util.ApplicationConstant.CREDIT_RESERVED_SQS;

@Service
@Log4j2
public class CreditReservedEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.customer.credit.reserved}")
    private String snsCustomerCreditReserved;

    public void raiseEventForCustomerCreditReserved(CreditReservedEventData creditReservedEventData) {
        log.trace("Request came to raise event for creditReservedEventData credit reserved: {}",creditReservedEventData);
        try{
            publisherEventService.publish(snsCustomerCreditReserved,creditReservedEventData,CREDIT_RESERVED_SQS);
            log.info("Successfully publish message for creditReservedEventData credit reserved: {}",creditReservedEventData);
        } catch (Exception e) {
            log.trace("Error occurred while raising event for creditReservedEventData credit reserved: {}",creditReservedEventData);
        }
    }

}
