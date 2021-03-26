package com.ohdoking.payment.service;

import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.TimeCard;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.TimeCardRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TimeCardService {

    final private EmployeeRepository employeeRepository;
    final private TimeCardRepository timeCardRepository;

    public void addTimeCard(UUID employeeId, LocalDate localDate, Double hours) {

        Employee employee = employeeRepository.getEmployee(employeeId);
        if (employee == null) {
            throw new ResourceNotFoundException(String.format("%s id of employee doesn't exist", employeeId.toString()));
        } else if (!employee.getPaymentType().equals(PaymentType.H)) {
            throw new IncorrectPaymentTypeEmployeeException("The employee is not H type of employee");
        }

        timeCardRepository.createTimeCard(TimeCard.builder()
                .id(UUID.randomUUID())
                .employeeId(employeeId)
                .date(localDate)
                .hours(hours)
                .build());

    }

    public List<TimeCard> getListOfTimeCardById(UUID id, LocalDate date) {
        return timeCardRepository.getListOfTimeCardById(id, date);
    }
}
