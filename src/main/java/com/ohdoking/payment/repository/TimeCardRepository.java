package com.ohdoking.payment.repository;

import com.ohdoking.payment.model.TimeCard;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TimeCardRepository {
    public void createTimeCard(TimeCard t){

    }

    public List<TimeCard> getListOfTimeCardById(UUID employeeId, LocalDate date) {
        return List.of(TimeCard.builder().build());
    }
}
