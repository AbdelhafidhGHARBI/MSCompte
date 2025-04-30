package tn.esprit.mscompte.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "comptes")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String numero;  // IBAN ou numéro de compte

    @Column(nullable = false)
    String titulaire;

    String banqueId;  // ID externe de la banque associée

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TypeCompte typeCompte;

    Long solde;

    Boolean actif;

    String ageTitulaire; // on l'utilise dans aucun DTO

    @Column(updatable = false)
    LocalDateTime dateCreation;

    LocalDateTime dateMiseAJour;
}
