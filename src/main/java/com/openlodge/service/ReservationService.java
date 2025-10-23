//Service de reservation, conectada con ReservationRepository
//falta hacer Payment

package com.openlodge.service;

import com.openlodge.entities.User;
import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Reservation;
import com.openlodge.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // Instanciamos el Repository
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // ponemos todas las reservas en una lista
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // busca la reserva especifica mediante el id de la reserva
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Obtiene la fecha del checkin
    public List<Reservation> getReservationsByCheckIn(LocalDate date) {
        return reservationRepository.findByCheckIn(date);
    }

    // Valida la reserva haciendo que el chackout sea post checkin
    public Reservation createReservation(Reservation reservation) {
        if (reservation.getCheckOut().isBefore(reservation.getCheckIn())) {
            throw new IllegalArgumentException("La fecha de check-out debe ser posterior a la de check-in");
        }
        return reservationRepository.save(reservation);
    }

    // Valida los precios del alojamiento
    public Reservation gettotalPrice(double totalPrice) {
        return (Reservation) reservationRepository.findByTotalPrice(totalPrice);
    }

    // BUsca la reserva mediante el guest(nombre)
    public List<Reservation> getReservationsByGuest(User guest) {
        return reservationRepository.findByGuest(guest);
    }

    //busca accomodation instanciado en ReservationRepo
    public List<Reservation> getAccomodationbyReservations(Accomodation accomodation) {
        return reservationRepository.findByAccomodation(accomodation);
    }

    
}