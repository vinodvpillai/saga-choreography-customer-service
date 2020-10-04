package com.vinod.saga.choreography.customer.service;

public interface IPublisherService {

    <T> String publish(String topic,T msg, String subject);
}
