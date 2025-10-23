package com.openlodge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openlodge.entities.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    
}
