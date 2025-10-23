package com.openlodge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openlodge.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
