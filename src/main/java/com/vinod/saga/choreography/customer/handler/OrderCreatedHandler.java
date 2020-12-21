package com.vinod.saga.choreography.customer.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinod.saga.choreography.customer.dto.OrderCreatedEventData;
import com.vinod.saga.choreography.customer.service.ICustomerService;
import com.vinod.saga.choreography.customer.util.GlobalUtility;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrderCreatedHandler {
    @Value("${sqs.queue.order.created}")
    private String sqsOrderCreated;
    @Autowired
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper= GlobalUtility.getDateFormatObjectMapper();
    @Autowired
    private ICustomerService customerService;

    @JmsListener(destination = "${sqs.queue.order.created}")
    void orderCreatedListener(String message) throws Exception {
        log.trace("Event received that a customer order is created");
        JsonNode jsonMessage= GlobalUtility.isNotNull(objectMapper.readTree(message)) ? objectMapper.readTree(message).get("Message"):null;
        if(GlobalUtility.isNotNull(jsonMessage)){
            OrderCreatedEventData receivedCustomerObject=objectMapper.readValue(jsonMessage.asText(), OrderCreatedEventData.class);
            customerService.validatePurchaseAndRaiseEvent(receivedCustomerObject);
            log.trace("Received customer order created object: {}",receivedCustomerObject);
        } else {
            log.warn("No message found in the message for: {} and the message: {}"+ sqsOrderCreated, message);
        }
    }
}
