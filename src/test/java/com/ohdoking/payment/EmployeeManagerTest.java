package com.ohdoking.payment;


import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.*;
import com.ohdoking.payment.repository.*;
import com.ohdoking.payment.service.SalesReceiptService;
import com.ohdoking.payment.service.ServiceChargeService;
import com.ohdoking.payment.service.TimeCardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeManagerTest {

    @InjectMocks
    EmployeeManager employeeManager;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    ServiceChargeService serviceChargeService;

    @Mock
    TimeCardService timeCardService;

    @Mock
    SalesReceiptService salesReceiptService;

    /**
     * usecase 1
     * <p>
     * add employee id, name, address, payment type, hourly rate, commission rate
     * <p>
     * type 1 : employee id, name, address, H, hourly rate
     * type 2 : employee id, name, address, S, salary
     * type 3 : employee id, name, address, C, salary, commission rate
     */

    @Test
    public void givenProperEmployeeInfoType1WhenExecuteAddEmpThenAddEmployee() {

        // given
        // type 1 : employee id, name, address, H, hourly rate
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.H;
        double hourlyRate = 8.0;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManager.addEmpWithHourlyRate(name, address, paymentType, hourlyRate);

        // then

        BDDMockito.verify(employeeRepository).addEmployee(any(Employee.class));

    }

    @Test
    public void givenProperEmployeeInfoType2WhenExecuteAddEmpThenAddEmployee() {

        // given
        // type 2 : employee id, name, address, S, salary
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.S;
        double monthlyPay = 300.0;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManager.addEmpWithMonthlyPay(name, address, paymentType, monthlyPay);

        // then
        BDDMockito.verify(employeeRepository).addEmployee(any(Employee.class));

    }

    @Test
    public void givenProperEmployeeInfoType3WhenExecuteAddEmpThenAddEmployee() {

        // given
        // type 3 : employee id, name, address, C, salary, commission rate
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.C;
        double monthlyPay = 300.0;
        double commissionRate = 0.02;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManager.addEmpWithCommission(name, address, paymentType, monthlyPay, commissionRate);

        // then
        BDDMockito.verify(employeeRepository).addEmployee(any(Employee.class));

    }

    @Test
    public void givenInProperEmployeeInfoWhenExecuteAddEmpThenThrowError() {

        // given
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.C;
        Double hourlyRate = null;

        // when
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManager.addEmpWithHourlyRate(name, address, paymentType, hourlyRate));

        // then
        assertEquals("hourlyRate is missing", actual.getMessage());
        BDDMockito.verifyZeroInteractions(employeeRepository);

    }

    /**
     * usecase 2
     * <p>
     * delete employee id
     */

    @Test
    public void givenValidEmployeeIdWhenExecuteDelEmpThenDeleteUser() {

        // given
        UUID id = UUID.randomUUID();

        willDoNothing().given(employeeRepository).deleteEmployee(any(UUID.class));

        // when
        employeeManager.delEmp(id);

        // then
        BDDMockito.verify(employeeRepository).deleteEmployee(any(UUID.class));

    }

    @Test
    public void givenInvalidEmployeeIdWhenExecuteDelEmpThenThrowInvalidParameterException() {

        // given
        UUID id = UUID.randomUUID();

        willThrow(new InvalidParameterException("id is invalid format")).given(employeeRepository).deleteEmployee(any(UUID.class));

        // when
        InvalidParameterException actual = assertThrows(InvalidParameterException.class, () -> employeeManager.delEmp(id));

        // then
        assertEquals("id is invalid format", actual.getMessage());
        BDDMockito.verify(employeeRepository).deleteEmployee(any(UUID.class));

    }

    @Test
    public void givenNonExistsEmployeeIdWhenExecuteDelEmpThenThrowResourceNotFoundException() {

        // given
        UUID id = UUID.randomUUID();

        willThrow(new ResourceNotFoundException("id doesn't exist")).given(employeeRepository).deleteEmployee(any(UUID.class));

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> employeeManager.delEmp(id));

        // then
        assertEquals("id doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).deleteEmployee(any(UUID.class));

    }







    /**
     * usecase 6
     *
     * Change employee information
     *
     * ChgEmp id, name type, name
     *  - change name of employee
     * ChgEmp id, address type, address
     *  - change name of address
     * ChgEmp id, hourly type, hourly
     *  - change to hourly type employee
     * ChgEmp id, salaried type, salaried
     *  - change to salary type employee
     * ChgEmp id, commissioned type, commissioned
     *  - change to commission type employee
     * ChgEmp id, hold
     *  - leave salary to salary manager
     * ChgEmp id, direct, bank, account
     *  - get salary directly
     * ChgEmp id, mail type, mail address
     *  - get salary via mail
     * ChgEmp id, member type, service change id, dues, service charge portion
     *  - add from service charge
     * ChgEmp id, nomember type
     *  - remove from service charge
     *
     */

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

        given(employeeRepository.getListOfEmployee()).willReturn(
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
        employeeManager.payday(now);

        // then
        verify(employeeRepository).getListOfEmployee();
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

        given(employeeRepository.getListOfEmployee()).willReturn(
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
        employeeManager.payday(now);


        // then
        verify(employeeRepository).getListOfEmployee();
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

        given(employeeRepository.getListOfEmployee()).willReturn(
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
        employeeManager.payday(now);

        // then
        verify(employeeRepository).getListOfEmployee();
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
