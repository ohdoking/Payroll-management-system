package com.ohdoking.payment;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeManageTest {

    EmployeeManage employeeManage;

    @BeforeEach
    public void init() {
        employeeManage = new EmployeeManage();
    }

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

        // when
        String actual = employeeManage.addEmpWithHourlyRate(id, name, address, paymentType, hourlyRate);

        // then
        assertEquals("success", actual);

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

        // when
        String actual = employeeManage.addEmpWithMonthlyPay(id, name, address, paymentType, monthlyPay);

        // then
        assertEquals("success", actual);

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

        // when
        String actual = employeeManage.addEmpWithCommission(id, name, address, paymentType, monthlyPay, commissionRate);

        // then
        assertEquals("success", actual);

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

    }


}
