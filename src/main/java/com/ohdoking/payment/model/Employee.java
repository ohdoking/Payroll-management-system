package com.ohdoking.payment.model;

import lombok.*;

@Getter
@Builder
public class Employee {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String address;
    @NonNull
    private PaymentType paymentType;
    private Double hourlyRate;
    private Double monthlyPay;
    private Double commissionRate;
}
