package com.openlodge.repository;

import com.openlodge.entities.Reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDeletedAtIsNull();

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " + 
        "WHERE r.accomodation.id = :accomodationId " +
        "AND r.deletedAt IS NULL " +
        "AND ((:checkIn < r.checkOut) AND (:checkOut > checkIn))")
    boolean existsOverLappingReservation(
        @Param("accomodationId") Long accomodationId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
    );

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
           "WHERE r.accomodation.id = :accomodationId " +
           "AND r.deletedAt IS NULL " +
           "AND r.id != :excludeId " + // <--- ESTA ES LA CLAVE
           "AND ((:checkIn < r.checkOut) AND (:checkOut > r.checkIn))")
    boolean existsOverlappingReservationForUpdate(
            @Param("accomodationId") Long accomodationId, 
            @Param("checkIn") LocalDate checkIn, 
            @Param("checkOut") LocalDate checkOut,
            @Param("excludeId") Long excludeId);
}
