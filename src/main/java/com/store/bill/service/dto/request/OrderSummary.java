package com.store.bill.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {
    @Schema(description = "User id requesting order", required = true)
    @NotNull
    @Positive(message = "User id must be supplied and should be positive.")
    private Long userId;
    @Schema(description = "Products", required = true)
    @NotEmpty(message = "Atleast one product must be purchased.")
    @Valid
    private List<OrderProductSummary> orderProductSummaries;
}
