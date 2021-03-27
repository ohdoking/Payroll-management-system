package com.ohdoking.payment.service;

import com.ohdoking.payment.model.*;
import com.ohdoking.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    EmployeeService employeeService;

    @Mock
    ServiceChargeService serviceChargeService;

    @Mock
    TimeCardService timeCardService;

    @Mock
    SalesReceiptService salesReceiptService;

    /**
     * usecase 7
     * <p>
     * pay salary to employee with the way that they choose
     * <p>
     * payday 트랜잭션을 받으면, 시스템은 지정된 날짜에 임금을 받아야할 직원을 모두 가려낸다.
     * 그리고 그들이 얼마나 받아야 하는지 결정하고, 이들이 선택한 지급 방식으로 임금을 지급한다.
     * <p>
     * hourly type ->
     * 1. every friday
     * 2. if they works over 8, hourly rate * 1.5
     * <p>
     * salary type ->
     * 1. weekday in last week of month
     * <p>
     * commission type ->
     * 1. once in 2 weeks
     * <p>
     * 3 type of getting payment
     * 1. leave salary to salary manager
     * 2. directly
     * 3. via mail
     */

    @Test
    public void givenPaymentTypeIsHourlyTypeWhenExecutePaydayThenPaySalary() {

        // given
        LocalDate now = LocalDate.now();

        UUID elsaEmployeeId = UUID.randomUUID();

        given(employeeService.getListOfEmployee()).willReturn(
                List.of(
                        Employee
                                .builder()
                                .id(elsaEmployeeId)
                                .name("Elsa")
                                .address("Berlin")
                                .paymentType(PaymentType.H)
                                .hourlyRate(10.0)
                                .paymentWay(PaymentWay.LEAVE_SALARY_TO_MANAGER)
                                .build()
                )
        );

        given(timeCardService.getListOfTimeCardById(any(UUID.class), any(LocalDate.class))).willReturn(
                List.of(
                        TimeCard.builder()
                                .id(UUID.randomUUID())
                                .employeeId(elsaEmployeeId)
                                .hours(6.0)
                                .date(LocalDate.now().minus(Period.ofDays(1)))
                                .build(),
                        TimeCard.builder()
                                .id(UUID.randomUUID())
                                .employeeId(elsaEmployeeId)
                                .hours(6.0)
                                .date(LocalDate.now().minus(Period.ofDays(2)))
                                .build(),
                        TimeCard.builder()
                                .id(UUID.randomUUID())
                                .employeeId(elsaEmployeeId)
                                .hours(6.0)
                                .date(LocalDate.now().minus(Period.ofDays(3)))
                                .build()
                )
        );

        given(serviceChargeService.findServiceChargeById(any(UUID.class))).willReturn(ServiceCharge.builder()
                .id(UUID.randomUUID())
                .employeeId(elsaEmployeeId)
                .amount(10)
                .build()
        );

        willDoNothing().given(paymentRepository).savePaymentt(any(Payment.class));

        // when
        paymentService.payday(now);

        // then
        verify(employeeService).getListOfEmployee();
        verify(timeCardService).getListOfTimeCardById(any(UUID.class), any(LocalDate.class));
        verifyNoMoreInteractions(salesReceiptService);
        verify(serviceChargeService).findServiceChargeById(any(UUID.class));
        verify(paymentRepository).savePaymentt(any(Payment.class));

    }

    @Test
    public void givenPaymentTypeIsCommissionTypeWhenExecutePaydayThenPaySalary() {

        // given
        LocalDate now = LocalDate.now();

        UUID kennyEmployeeId = UUID.randomUUID();

        given(employeeService.getListOfEmployee()).willReturn(
                List.of(
                        Employee
                                .builder()
                                .id(kennyEmployeeId)
                                .name("Kenny")
                                .address("NY")
                                .paymentType(PaymentType.C)
                                .monthlyPay(1000.0)
                                .commissionRate(0.01)
                                .paymentWay(PaymentWay.DIRECT)
                                .build()
                )
        );

        given(salesReceiptService.getListOfSalesReceiptById(any(UUID.class), any(LocalDate.class))).willReturn(
                List.of(
                        SaleReceipt.builder()
                                .id(UUID.randomUUID())
                                .employeeId(kennyEmployeeId)
                                .amount(100)
                                .date(LocalDate.now().minus(Period.ofDays(1)))
                                .build(),
                        SaleReceipt.builder()
                                .id(UUID.randomUUID())
                                .employeeId(kennyEmployeeId)
                                .amount(200)
                                .date(LocalDate.now().minus(Period.ofDays(2)))
                                .build(),
                        SaleReceipt.builder()
                                .id(UUID.randomUUID())
                                .employeeId(kennyEmployeeId)
                                .amount(250)
                                .date(LocalDate.now().minus(Period.ofDays(3)))
                                .build()
                )
        );

        given(serviceChargeService.findServiceChargeById(any(UUID.class))).willReturn(ServiceCharge.builder()
                .id(UUID.randomUUID())
                .employeeId(kennyEmployeeId)
                .amount(10)
                .build()
        );

        willDoNothing().given(paymentRepository).savePaymentt(any(Payment.class));

        // then
        paymentService.payday(now);


        // then
        verify(employeeService).getListOfEmployee();
        verifyNoMoreInteractions(timeCardService);
        verify(salesReceiptService).getListOfSalesReceiptById(any(UUID.class), any(LocalDate.class));
        verify(serviceChargeService).findServiceChargeById(any(UUID.class));
        verify(paymentRepository).savePaymentt(any(Payment.class));


    }

    @Test
    public void givenPaymentTypeIsSalaryTypeWhenExecutePaydayThenPaySalary() {

        // given
        LocalDate now = LocalDate.now();

        UUID dokeunEmployeeId = UUID.randomUUID();

        given(employeeService.getListOfEmployee()).willReturn(
                List.of(
                        Employee
                                .builder()
                                .id(dokeunEmployeeId)
                                .name("Dokeun")
                                .address("Busan")
                                .paymentType(PaymentType.S)
                                .monthlyPay(10000.0)
                                .paymentWay(PaymentWay.MAIL)
                                .build()
                )
        );

        given(serviceChargeService.findServiceChargeById(any(UUID.class))).willReturn(ServiceCharge.builder()
                .id(UUID.randomUUID())
                .employeeId(dokeunEmployeeId)
                .amount(10)
                .build()
        );

        willDoNothing().given(paymentRepository).savePaymentt(any(Payment.class));

        // when
        paymentService.payday(now);

        // then
        verify(employeeService).getListOfEmployee();
        verifyNoMoreInteractions(timeCardService);
        verifyNoMoreInteractions(salesReceiptService);
        verify(serviceChargeService).findServiceChargeById(any(UUID.class));
        verify(paymentRepository).savePaymentt(any(Payment.class));


    }

