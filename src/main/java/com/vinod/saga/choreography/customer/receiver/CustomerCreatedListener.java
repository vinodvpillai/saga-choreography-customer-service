package com.vinod.saga.choreography.customer.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinod.saga.choreography.customer.model.Customer;
import com.vinod.saga.choreography.customer.util.GlobalUtility;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.jvm.hotspot.HelloWorld;

@Service
@Log4j2
public class CustomerCreatedListener {
    @Value("${sqs.queue.customer.created}")
    private String sqsCustomerCreated;
    @Autowired
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper= GlobalUtility.getDateFormatObjectMapper();

    @JmsListener(destination = "${sqs.queue.customer.created}")
    void customerCreatedListener(String message) throws Exception {
        log.trace("Event received that a new customer is created");
        JsonNode jsonMessage= GlobalUtility.isNotNull(objectMapper.readTree(message)) ? objectMapper.readTree(message).get("Message"):null;
        if(GlobalUtility.isNotNull(jsonMessage)){
            Customer receivedCustomerObject=objectMapper.readValue(jsonMessage.asText(),Customer.class);
            log.trace("Received customer object: {}",receivedCustomerObject);
        } else {
            log.warn("No message found in the message for: {} and the message: {}"+sqsCustomerCreated, message);
        }
    }
}
