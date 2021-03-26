package com.ohdoking.payment.service;

import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.ServiceCharge;
import com.ohdoking.payment.repository.*;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class SalesReceiptService {

    final private EmployeeRepository employeeRepository;
    final private ServiceChargeRepository serviceChargeRepository;

    public void addServiceCharge(UUID employeeId, Integer amount) {

        Employee employee = employeeRepository.getEmployee(employeeId);
        if (employee == null) {
            throw new ResourceNotFoundException(String.format("%s id of employee doesn't exist", employeeId.toString()));
        }

        serviceChargeRepository.createServiceCharge(ServiceCharge.builder()
                .id(UUID.randomUUID())
                .employeeId(employeeId)
                .amount(amount)
                .build());
    }
}
