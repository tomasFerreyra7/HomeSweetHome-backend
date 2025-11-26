package com.openlodge.service;

import com.openlodge.dto.ReservationDTO;
import com.openlodge.entities.Reservation;
import com.openlodge.entities.User;
import com.openlodge.entities.Accomodation;
import com.openlodge.entities.Payment;
import com.openlodge.repository.ReservationRepository;
import com.openlodge.repository.UserRepository;
import com.openlodge.repository.AccomodationRepository;
import com.openlodge.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccomodationRepository accomodationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Crear una nueva reserva
    @Transactional
    public Reservation create(ReservationDTO dto) {
        
        if (!dto.getCheckIn().isBefore(dto.getCheckOut())) {
            throw new IllegalArgumentException("La fecha de Chack-In debe ser anterior al Check-Out");
        }

        boolean isOccupied = reservationRepository.existsOverLappingReservation(
            dto.getAccomodationId(),
            dto.getCheckIn(),
            dto.getCheckOut()
        );

        if (isOccupied) {
            throw new RuntimeException("El alojamiento no está disponible en las fechas seleccionadas.");
        }

        User guest = userRepository.findById(dto.getGuestId())
                .orElseThrow(() -> new EntityNotFoundException("Guest no encontrado"));

        Accomodation accomodation = accomodationRepository.findById(dto.getAccomodationId())
                .orElseThrow(() -> new EntityNotFoundException("Accomodation no encontrado"));

        Reservation reservation = Reservation.builder()
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .totalPrice(dto.getTotalPrice())
                .guest(guest)
                .accomodation(accomodation)
                .build();

        if (dto.getPaymentId() != null) {
            Payment payment = paymentRepository.findById(dto.getPaymentId())
                    .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado"));
            reservation.setPayment(payment);
        }

        return reservationRepository.save(reservation);
    }

    // Obtener todas las reservas
    public List<Reservation> findAll() {
        return reservationRepository.findByDeletedAtIsNull();
    }

    // Obtener una reserva por ID
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation no encontrada"));
    }

    @Transactional
    public Reservation update(Long id, ReservationDTO dto) {
        // Buscar la reserva por ID
        Reservation reservation = findById(id);

        if (reservation.getDeletedAt() != null) {
            throw new EntityNotFoundException("No se puede actualizar una reserva eliminada");
        }

        // 1. DETERMINAR LOS VALORES "EFECTIVOS"
        // Si el DTO trae un dato nuevo, usamos ese. Si es null, usamos el que ya tenía la reserva.
        LocalDate newCheckIn = (dto.getCheckIn() != null) ? dto.getCheckIn() : reservation.getCheckIn();
        LocalDate newCheckOut = (dto.getCheckOut() != null) ? dto.getCheckOut() : reservation.getCheckOut();
        Long newAccomodationId = (dto.getAccomodationId() != null) ? dto.getAccomodationId() : reservation.getAccomodation().getId();

        // 2. VALIDAR COHERENCIA DE FECHAS
        if (!newCheckIn.isBefore(newCheckOut)) {
            throw new IllegalArgumentException("La fecha de Check-In debe ser anterior al Check-Out");
        }

        // 3. VALIDAR DISPONIBILIDAD (Solo si cambiaron fechas o alojamiento)
        // Verificamos si cambiaron para no hacer consultas a la BD innecesarias, 
        // aunque podrías validar siempre por seguridad.
        boolean datesChanged = !newCheckIn.equals(reservation.getCheckIn()) || !newCheckOut.equals(reservation.getCheckOut());
        boolean roomChanged = !newAccomodationId.equals(reservation.getAccomodation().getId());

        if (datesChanged || roomChanged) {
            boolean isOccupied = reservationRepository.existsOverlappingReservationForUpdate(
                    newAccomodationId,
                    newCheckIn,
                    newCheckOut,
                    id // Pasamos el ID actual para excluirlo
            );

            if (isOccupied) {
                throw new RuntimeException("La modificación no es posible: El alojamiento no está disponible en las nuevas fechas.");
            }
        }

        // Actualizar solo si el campo no es nulo
        if (dto.getCheckIn() != null) {
            reservation.setCheckIn(dto.getCheckIn());
        }
        if (dto.getCheckOut() != null) {
            reservation.setCheckOut(dto.getCheckOut());
        }
        if (dto.getTotalPrice() != null) {
            reservation.setTotalPrice(dto.getTotalPrice());
        }

        // Actualizar los objetos relacionados solo si el ID es válido
        if (dto.getGuestId() != null) {
            User guest = userRepository.findById(dto.getGuestId())
                    .orElseThrow(() -> new EntityNotFoundException("Guest no encontrado"));
            reservation.setGuest(guest);
        }

        if (dto.getAccomodationId() != null) {
            Accomodation accomodation = accomodationRepository.findById(dto.getAccomodationId())
                    .orElseThrow(() -> new EntityNotFoundException("Accomodation no encontrado"));
            reservation.setAccomodation(accomodation);
        }

        if (dto.getPaymentId() != null) {
            Payment payment = paymentRepository.findById(dto.getPaymentId())
                    .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado"));
            reservation.setPayment(payment);
        }

        // Guardar la reserva actualizada
        return reservationRepository.save(reservation);
    }

    // Eliminar una reserva
    @Transactional
    public void delete(Long id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation);
    }

    // Soft delete
    @Transactional
    public void softDelete(Long id) {
        Reservation reservation = findById(id);

        reservation.setDeletedAt(LocalDate.now());
        reservationRepository.save(reservation);
    }

    @Transactional
    public void restore(Long id) {
        Reservation reservation = findById(id);

        if (reservation.getDeletedAt() != null) {
            reservation.setDeletedAt(null);
            reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("La reserva no esta eliminada");
        }
    }
}
