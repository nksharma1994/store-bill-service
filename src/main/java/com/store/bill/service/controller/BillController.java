package com.store.bill.service.controller;

import com.store.bill.service.dto.request.OrderSummary;
import com.store.bill.service.dto.response.BillSummary;
import com.store.bill.service.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "store/bill")
@Validated
public class BillController {
    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @Operation(summary = "Get Bill summary for order summary",
            description = "This endpoint will generate bill summary for supplied list of products in order summary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill summary")
    })
    @PostMapping("summary")
    public ResponseEntity<BillSummary> getBillSummary(@Valid @RequestBody OrderSummary orderSummary) {
        log.info("Get bill summary called for " + orderSummary);
        return ResponseEntity.ok(billService.getBillSummary(orderSummary));
    }
}
