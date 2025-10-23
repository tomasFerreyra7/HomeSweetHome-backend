package com.openlodge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openlodge.entities.Accomodation;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, Long>{
    
}
