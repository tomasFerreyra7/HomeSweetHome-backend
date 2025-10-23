//Controller de Reservation
//Falta hacer el Payment

package com.openlodge.controller;

import com.openlodge.entities.Reservation;
import com.openlodge.entities.User;
import com.openlodge.entities.Accomodation;
import com.openlodge.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/checkin")
    public List<Reservation> getByCheckIn(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.getReservationsByCheckIn(date);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        try {
            Reservation created = reservationService.createReservation(reservation);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/guest/{guestId}")
    public List<Reservation> getByGuest(@PathVariable Long guestId) {
        User guest = new User();
        guest.setId(guestId);
        return reservationService.getReservationsByGuest(guest);
    }

    @GetMapping("/accomodation/{accomodationId}")
    public List<Reservation> getByAccomodation(@PathVariable Long accomodationId) {
        Accomodation acc = new Accomodation();
        acc.setId(accomodationId);
        return reservationService.getAccomodationbyReservations(acc);
    }

    @GetMapping("/price")
    public ResponseEntity<Reservation> getByTotalPrice(@RequestParam double amount) {
        Reservation r = reservationService.gettotalPrice(amount);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }
}