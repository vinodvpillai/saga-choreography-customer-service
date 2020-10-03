package com.vinod.saga.choreography.customer.repository;

import com.vinod.saga.choreography.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmailId(String emailId);
}
