package com.ohdoking.payment.service;

import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.ServiceCharge;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.ServiceChargeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class ServiceChargeServiceTest {
    @InjectMocks
    ServiceChargeService serviceChargeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    ServiceChargeRepository serviceChargeRepository;

    /**
     * usecase 5
     * <p>
     * write service charge
     * <p>
     * service charge id, amount
     */

    @Test
    public void givenValidEmployeeIdAndAmountWhenExecuteAddServiceChargeThenCreateServiceCharge() {

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
        serviceChargeService.addServiceCharge(employeeId, amount);

        // then
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verify(serviceChargeRepository).createServiceCharge(any(ServiceCharge.class));


    }

    @Test
    public void givenNonExistEmployeeWhenExecuteAddServiceChargeThenThrowResourceNotFoundException() {

        // given
        UUID employeeId = UUID.randomUUID();
        Integer amount = 10;

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(null);

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> serviceChargeService.addServiceCharge(employeeId, amount));

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
        NullPointerException actual = assertThrows(NullPointerException.class, () -> serviceChargeService.addServiceCharge(employeeId, amount));

        // then
        assertEquals("amount is marked non-null but is null", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyNoMoreInteractions(serviceChargeRepository);
    }

}
