package com.openlodge.controller;

import com.openlodge.dto.ReservationDTO;
import com.openlodge.entities.Reservation;
import com.openlodge.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody @jakarta.validation.Valid ReservationDTO reservationDTO) {
        Reservation reservation = reservationService.create(reservationDTO);
        return ResponseEntity.ok(reservation);
    }

    // Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<Reservation>> getAll() {
        List<Reservation> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    // Obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    // Reservas por hostId
    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Reservation>> getReservationsByHost(@PathVariable Long hostId) {
        List<Reservation> reservations = reservationService.findByHostId(hostId);
        return ResponseEntity.ok(reservations);
    }

    // Reservas por accomodationId
    @GetMapping("/accomodations/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByAccomodation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findByAccomodationId(id));
    }

    // Actualizar una reserva
    @PatchMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        // Verificar si el ID es válido
        if (id == null) {
            return ResponseEntity.badRequest().body(null); // Error si el ID no es válido
        }

        // Log de depuración
        System.out.println("ID recibido: " + id);

        // Llamada al servicio para actualizar la reserva
        Reservation reservation = reservationService.update(id, reservationDTO);

        // Respuesta con la reserva actualizada
        return ResponseEntity.ok(reservation);
    }

    // Eliminar una reserva(soft-delete)
    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        reservationService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar una reserva (soft-delete == null)
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        reservationService.restore(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
