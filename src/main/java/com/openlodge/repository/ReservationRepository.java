package com.openlodge.repository;

import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Reservation;
import com.openlodge.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Busca las reservas por fecha de check-in
    List<Reservation> findByCheckIn(LocalDate checkIn);

    // Busca las reservas por fecha de check-out
    List<Reservation> findByCheckOut(LocalDate checkOut);

    // Busca la reserva y ve el precio total
    List<Reservation> findByTotalPrice(Double totalPrice);

    // busca la reserva por el usuario
    List<Reservation> findByGuest(User guest);

    // busca la lista de accomodation
    List<Reservation> findByAccomodation(Accomodation accomodation);
}