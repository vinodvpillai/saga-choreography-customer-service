package com.vinod.saga.choreography.customer.service.impl;

import com.vinod.saga.choreography.customer.service.IQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AWSSQSQueueService implements IQueueService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void sendMessage(String queue, String message) {
        jmsTemplate.convertAndSend(queue, message, m -> {
            m.setStringProperty("JMSXGroupID", "MSG_ID");
            return m;
        });
    }
}
