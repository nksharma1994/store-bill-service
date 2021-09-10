package com.store.bill.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillSummary {
    @Schema(description = "User details")
    private User user;
    private List<ProductSummary> productSummaries;
    @Schema(description = "Total price of products")
    private Double grossPrice;
    @Schema(description = "Discount applied")
    private Double discount;
    @Schema(description = "Total price to be paid")
    private Double netPayablePrice;
}
