package tn.esprit.mscompte.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.entities.Compte;
import tn.esprit.mscompte.entities.TypeCompte;
import tn.esprit.mscompte.exceptions.CompteInactifException;
import tn.esprit.mscompte.exceptions.FondsInsuffisantsException;
import tn.esprit.mscompte.mappers.CompteMapper;
import tn.esprit.mscompte.repositories.CompteRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompteServiceImpl implements ICompteService {

    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;

    @Override
    @Transactional
    public CompteDto create(CompteDto compteDto) {
        Compte compte = compteMapper.toEntity(compteDto);
        Compte savedCompte = compteRepository.save(compte);
        return compteMapper.toDto(savedCompte);
    }

    @Override
    public CompteDto findById(Long id) {
        return compteRepository.findById(id)
                .map(compteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));
    }

    @Override
    public CompteDto findByNumero(String numero) {
        return compteRepository.findByNumero(numero)
                .map(compteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec le numéro: " + numero));
    }

    @Override
    public List<CompteDto> findByTitulaire(String titulaire) {
        return compteRepository.findByTitulaireContainingIgnoreCase(titulaire).stream()
                .map(compteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CompteDto> findAll(Pageable pageable) {
        return compteRepository.findAll(pageable)
                .map(compteMapper::toDto);
    }

    @Override
    @Transactional
    public CompteDto update(Long id, CompteDto compteDto) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        compteMapper.updateEntityFromDto(compteDto, compte);
        Compte updatedCompte = compteRepository.save(compte);
        return compteMapper.toDto(updatedCompte);
    }

    @Override
    @Transactional
    public CompteDto partialUpdate(Long id, Map<String, Object> updates) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "titulaire" -> compte.setTitulaire((String) value);
                case "type" -> compte.setType(TypeCompte.valueOf((String) value));
                case "actif" -> compte.setActif((Boolean) value);
                case "banqueId" -> compte.setBanqueId((String) value);
                default -> throw new IllegalStateException("Unexpected value: " + key);
            }
        });

        return compteMapper.toDto(compteRepository.save(compte));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!compteRepository.existsById(id)) {
            throw new EntityNotFoundException("Compte non trouvé avec l'id: " + id);
        }
        compteRepository.deleteById(id);
    }

    @Override
    public Page<CompteDto> findByBanque(String banqueId, Pageable pageable) {
        return compteRepository.findByBanqueId(banqueId, pageable)
                .map(compteMapper::toDto);
    }

    @Override
    @Transactional
    public CompteDto crediter(Long id, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        Compte compte = compteRepository.findByIdWithLock(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        if (!compte.getActif()) {
            throw new CompteInactifException("Impossible de créditer un compte inactif");
        }

        compte.setSolde(compte.getSolde().add(montant));
        return compteMapper.toDto(compteRepository.save(compte));
    }

    @Override
    @Transactional
    public CompteDto debiter(Long id, BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        Compte compte = compteRepository.findByIdWithLock(id).orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        if (!compte.getActif()) {
            throw new CompteInactifException("Impossible de débiter un compte inactif");
        }

        if (compte.getSolde().compareTo(montant) < 0) {
            throw new FondsInsuffisantsException("Solde insuffisant pour effectuer cette opération");
        }

        compte.setSolde(compte.getSolde().subtract(montant));
        return compteMapper.toDto(compteRepository.save(compte));
    }

    @Override
    @Transactional
    public void transferer(Long sourceId, Long destinationId, BigDecimal montant) {
        if (sourceId.equals(destinationId)) {
            throw new IllegalArgumentException("Les comptes source et destination doivent être différents");
        }

        Compte source = compteRepository.findByIdWithLock(sourceId)
                .orElseThrow(() -> new EntityNotFoundException("Compte source non trouvé"));

        Compte destination = compteRepository.findByIdWithLock(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Compte destination non trouvé"));

        if (!source.getActif() || !destination.getActif()) {
            throw new CompteInactifException("Les deux comptes doivent être actifs pour un transfert");
        }

        if (source.getSolde().compareTo(montant) < 0) {
            throw new FondsInsuffisantsException("Solde insuffisant pour effectuer ce transfert");
        }

        source.setSolde(source.getSolde().subtract(montant));
        destination.setSolde(destination.getSolde().add(montant));

        compteRepository.saveAll(List.of(source, destination));
    }

    @Override
    @Transactional
    public CompteDto activerDesactiver(Long id, boolean activer) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        compte.setActif(activer);
        return compteMapper.toDto(compteRepository.save(compte));
    }

    @Override
    public List<CompteDto> getComptesByType(TypeCompte type, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Compte> comptesPage = compteRepository.findByType(type, pageable);
        return comptesPage.map(compteMapper::toDto).getContent();
    }


    public Page<Compte> getComptesByActif(Boolean actif, int page, int size, String sort, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return compteRepository.findByActif(actif, pageable);
    }

    @Override
    public Page<CompteDto> findByType(TypeCompte type, Pageable pageable) {
        Page<Compte> comptes = compteRepository.findByType(type, pageable);
        return comptes.map(compteMapper::toDto);
    }

}