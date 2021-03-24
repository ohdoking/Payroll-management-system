package com.ohdoking.payment;


import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.TimeCard;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.TimeCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;
import java.time.LocalDate;

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
        int id = 1;
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType =PaymentType.H;
        double hourlyRate = 8.0;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManage.addEmpWithHourlyRate(id, name, address, paymentType, hourlyRate);

        // then

        BDDMockito.verify(employeeRepository).addEmployee(any(Employee.class));

    }

    @Test
    public void givenProperEmployeeInfoType2WhenExecuteAddEmpThenAddEmployee() {

        // given
        // type 2 : employee id, name, address, S, salary
        int id = 1;
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.S;
        double monthlyPay = 300.0;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManage.addEmpWithMonthlyPay(id, name, address, paymentType, monthlyPay);

        // then
        BDDMockito.verify(employeeRepository).addEmployee(any(Employee.class));

    }

    @Test
    public void givenProperEmployeeInfoType3WhenExecuteAddEmpThenAddEmployee() {

        // given
        // type 3 : employee id, name, address, C, salary, commission rate

        int id = 1;
        String name = "Dokeun";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.C;
        double monthlyPay = 300.0;
        double commissionRate = 0.02;

        willDoNothing().given(employeeRepository).addEmployee(any(Employee.class));

        // when
        employeeManage.addEmpWithCommission(id, name, address, paymentType, monthlyPay, commissionRate);

        // then
        BDDMockito.verify(employeeRepository).addEmployee(any(Employee.class));

    }

    @Test
    public void givenInProperEmployeeInfoWhenExecuteAddEmpThenThrowError() {

        // given

        int id = 1;
        String name = "Kenny";
        String address = "Berlin";
        PaymentType paymentType = PaymentType.C;
        Double hourlyRate = null;

        // when
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManage.addEmpWithHourlyRate(id, name, address, paymentType, hourlyRate));

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
        int id = 1;

        willDoNothing().given(employeeRepository).deleteEmployee(anyInt());

        // when
        employeeManage.delEmp(id);

        // then
        BDDMockito.verify(employeeRepository).deleteEmployee(anyInt());

    }

    @Test
    public void givenInvalidEmployeeIdWhenExecuteDelEmpThenThrowInvalidParameterException() {

        // given
        int id = 123123213;

        willThrow(new InvalidParameterException("id is invalid format")).given(employeeRepository).deleteEmployee(anyInt());

        // when
        InvalidParameterException actual = assertThrows(InvalidParameterException.class, () -> employeeManage.delEmp(id));

        // then
        assertEquals("id is invalid format", actual.getMessage());
        BDDMockito.verify(employeeRepository).deleteEmployee(anyInt());

    }

    @Test
    public void givenNonExistsEmployeeIdWhenExecuteDelEmpThenThrowResourceNotFoundException() {

        // given
        int id = 123123213;

        willThrow(new ResourceNotFoundException("id doesn't exist")).given(employeeRepository).deleteEmployee(anyInt());

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> employeeManage.delEmp(id));

        // then
        assertEquals("id doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).deleteEmployee(anyInt());

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
        int id = 1;
        int employeeId = 1;
        LocalDate localDate = LocalDate.now();
        Double hours = 8.0;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.H)
                .hourlyRate(10.0)
                .build();

        given(employeeRepository.getEmployee(anyInt())).willReturn(employee);
        willDoNothing().given(timeCardRepository).createTimeCard(any(TimeCard.class));

        // when
        employeeManage.addTimeCard(id, employeeId, localDate, hours);

        // then
        BDDMockito.verify(employeeRepository).getEmployee(anyInt());
        BDDMockito.verify(timeCardRepository).createTimeCard(any(TimeCard.class));

    }

    @Test
    public void givenNotHourlyRateEmpolyeeIdWhenExecuteWriteTimeCardThenThrowException() {

        // given
        int id = 1;
        int employeeId = 2;
        LocalDate localDate = LocalDate.now();
        Double hours = 8.0;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.S)
                .monthlyPay(1000.0)
                .build();

        given(employeeRepository.getEmployee(anyInt())).willReturn(employee);

        // when
        IncorrectPaymentTypeEmployeeException actual = assertThrows(IncorrectPaymentTypeEmployeeException.class, () -> employeeManage.addTimeCard(id, employeeId, localDate, hours));

        // then
        assertEquals("The employee is not H type of employee", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(anyInt());
        BDDMockito.verifyZeroInteractions(timeCardRepository);
    }


    @Test
    public void givenNullHourWhenExecuteWriteTimeCardThenThrowException() {

        // given
        int id = 1;
        int employeeId = 2;
        LocalDate localDate = LocalDate.now();
        Double hours = null;

        Employee employee = Employee.builder()
                .id(employeeId)
                .name("Dokeun")
                .address("berlin")
                .paymentType(PaymentType.H)
                .hourlyRate(10.0)
                .build();

        given(employeeRepository.getEmployee(anyInt())).willReturn(employee);

        // when
        NullPointerException actual = assertThrows(NullPointerException.class, () -> employeeManage.addTimeCard(id, employeeId, localDate, hours));

        // then
        assertEquals("hours is marked non-null but is null", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(anyInt());
        BDDMockito.verifyZeroInteractions(timeCardRepository);
    }



}
