package com.ohdoking.payment;

import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.SaleReceipt;
import com.ohdoking.payment.model.TimeCard;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.SalesReceiptRepository;
import com.ohdoking.payment.repository.TimeCardRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class EmployeeManage {

    final private EmployeeRepository employeeRepository;
    final private TimeCardRepository timeCardRepository;
    final private SalesReceiptRepository salesReceiptRepository;

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

    public void addTimeCard(UUID employeeId, LocalDate localDate, Double hours) {

        Employee employee = employeeRepository.getEmployee(employeeId);
        if (!employee.getPaymentType().equals(PaymentType.H)) {
            throw new IncorrectPaymentTypeEmployeeException("The employee is not H type of employee");
        }

        timeCardRepository.createTimeCard(TimeCard.builder()
                .id(UUID.randomUUID())
                .employeeId(employeeId)
                .date(localDate)
                .hours(hours)
                .build());

    }

    public void addSalesReceipt(UUID employeeId, LocalDate localDate, Integer amount) {

        Employee employee = employeeRepository.getEmployee(employeeId);
        if (!employee.getPaymentType().equals(PaymentType.C)) {
            throw new IncorrectPaymentTypeEmployeeException("The employee is not C type of employee");
        }

        salesReceiptRepository.createSaleReceipt(SaleReceipt.builder()
                .id(UUID.randomUUID())
                .employeeId(employeeId)
                .date(localDate)
                .amount(amount)
                .build());

    }
}
