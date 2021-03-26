package com.ohdoking.payment.repository;

import com.ohdoking.payment.model.SaleReceipt;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class SalesReceiptRepository {
    public void createSaleReceipt(SaleReceipt saleReceipt) {
    }

    public List<SaleReceipt> getListOfSalesReceiptById(UUID employeeId, LocalDate date) {
        return List.of(SaleReceipt.builder().build());
    }
}
