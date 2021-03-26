package com.ohdoking.payment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@Getter
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
