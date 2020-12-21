package com.vinod.saga.choreography.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(name="customer",schema = "saga-choreography-customer")
public class Customer {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String emailId;
    @Column
    private String password;
    @Column
    private String address;
    @Column
    private BigDecimal maxCreditLimit;
    @Column
    private BigDecimal currentCreditLimit;
    @Column
    private String status;
}
