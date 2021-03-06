package com.ohdoking.payment.service;

import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.SaleReceipt;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.SalesReceiptRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SalesReceiptService {

    final private EmployeeRepository employeeRepository;
    final private SalesReceiptRepository salesReceiptRepository;

    public void addSalesReceipt(UUID employeeId, LocalDate localDate, Integer amount) {

        Employee employee = employeeRepository.getEmployee(employeeId);
        if (employee == null) {
            throw new ResourceNotFoundException(String.format("%s id of employee doesn't exist", employeeId.toString()));
        } else if (!employee.getPaymentType().equals(PaymentType.C)) {
            throw new IncorrectPaymentTypeEmployeeException("The employee is not C type of employee");
        }

        salesReceiptRepository.createSaleReceipt(SaleReceipt.builder()
                .id(UUID.randomUUID())
                .employeeId(employeeId)
                .date(localDate)
                .amount(amount)
                .build());

    }

    public List<SaleReceipt> getListOfSalesReceiptById(UUID employeeId, LocalDate date) {
        return salesReceiptRepository.getListOfSalesReceiptById(employeeId, date);
    }
}
