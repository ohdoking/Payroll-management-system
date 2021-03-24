package com.ohdoking.payment;


import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.*;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.SalesReceiptRepository;
import com.ohdoking.payment.repository.ServiceChargeRepository;
import com.ohdoking.payment.repository.TimeCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeManageTest {

    @InjectMocks
    EmployeeManage employeeManage;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    TimeCardRepository timeCardRepository;

    @Mock
    SalesReceiptRepository salesReceiptRepository;

    @Mock
    ServiceChargeRepository serviceChargeRepository;

    /**
     * usecase 1
     *
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
        PaymentType paymentType =PaymentType.H;
        double hourlyRate = 8.0;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManage.addEmpWithHourlyRate(name, address, paymentType, hourlyRate);

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
        employeeManage.addEmpWithMonthlyPay(name, address, paymentType, monthlyPay);

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
        employeeManage.addEmpWithCommission(name, address, paymentType, monthlyPay, commissionRate);

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
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManage.addEmpWithHourlyRate(name, address, paymentType, hourlyRate));

        // then
        assertEquals("hourlyRate is missing", actual.getMessage());
        BDDMockito.verifyZeroInteractions(employeeRepository);

    }

    /**
     * usecase 2
     *
     * delete employee id
     */

    @Test
    public void givenValidEmployeeIdWhenExecuteDelEmpThenDeleteUser() {

        // given
        UUID id = UUID.randomUUID();

        willDoNothing().given(employeeRepository).deleteEmployee(any(UUID.class));

        // when
        employeeManage.delEmp(id);

        // then
        BDDMockito.verify(employeeRepository).deleteEmployee(any(UUID.class));

    }

    @Test
    public void givenInvalidEmployeeIdWhenExecuteDelEmpThenThrowInvalidParameterException() {

        // given
        UUID id = UUID.randomUUID();

        willThrow(new InvalidParameterException("id is invalid format")).given(employeeRepository).deleteEmployee(any(UUID.class));

        // when
        InvalidParameterException actual = assertThrows(InvalidParameterException.class, () -> employeeManage.delEmp(id));

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
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> employeeManage.delEmp(id));

        // then
        assertEquals("id doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).deleteEmployee(any(UUID.class));

    }

    /**
     * usecase 3
     *
     * write time card
     *
     * timecard id, date, time
     *
     */

    @Test
    public void givenValidIdAndValidEmployeeIdAndDateAndTimeWhenExecuteWriteTimeCardThenCreateTimeRecordAndConnect() {

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Double hours = 8.0;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.H)
                .hourlyRate(10.0)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);
        willDoNothing().given(timeCardRepository).createTimeCard(any(TimeCard.class));

        // when
        employeeManage.addTimeCard(employeeId, localDate, hours);

        // then
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verify(timeCardRepository).createTimeCard(any(TimeCard.class));

    }

    @Test
    public void givenNotHourlyRateEmployeeIdWhenExecuteWriteTimeCardThenThrowIncorrectPaymentTypeEmployeeException() {

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Double hours = 8.0;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.S)
                .monthlyPay(1000.0)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        // when
        IncorrectPaymentTypeEmployeeException actual = assertThrows(IncorrectPaymentTypeEmployeeException.class, () -> employeeManage.addTimeCard(employeeId, localDate, hours));

        // then
        assertEquals("The employee is not H type of employee", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyZeroInteractions(timeCardRepository);
    }


    @Test
    public void givenNullHourWhenExecuteWriteTimeCardThenThrowNullPointerException() {

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Double hours = null;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.H)
                .hourlyRate(10.0)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        // when
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManage.addTimeCard(employeeId, localDate, hours));

        // then
        assertEquals("hours is marked non-null but is null", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyZeroInteractions(timeCardRepository);
    }

    @Test
    public void givenNonExistEmployeeWhenExecuteWriteTimeCardThenThrowResourceNotFoundException(){

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Double hours = 10.0;

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(null);

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> employeeManage.addTimeCard(employeeId, localDate, hours));

        // then
        assertEquals(employeeId.toString() + " id of employee doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyNoMoreInteractions(timeCardRepository);
    }

    /**
     * usecase 4
     *
     * write sales receipt
     *
     * sales receipt id, date, amount
     *
     */

    @Test
    public void givenValidEmployeeIdAndDateAndAmountWhenExecuteAddSalesReceiptThenCreateNewSalesReceipt(){

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Integer amount = 10;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.C)
                .monthlyPay(1000.0)
                .commissionRate(0.1)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        willDoNothing().given(salesReceiptRepository).createSaleReceipt(any(SaleReceipt.class));

        // when
        employeeManage.addSalesReceipt(employeeId, localDate, amount);

        // then
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verify(salesReceiptRepository).createSaleReceipt(any(SaleReceipt.class));


    }

    @Test
    public void givenNotCommissionPaymentTypeEmployeeWhenExecuteAddSalesReceiptThenCreateNewSalesReceipt(){

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Integer amount = 10;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.H)
                .hourlyRate(10.0)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        // when
        IncorrectPaymentTypeEmployeeException actual = assertThrows(IncorrectPaymentTypeEmployeeException.class, () -> employeeManage.addSalesReceipt(employeeId, localDate, amount));

        // then
        assertEquals("The employee is not C type of employee", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyZeroInteractions(salesReceiptRepository);


    }

    @Test
    public void givenNullAmountWhenExecuteExecuteAddSalesReceiptThenThrowNullPointerException() {

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Integer amount = null;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.C)
                .monthlyPay(1000.0)
                .commissionRate(0.1)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        // when
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManage.addSalesReceipt(employeeId, localDate, amount));

        // then
        assertEquals("amount is marked non-null but is null", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyZeroInteractions(salesReceiptRepository);
    }

    @Test
    public void givenNonExistEmployeeWhenExecuteAddSalesReceiptThenThrowResourceNotFoundException(){

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Integer amount = 10;

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(null);

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> employeeManage.addSalesReceipt(employeeId, localDate, amount));

        // then
        assertEquals(employeeId.toString() + " id of employee doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyNoMoreInteractions(salesReceiptRepository);
    }


    /**
     * usecase 5
     *
     * t ad
     *
     * service charge id, amount
     *
     */

    @Test
    public void givenValidEmployeeIdAndAmountWhenExecuteAddServiceChargeThenCreateServiceCharge(){

        // given
        UUID employeeId = UUID.randomUUID();
        Integer amount = 10;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.C)
                .monthlyPay(1000.0)
                .commissionRate(0.1)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        willDoNothing().given(serviceChargeRepository).createServiceCharge(any(ServiceCharge.class));

        // when
        employeeManage.addServiceCharge(employeeId, amount);

        // then
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verify(serviceChargeRepository).createServiceCharge(any(ServiceCharge.class));


    }

    @Test
    public void givenNonExistEmployeeWhenExecuteAddServiceChargeThenThrowResourceNotFoundException(){

        // given
        UUID employeeId = UUID.randomUUID();
        Integer amount = 10;

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(null);

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> employeeManage.addServiceCharge(employeeId, amount));

        // then
        assertEquals(employeeId.toString() + " id of employee doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyNoMoreInteractions(serviceChargeRepository);
    }

    @Test
    public void givenNullAmountWhenExecuteAddServiceChargeThenThrowNullPointerException() {

        // given
        UUID employeeId = UUID.randomUUID();
        Integer amount = null;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.C)
                .monthlyPay(1000.0)
                .commissionRate(0.1)
                .build();

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(employee);

        // when
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManage.addServiceCharge(employeeId, amount));

        // then
        assertEquals("amount is marked non-null but is null", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyNoMoreInteractions(serviceChargeRepository);
    }





}
