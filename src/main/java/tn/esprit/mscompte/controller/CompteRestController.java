package tn.esprit.mscompte.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mscompte.client.BanqueFeignClient;
import tn.esprit.mscompte.dto.BanqueDto;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.services.ICompteService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@CrossOrigin
public class CompteRestController {

    private final ICompteService compteService;
    private final BanqueFeignClient banqueFeignClient;

    @PostMapping
    public ResponseEntity<CompteDto> create(@RequestBody @Valid CompteDto compteDto) {
        // Verify bank exists
        BanqueDto banqueDto = banqueFeignClient.getBanqueById(compteDto.banqueId());
        CompteDto createdCompte = compteService.create(compteDto);

        // Update the bank with the new account
        banqueFeignClient.addCompteToBanque(compteDto.banqueId(), createdCompte.id());

        // Return with bank details
        CompteDto responseDto = new CompteDto(
                createdCompte.id(),
                createdCompte.numero(),
                createdCompte.titulaire(),
                createdCompte.banqueId(),
                banqueDto,
                createdCompte.typeCompte(),
                createdCompte.solde(),
                createdCompte.actif(),
                createdCompte.dateCreation(),
                createdCompte.dateMiseAJour()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompteDto> findById(@PathVariable Long id) {
        CompteDto compteDto = compteService.findById(id);
        // Fetch bank details
        BanqueDto banqueDto = banqueFeignClient.getBanqueById(compteDto.banqueId());
        CompteDto responseDto = new CompteDto(
                compteDto.id(),
                compteDto.numero(),
                compteDto.titulaire(),
                compteDto.banqueId(),
                banqueDto,
                compteDto.typeCompte(),
                compteDto.solde(),
                compteDto.actif(),
                compteDto.dateCreation(),
                compteDto.dateMiseAJour()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CompteDto>> findAll() {
        List<CompteDto> comptes = compteService.findAll();
        return ResponseEntity.ok(comptes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompteDto> update(@PathVariable Long id, @RequestBody @Valid CompteDto compteDto) {
        CompteDto updatedCompte = compteService.update(id, compteDto);
        return ResponseEntity.ok(updatedCompte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        compteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}