package com.ohdoking.payment.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Builder
public class SaleReceipt {
    @NonNull
    UUID id;
    @NonNull
    UUID employeeId;
    @NonNull
    LocalDate date;
    @NonNull
    Integer amount;
}
