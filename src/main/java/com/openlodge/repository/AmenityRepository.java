package com.openlodge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openlodge.entities.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
