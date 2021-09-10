package com.store.bill.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummary {
    @Schema(description = "Product id")
    private Long id;
    @Schema(description = "Product name")
    private String name;
    @Schema(description = "Category name")
    private String categoryName;
    @Schema(description = "Order quantity")
    private Integer quantity;
    @Schema(description = "Price per piece")
    private Double price;
    @Schema(description = "Total price = quantity*price")
    private Double grossPrice;
}
