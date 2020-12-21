package com.vinod.saga.choreography.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditReservedEventData implements Serializable {
    private String orderId;
    private String customerId;
    private String emailId;
    private String customerAddress;
}
