package tn.esprit.mscompte.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.entities.TypeCompte;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface ICompteService {
    CompteDto create(CompteDto compteDto);

    CompteDto findById(Long id);

    CompteDto findByNumero(String numero);

    List<CompteDto> findByTitulaire(String titulaire);

    Page<CompteDto> findAll(Pageable pageable);

    CompteDto update(Long id, CompteDto compteDto);

    CompteDto partialUpdate(Long id, Map<String, Object> updates);

    void delete(Long id);

    Page<CompteDto> findByBanque(String banqueId, Pageable pageable);

    CompteDto crediter(Long id, BigDecimal montant);

    CompteDto debiter(Long id, BigDecimal montant);

    void transferer(Long sourceId, Long destinationId, BigDecimal montant);

    CompteDto activerDesactiver(Long id, boolean activer);

    List<CompteDto> getComptesByType(TypeCompte type, int page, int size, String sortBy, String direction);

    Page<CompteDto> findByType(TypeCompte type, Pageable pageable);
}