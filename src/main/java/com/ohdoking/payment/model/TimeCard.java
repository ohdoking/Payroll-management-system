package com.ohdoking.payment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@Builder
public class TimeCard {
    @NonNull
    Integer id;
    @NonNull
    Integer employeeId;
    @NonNull
    LocalDate date;
    @NonNull
    Double hours;
}
