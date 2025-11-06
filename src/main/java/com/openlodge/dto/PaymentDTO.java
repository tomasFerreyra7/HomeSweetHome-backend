package com.openlodge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private String method;  // Método de pago (tarjeta, transferencia, etc.)
    private Double amount;  // Monto del pago
    private String status;  // Estado del pago (pending, completed, etc.)
    private Long reservationId;  // ID de la reserva asociada al pago
}

