package com.ohdoking.payment.repository;

import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.ServiceCharge;

import java.util.UUID;

public class ServiceChargeRepository {
    public void createServiceCharge(ServiceCharge serviceCharge) {
    }

    public ServiceCharge findById(UUID employeeId) {
        return ServiceCharge.builder().build();
    }
}
