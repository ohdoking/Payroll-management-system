package com.ohdoking.payment.repository;

import com.ohdoking.payment.model.Employee;

import java.util.List;
import java.util.UUID;

public class EmployeeRepository {
    public void addEmployee(Employee employee){

    }

    public void deleteEmployee(UUID employeeId){

    }

    public Employee getEmployee(UUID id) {
        return Employee.builder().build();
    }

    public List<Employee> getListOfEmployee() {
        return List.of(Employee.builder().build());
    }
}
