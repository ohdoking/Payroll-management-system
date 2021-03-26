package com.ohdoking.payment.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Builder
public class Payment {
    @NonNull
    private UUID id;
    @NonNull
    private UUID employeeId;
    @NonNull
    private Double salaryAmount;
    @NonNull
    private PaymentWay paymentWay;
    @NonNull
    private LocalDate paymentDate;
}
