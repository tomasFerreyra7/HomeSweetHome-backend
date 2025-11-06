package com.openlodge.controller;

import com.openlodge.dto.PaymentDTO;
import com.openlodge.entities.Payment;
import com.openlodge.service.PaymentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Crear un pago
    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody PaymentDTO paymentDTO) {
        Payment payment = paymentService.create(paymentDTO);
        return ResponseEntity.ok(payment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        try {
            Payment payment = paymentService.update(id, paymentDTO);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            // Devolver el mensaje de error para debugging
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + e.getMessage());
        }
    }

    // Eliminar un pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener un pago
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        return ResponseEntity.ok(payment);
    }

    // Obtener todos los pagos
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }
}
