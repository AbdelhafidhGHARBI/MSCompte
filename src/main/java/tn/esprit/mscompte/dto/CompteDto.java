package tn.esprit.mscompte.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tn.esprit.mscompte.entities.TypeCompte;

import java.time.LocalDateTime;

public record CompteDto(
        Long id,
        String numero,
        String titulaire,
        String banqueId,
        BanqueDto banqueDto,
        TypeCompte typeCompte,
        Long solde,
        Boolean actif,
        LocalDateTime dateCreation,
        LocalDateTime dateMiseAJour
) {}
