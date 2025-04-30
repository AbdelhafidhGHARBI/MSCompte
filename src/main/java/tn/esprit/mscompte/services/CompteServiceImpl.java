package tn.esprit.mscompte.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.entities.Compte;
import tn.esprit.mscompte.entities.TypeCompte;
import tn.esprit.mscompte.mappers.CompteMapper;
import tn.esprit.mscompte.repositories.CompteRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompteServiceImpl implements ICompteService {

    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;

    @Override
    public CompteDto create(CompteDto compteDto) {
        Compte compte = compteMapper.toEntity(compteDto);
        compte.setDateCreation(LocalDateTime.now());
        compte.setActif(true); // optionnel selon logique
        Compte savedCompte = compteRepository.save(compte);
        return compteMapper.toDto(savedCompte);
    }

    @Override
    public CompteDto findById(Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));
        return compteMapper.toDto(compte);
    }

    @Override
    public List<CompteDto> findAll() {
        List<Compte> comptes = compteRepository.findAll();
        return comptes.stream()
                .map(compteMapper::toDto)
                .toList();
    }

    @Override
    public CompteDto update(Long id, CompteDto compteDto) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        compteMapper.updateEntityFromDto(compteDto, compte);
        compte.setDateMiseAJour(LocalDateTime.now());

        Compte updatedCompte = compteRepository.save(compte);
        return compteMapper.toDto(updatedCompte);
    }

    @Override
    public void delete(Long id) {
        if (!compteRepository.existsById(id)) {
            throw new EntityNotFoundException("Compte non trouvé avec l'id: " + id);
        }
        compteRepository.deleteById(id);
    }
}