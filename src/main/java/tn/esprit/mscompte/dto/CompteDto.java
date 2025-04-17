package tn.esprit.mscompte.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import tn.esprit.mscompte.entities.TypeCompte;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Objet représentant un compte bancaire")
public record CompteDto(
        @Schema(description = "ID unique du compte", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @NotBlank(message = "Le numéro de compte est obligatoire")
        @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$",
                message = "Format IBAN invalide")
        @Schema(description = "Numéro IBAN du compte", example = "FR7630006000011234567890189")
        String numero,

        @NotBlank(message = "Le nom du titulaire est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom du titulaire doit contenir entre 2 et 100 caractères")
        @Schema(description = "Nom du titulaire du compte", example = "Jean Dupont")
        String titulaire,

        @NotBlank(message = "L'ID de la banque est obligatoire")
        @Schema(description = "ID de la banque associée au compte", example = "507f1f77bcf86cd799439011")
        String banqueId,

        @NotNull(message = "Le type de compte est obligatoire")
        @Schema(description = "Type de compte", example = "COURANT")
        TypeCompte type,

        @PositiveOrZero(message = "Le solde ne peut pas être négatif")
        @Schema(description = "Solde actuel du compte", example = "1500.00")
        BigDecimal solde,

        @Schema(description = "Indique si le compte est actif", example = "true")
        Boolean actif,

        @Schema(description = "Date de création", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime dateCreation,

        @Schema(description = "Date de dernière modification", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime dateMiseAJour
) {
    // Méthode factory pour la création
    public static CompteDto create(String numero, String titulaire, String banqueId, TypeCompte type, BigDecimal solde) {
        return new CompteDto(null, numero, titulaire, banqueId, type, solde, true, null, null);
    }
}