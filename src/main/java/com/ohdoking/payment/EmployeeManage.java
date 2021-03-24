package com.ohdoking.payment;

import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.TimeCard;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.TimeCardRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class EmployeeManage {

    final private EmployeeRepository employeeRepository;
    final private TimeCardRepository timeCardRepository;

    public void addEmpWithCommission(Integer id, String name, String address, PaymentType paymentType, Double monthlyPay, Double commissionRate) {
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

    public void addEmpWithMonthlyPay(Integer id, String name, String address, PaymentType paymentType, Double monthlyPay) {
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

    public void addEmpWithHourlyRate(Integer id, String name, String address, PaymentType paymentType, Double hourlyRate) {
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

    public void addTimeCard(int id, int employeeId, LocalDate localDate, Double hours) {

        Employee employee = employeeRepository.getEmployee(employeeId);
        if(!employee.getPaymentType().equals(PaymentType.H)){
            throw new IncorrectPaymentTypeEmployeeException("The employee is not H type of employee");
        }

        timeCardRepository.createTimeCard(TimeCard.builder()
                .id(id)
                .employeeId(employeeId)
                .date(localDate)
                .hours(hours)
                .build());

    }
}
