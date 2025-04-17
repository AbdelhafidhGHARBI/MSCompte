package tn.esprit.mscompte.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OperationDto(
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal montant
) {}
