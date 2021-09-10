package com.store.bill.service.dto.response;

import com.store.bill.service.entity.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Schema(description = "User id")
    private Long id;
    @Schema(description = "User name")
    private String name;
    @Schema(description = "User type")
    private UserType type;
    @Schema(description = "Joining date")
    private LocalDate joiningDate;
}