//    @Test
//    public void givenPaymentTypeIsHourlyTypeWhenExecutePaydayThenPaySalary() {
//
//        // given
//        LocalDate now = LocalDate.now();
//
//        // TODO
//        // 1. get list of employee
//        // 2. check whether payment date or not
//        // 3. calculate salary based on paymentType
//        // 3-1 hourly type
//        // 3-1-1 check whether today is friday
//        // 3-1-2 get last week salary from timecard
//        // 3-1-3 calculate
//        // 3-2 salary type
//        // 3-2-1 check whether today is weekday in last week of month
//        // 3-2-2 get salary from employee
//        // 3-3 commission type
//        // 3-3-1 check whether when is the last payment date
//        // 3-3-2 get salary from employee
//        // 3-3-3 calculate sales receipt from last payment date and add in salary
//        // 4. check whether the employee is part of service charge
//        // 4-1 if it,s true then reduce the price for this from salary
//        // 5. write this in payment table.
//        // 6. pay salary with what employee want.
//        // 6-1. get payment way from Employee
//
//        UUID dokeunEmployeeId = UUID.randomUUID();
//        UUID elsaEmployeeId = UUID.randomUUID();
//        UUID kennyEmployeeId = UUID.randomUUID();
//
//        given(employeeRepository.getListOfEmployee()).willReturn(
//                List.of(
//                        Employee
//                                .builder()
//                                .id(dokeunEmployeeId)
//                                .name("Dokeun")
//                                .address("Busan")
//                                .paymentType(PaymentType.S)
//                                .monthlyPay(10000.0)
//                                .paymentWay(PaymentWay.MAIL)
//                                .build(),
//                        Employee
//                                .builder()
//                                .id(elsaEmployeeId)
//                                .name("Elsa")
//                                .address("Berlin")
//                                .paymentType(PaymentType.H)
//                                .hourlyRate(10.0)
//                                .paymentWay(PaymentWay.LEAVE_SALARY_TO_MANAGER)
//                                .build(),
//                        Employee
//                                .builder()
//                                .id(kennyEmployeeId)
//                                .name("Kenny")
//                                .address("NY")
//                                .paymentType(PaymentType.C)
//                                .monthlyPay(1000.0)
//                                .commissionRate(0.01)
//                                .paymentWay(PaymentWay.DIRECT)
//                                .build()
//                )
//        );
//
//        given(timeCardRepository.getListOfTimeCardById(any(UUID.class), any(LocalDate.class))).willReturn(
//                List.of(
//                        TimeCard.builder()
//                                .id(UUID.randomUUID())
//                                .employeeId(dokeunEmployeeId)
//                                .hours(6.0)
//                                .date(LocalDate.now().minus(Period.ofDays(1)))
//                                .build(),
//                        TimeCard.builder()
//                                .id(UUID.randomUUID())
//                                .employeeId(dokeunEmployeeId)
//                                .hours(6.0)
//                                .date(LocalDate.now().minus(Period.ofDays(2)))
//                                .build(),
//                        TimeCard.builder()
//                                .id(UUID.randomUUID())
//                                .employeeId(dokeunEmployeeId)
//                                .hours(6.0)
//                                .date(LocalDate.now().minus(Period.ofDays(3)))
//                                .build()
//                )
//        );
//
//        given(salesReceiptRepository.getListOfSalesReceiptById(any(UUID.class), any(LocalDate.class))).willReturn(
//                List.of(
//                        SaleReceipt.builder()
//                                .id(UUID.randomUUID())
//                                .employeeId(kennyEmployeeId)
//                                .amount(100)
//                                .date(LocalDate.now().minus(Period.ofDays(1)))
//                                .build(),
//                        SaleReceipt.builder()
//                                .id(UUID.randomUUID())
//                                .employeeId(kennyEmployeeId)
//                                .amount(200)
//                                .date(LocalDate.now().minus(Period.ofDays(2)))
//                                .build(),
//                        SaleReceipt.builder()
//                                .id(UUID.randomUUID())
//                                .employeeId(kennyEmployeeId)
//                                .amount(250)
//                                .date(LocalDate.now().minus(Period.ofDays(3)))
//                                .build()
//                )
//        );
//
//        willDoNothing().given(paymentRepository).savePaymentt(any(Payment.class));
//
//        // then
//        employeeManager.payday(now);
//
//
//
//    }
}
