package com.ohdoking.payment;


import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidParameterException;

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
        String name = "Kenny";
        String address = "Berlin";
        String paymentType = "H";
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
        String name = "Kenny";
        String address = "Berlin";
        String paymentType = "S";
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
        String name = "Kenny";
        String address = "Berlin";
        String paymentType = "C";
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
        String paymentType = "C";
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


}
