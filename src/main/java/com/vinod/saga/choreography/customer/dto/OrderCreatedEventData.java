package com.vinod.saga.choreography.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEventData implements Serializable {
    private String orderId;
    private BigDecimal amount;
    private String customerId;
}
