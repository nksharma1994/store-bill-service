package com.store.bill.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductSummary {
    @Schema(description = "Product id", required = true)
    @NotNull
    @Positive(message = "Product id must be supplied and should be positive.")
    private Long productId;
    @Schema(description = "Order quantity", required = true)
    @Positive(message = "minimum 1 quantity must be supplied.")
    private Integer quantity;
}
