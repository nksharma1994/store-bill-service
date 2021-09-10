package com.store.bill.service.service;

import com.store.bill.service.dto.request.OrderSummary;
import com.store.bill.service.dto.response.BillSummary;

public interface BillService {
    BillSummary getBillSummary(OrderSummary orderSummary);
}
