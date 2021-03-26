package com.ohdoking.payment;

import com.ohdoking.payment.model.*;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.PaymentRepository;
import com.ohdoking.payment.service.SalesReceiptService;
import com.ohdoking.payment.service.ServiceChargeService;
import com.ohdoking.payment.service.TimeCardService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class EmployeeManager {

    final private EmployeeRepository employeeRepository;
    final private PaymentRepository paymentRepository;
    final private ServiceChargeService serviceChargeService;
    final private TimeCardService timeCardService;
    final private SalesReceiptService salesReceiptService;

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


    /**
     * 1. get list of employee
     * 2. check whether payment date or not
     * 3. calculate salary based on paymentType
     * 3-1 hourly type
     * 3-1-1 check whether today is friday
     * 3-1-2 get last week salary from timecard
     * 3-1-3 calculate
     * 3-2 salary type
     * 3-2-1 check whether today is weekday in last week of month
     * 3-2-2 get salary from employee
     * 3-3 commission type
     * 3-3-1 check whether when is the last payment date
     * 3-3-2 get salary from employee
     * 3-3-3 calculate sales receipt from last payment date and add in salary
     * 4. check whether the employee is part of service charge
     * 4-1 if it,s true then reduce the price for this from salary
     * 5. write this in payment table.
     * 6. pay salary with what employee want.
     * 6-1. get payment way from Employee
     *
     * @param date
     */

    public void payday(LocalDate date) {
        //1. get list of employee
        List<Employee> employeeList = employeeRepository.getListOfEmployee();

        employeeList.forEach((employee) -> {
            if (isPaymentDate(employee.getPaymentType(), date)) {

                double salaryAmount = 0.0;

                if (PaymentType.H.equals(employee.getPaymentType())) {
                    List<TimeCard> timeCardList = timeCardService.getListOfTimeCardById(employee.getId(), date.minus(Period.ofWeeks(1)));

                    for (TimeCard timeCard : timeCardList) {
                        salaryAmount += getSalaryAmount(employee.getHourlyRate(), timeCard.getHours());
                    }
                } else if (PaymentType.S.equals(employee.getPaymentType())) {
                    salaryAmount = employee.getMonthlyPay();

                } else if (PaymentType.C.equals(employee.getPaymentType())) {
                    salaryAmount = employee.getMonthlyPay();

                    List<SaleReceipt> saleReceiptList = salesReceiptService.getListOfSalesReceiptById(employee.getId(), date.minus(Period.ofWeeks(2)));
                    for (SaleReceipt saleReceipt : saleReceiptList) {
                        salaryAmount += saleReceipt.getAmount() * employee.getCommissionRate();
                    }
                }

                ServiceCharge serviceCharge = serviceChargeService.findServiceChargeById(employee.getId());
                if (serviceCharge != null) {
                    salaryAmount -= serviceCharge.getAmount();
                }

                paymentRepository.savePaymentt(Payment.builder()
                        .id(UUID.randomUUID())
                        .employeeId(employee.getId())
                        .salaryAmount(salaryAmount)
                        .paymentDate(date)
                        .paymentWay(employee.getPaymentWay())
                        .build()
                );
            }
        });

    }

    private double getSalaryAmount(Double employeeHourlyRate, double hours) {
        if (hours > 8) {
            return 8 * employeeHourlyRate + getSalaryAmount(employeeHourlyRate * 1.5, hours - 8);
        }
        return hours * employeeHourlyRate;
    }

    // TODO implement proper code
    private boolean isPaymentDate(PaymentType paymentType, LocalDate date) {
        if (PaymentType.H.equals(paymentType)) {
            return true;
        } else if (PaymentType.S.equals(paymentType)) {
            return true;
        } else return PaymentType.C.equals(paymentType);
    }

}
