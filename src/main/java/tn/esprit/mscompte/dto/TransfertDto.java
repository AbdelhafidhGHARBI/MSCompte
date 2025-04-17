package tn.esprit.mscompte.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransfertDto(
        @NotNull Long sourceId,
        @NotNull Long destinationId,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal montant
) {}

