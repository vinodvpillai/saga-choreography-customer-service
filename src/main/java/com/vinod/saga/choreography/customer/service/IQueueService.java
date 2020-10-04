package com.vinod.saga.choreography.customer.service;

public interface IQueueService {

    public void sendMessage(String queue,String message);
}
