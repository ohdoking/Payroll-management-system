package com.ohdoking.payment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Getter
public class ServiceCharge {
    @NonNull
    UUID id;

    @NonNull
    UUID employeeId;

    @NonNull
    Integer amount;
}
