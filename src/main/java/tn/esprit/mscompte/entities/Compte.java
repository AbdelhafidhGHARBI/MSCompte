package tn.esprit.mscompte.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "comptes")
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compte implements Serializable {

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
    tn.esprit.mscompte.entities.TypeCompte type;

    BigDecimal solde;

    Boolean actif;

    @Column(updatable = false)
    LocalDateTime dateCreation;

    LocalDateTime dateMiseAJour;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.actif == null) {
            this.actif = true;
        }
        if (this.solde == null) {
            this.solde = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setBanqueId(String banqueId) {
        this.banqueId = banqueId;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }
    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

}

//@Entity
//@Table(name = "comptes")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Compte implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    @Column(nullable = false, unique = true)
//    String numero;  // IBAN ou numéro de compte
//
//    @Column(nullable = false)
//    String titulaire;
//
//    public String getTitulaire() {
//        return titulaire;
//    }
//
//    public void setTitulaire(String titulaire) {
//        this.titulaire = titulaire;
//    }
//
//    String banqueId;  // ID externe de la banque associée
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    tn.esprit.mscompte.entities.TypeCompte type;
//
//    BigDecimal solde;
//
//    Boolean actif;
//
//    @Column(updatable = false)
//    LocalDateTime dateCreation;
//
//    LocalDateTime dateMiseAJour;
//
//    @PrePersist
//    protected void onCreate() {
//        this.dateCreation = LocalDateTime.now();
//        if (this.actif == null) {
//            this.actif = true;
//        }
//        if (this.solde == null) {
//            this.solde = BigDecimal.ZERO;
//        }
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.dateMiseAJour = LocalDateTime.now();
//    }
//
//}