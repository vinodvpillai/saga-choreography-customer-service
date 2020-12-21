package com.vinod.saga.choreography.customer.event;

import com.vinod.saga.choreography.customer.dto.CreditLimitExceededEventData;
import com.vinod.saga.choreography.customer.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.vinod.saga.choreography.customer.util.ApplicationConstant.CREDIT_LIMIT_EXCEEDED_SQS;

@Service
@Log4j2
public class CreditLimitExceededEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.customer.credit.limit.exceeded}")
    private String snsCustomerCreditLimitExceeded;

    public void raiseEventForCustomerCreditLimitExceeded(CreditLimitExceededEventData creditLimitExceededEventData) {
        log.trace("Request came to raise event for creditLimitExceededEventData credit limit exceeded: {}",creditLimitExceededEventData);
        try{
            publisherEventService.publish(snsCustomerCreditLimitExceeded,creditLimitExceededEventData,CREDIT_LIMIT_EXCEEDED_SQS);
            log.info("Successfully publish message for creditLimitExceededEventData credit limit exceeded: {}",creditLimitExceededEventData);
        } catch (Exception e) {
            log.trace("Error occurred while raising event for credit limit exceeded: {}",creditLimitExceededEventData);
        }
    }

}
