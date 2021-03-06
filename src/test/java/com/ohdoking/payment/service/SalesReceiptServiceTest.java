package com.ohdoking.payment.service;

import com.ohdoking.payment.exception.IncorrectPaymentTypeEmployeeException;
import com.ohdoking.payment.exception.ResourceNotFoundException;
import com.ohdoking.payment.model.Employee;
import com.ohdoking.payment.model.PaymentType;
import com.ohdoking.payment.model.SaleReceipt;
import com.ohdoking.payment.repository.EmployeeRepository;
import com.ohdoking.payment.repository.SalesReceiptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class SalesReceiptServiceTest {

    @InjectMocks
    SalesReceiptService salesReceiptService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    SalesReceiptRepository salesReceiptRepository;

    /**
     * usecase 4
     * <p>
     * write sales receipt
     * <p>
     * sales receipt id, date, amount
     */

    @Test
    public void givenValidEmployeeIdAndDateAndAmountWhenExecuteAddSalesReceiptThenCreateNewSalesReceipt() {

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
        salesReceiptService.addSalesReceipt(employeeId, localDate, amount);

        // then
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verify(salesReceiptRepository).createSaleReceipt(any(SaleReceipt.class));


    }

    @Test
    public void givenNotCommissionPaymentTypeEmployeeWhenExecuteAddSalesReceiptThenCreateNewSalesReceipt() {

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
        IncorrectPaymentTypeEmployeeException actual = assertThrows(IncorrectPaymentTypeEmployeeException.class, () -> salesReceiptService.addSalesReceipt(employeeId, localDate, amount));

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
        NullPointerException actual = assertThrows(NullPointerException.class, () -> salesReceiptService.addSalesReceipt(employeeId, localDate, amount));

        // then
        assertEquals("amount is marked non-null but is null", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyZeroInteractions(salesReceiptRepository);
    }

    @Test
    public void givenNonExistEmployeeWhenExecuteAddSalesReceiptThenThrowResourceNotFoundException() {

        // given
        UUID employeeId = UUID.randomUUID();
        LocalDate localDate = LocalDate.now();
        Integer amount = 10;

        given(employeeRepository.getEmployee(any(UUID.class))).willReturn(null);

        // when
        ResourceNotFoundException actual = assertThrows(ResourceNotFoundException.class, () -> salesReceiptService.addSalesReceipt(employeeId, localDate, amount));

        // then
        assertEquals(employeeId.toString() + " id of employee doesn't exist", actual.getMessage());
        BDDMockito.verify(employeeRepository).getEmployee(any(UUID.class));
        BDDMockito.verifyNoMoreInteractions(salesReceiptRepository);
    }
}
