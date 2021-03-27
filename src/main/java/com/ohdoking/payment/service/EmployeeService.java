package com.ohdoking.payment.service;

import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class EmployeeService {

    final private EmployeeRepository employeeRepository;

    public void addEmpWithCommission(String name, String address, PaymentType paymentType, Double monthlyPay, Double commissionRate) {
        if (monthlyPay == null || commissionRate == null) {
            throw new NullPointerException("monthlyPay or commissionRate is missing");
        }
        addEmp(Employee.builder()
                .id(UUID.randomUUID())
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .monthlyPay(monthlyPay)
                .commissionRate(commissionRate)
                .build());

    }

    public void addEmpWithMonthlyPay(String name, String address, PaymentType paymentType, Double monthlyPay) {
        if (monthlyPay == null) {
            throw new NullPointerException("monthlyPay is missing");
        }
        addEmp(Employee.builder()
                .id(UUID.randomUUID())
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .monthlyPay(monthlyPay)
                .build());
    }

    public void addEmpWithHourlyRate(String name, String address, PaymentType paymentType, Double hourlyRate) {
        if (hourlyRate == null) {
            throw new NullPointerException("hourlyRate is missing");
        }
        addEmp(Employee.builder()
                .id(UUID.randomUUID())
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .hourlyRate(hourlyRate)
                .build());
    }

    private void addEmp(Employee employee) {
        employeeRepository.addEmployee(employee);
    }

    public void delEmp(UUID id) {
        employeeRepository.deleteEmployee(id);
    }

    public List<Employee> getListOfEmployee() {
        return employeeRepository.getListOfEmployee();
    }

}
