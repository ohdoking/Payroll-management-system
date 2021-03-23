package com.ohdoking.payment;

import com.ohdoking.payment.model.Employee;

public class EmployeeManage {

    public String addEmpWithCommission(Integer id, String name, String address, String paymentType, Double monthlyPay, Double commissionRate) {
        if (monthlyPay == null || commissionRate == null){
            throw new NullPointerException("monthlyPay or commissionRate is missing");
        }
        return addEmp(Employee.builder()
                .id(id)
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .monthlyPay(monthlyPay)
                .commissionRate(commissionRate)
                .build());

    }

    public String addEmpWithMonthlyPay(Integer id, String name, String address, String paymentType, Double monthlyPay) {
        if (monthlyPay == null){
            throw new NullPointerException("monthlyPay is missing");
        }
        return addEmp(Employee.builder()
                .id(id)
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .monthlyPay(monthlyPay)
                .build());
    }

    public String addEmpWithHourlyRate(Integer id, String name, String address, String paymentType, Double hourlyRate) {
        if (hourlyRate == null){
            throw new NullPointerException("hourlyRate is missing");
        }
        return addEmp(Employee.builder()
                .id(id)
                .name(name)
                .address(address)
                .paymentType(paymentType)
                .hourlyRate(hourlyRate)
                .build());
    }

    private String addEmp(Employee employee){

        return "success";
    }

}
