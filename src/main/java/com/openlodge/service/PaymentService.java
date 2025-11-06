package com.openlodge.service;

import com.openlodge.dto.PaymentDTO;
import com.openlodge.entities.Payment;
import com.openlodge.entities.Reservation;
import com.openlodge.repository.PaymentRepository;
import com.openlodge.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;

    // Crear un pago
    @Transactional
    public Payment create(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setMethod(dto.getMethod());
        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());

        if (dto.getReservationId() != null) {
            // Buscar la reserva asociada al pago, si existe
            Reservation reservation = reservationRepository.findById(dto.getReservationId())
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
            payment.setReservation(reservation);
        }

        return paymentRepository.save(payment);
    }

    // Actualizar un pago
    @Transactional
    public Payment update(Long id, PaymentDTO dto) {
        // Buscar el pago por su ID
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    
        // Actualizar solo los campos que no son nulos
        if (dto.getMethod() != null) {
            payment.setMethod(dto.getMethod());
        }
        
        if (dto.getAmount() != null) {
            payment.setAmount(dto.getAmount());
        }
        
        if (dto.getStatus() != null) {
            payment.setStatus(dto.getStatus());
        }
    
        // Si se pasó un ID de reserva, actualizar la reserva asociada
        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(dto.getReservationId())
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
            payment.setReservation(reservation);
        }
    
        // Guardar el pago actualizado
        return paymentRepository.save(payment);
    }

    // Eliminar un pago
    @Transactional
    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        paymentRepository.delete(payment);
    }

    // Obtener un pago por su ID
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    // Obtener todos los pagos
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }
}
