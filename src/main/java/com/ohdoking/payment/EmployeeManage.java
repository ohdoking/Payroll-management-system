package com.ohdoking.payment;

import com.ohdoking.payment.model.Employee;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeManage {

    final private EmployeeRepository employeeRepository;

    public void addEmpWithCommission(Integer id, String name, String address, String paymentType, Double monthlyPay, Double commissionRate) {
        if (monthlyPay == null || commissionRate == null){
            throw new NullPointerException("monthlyPay or commissionRate is missing");
        }
        addEmp(Employee.builder()
                .id(id)
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .monthlyPay(monthlyPay)
                .commissionRate(commissionRate)
                .build());

    }

    public void addEmpWithMonthlyPay(Integer id, String name, String address, String paymentType, Double monthlyPay) {
        if (monthlyPay == null){
            throw new NullPointerException("monthlyPay is missing");
        }
        addEmp(Employee.builder()
                .id(id)
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .monthlyPay(monthlyPay)
                .build());
    }

    public void addEmpWithHourlyRate(Integer id, String name, String address, String paymentType, Double hourlyRate) {
        if (hourlyRate == null){
            throw new NullPointerException("hourlyRate is missing");
        }
        addEmp(Employee.builder()
                .id(id)
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .hourlyRate(hourlyRate)
                .build());
    }

    private void addEmp(Employee employee){
        employeeRepository.addEmployee(employee);
    }

    public void delEmp(int id) {
        employeeRepository.deleteEmployee(id);
    }
}
