package tn.esprit.mscompte.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.dto.OperationDto;
import tn.esprit.mscompte.dto.TransfertDto;
import tn.esprit.mscompte.entities.TypeCompte;
import tn.esprit.mscompte.services.ICompteService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/comptes")
@Tag(name = "Compte", description = "API de gestion des comptes bancaires")
public class CompteRestController {

    private final ICompteService compteService;

    @Autowired
    public CompteRestController(ICompteService compteService) {
        this.compteService = compteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouveau compte")
    @ApiResponse(responseCode = "201", description = "Compte créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "Le numéro de compte existe déjà")
    public CompteDto create(@Valid @RequestBody CompteDto compteDto) {
        return compteService.create(compteDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trouver un compte par son ID")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    public CompteDto getById(@PathVariable Long id) {
        return compteService.findById(id);
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Trouver un compte par son numéro")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    public CompteDto getByNumero(@PathVariable String numero) {
        return compteService.findByNumero(numero);
    }

    @GetMapping("/titulaire/{titulaire}")
    @Operation(summary = "Trouver les comptes d'un titulaire")
    public List<CompteDto> getByTitulaire(@PathVariable String titulaire) {
        return compteService.findByTitulaire(titulaire);
    }

    @GetMapping
    @Operation(summary = "Lister tous les comptes (avec pagination)")
    public Page<CompteDto> getAll(
            @Parameter(description = "Paramètres de pagination")
            @PageableDefault(size = 10, sort = "titulaire") Pageable pageable) {
        return compteService.findAll(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un compte")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    public CompteDto update(@PathVariable Long id, @Valid @RequestBody CompteDto compteDto) {
        return compteService.update(id, compteDto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour partiellement un compte")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    public CompteDto partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<@NotBlank String, Object> updates) {
        return compteService.partialUpdate(id, updates);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un compte")
    @ApiResponse(responseCode = "204", description = "Compte supprimé")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        compteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/banque/{banqueId}")
    @Operation(summary = "Lister les comptes d'une banque")
    public Page<CompteDto> getByBanque(
            @PathVariable String banqueId,
            @PageableDefault(size = 10) Pageable pageable) {
        return compteService.findByBanque(banqueId, pageable);
    }


    @PostMapping("/{id}/crediter")
    @Operation(summary = "Créditer un compte")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    @ApiResponse(responseCode = "400", description = "Montant invalide")
    public CompteDto crediter(
            @PathVariable Long id,
            @Valid @RequestBody OperationDto operation) {
        return compteService.crediter(id, operation.montant());
    }

    @PostMapping("/{id}/debiter")
    @Operation(summary = "Débiter un compte")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    @ApiResponse(responseCode = "400", description = "Montant invalide ou fonds insuffisants")
    public CompteDto debiter(
            @PathVariable Long id,
            @Valid @RequestBody OperationDto operation) {
        return compteService.debiter(id, operation.montant());
    }

    @PostMapping("/transfert")
    @Operation(summary = "Effectuer un transfert entre deux comptes")
    @ApiResponse(responseCode = "200", description = "Transfert effectué avec succès")
    @ApiResponse(responseCode = "404", description = "Compte source ou destination non trouvé")
    @ApiResponse(responseCode = "400", description = "Montant invalide ou fonds insuffisants")
    public ResponseEntity<Void> transferer(@Valid @RequestBody TransfertDto transfert) {
        compteService.transferer(transfert.sourceId(), transfert.destinationId(), transfert.montant());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/actif")
    @Operation(summary = "Activer ou désactiver un compte")
    @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    public CompteDto activerDesactiver(
            @PathVariable Long id,
            @RequestParam boolean actif) {
        return compteService.activerDesactiver(id, actif);
    }

    // ✅ Méthode bien séparée
    @GetMapping("/par-type")
    @Operation(summary = "Lister les comptes par type (avec pagination et tri)")
    public Page<CompteDto> getComptesByType(
            @RequestParam TypeCompte type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return compteService.findByType(type, pageable);
    }

}