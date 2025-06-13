package tn.esprit.mscompte.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.entities.Compte;
import tn.esprit.mscompte.events.KafkaProducerService;
import tn.esprit.mscompte.mappers.CompteMapper;
import tn.esprit.mscompte.repositories.CompteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class CompteServiceImpl implements ICompteService {

    private final CompteRepository compteRepository;
    private final CompteMapper compteMapper;
    private final KafkaProducerService kafkaProducer; // Injectez le producteur Kafka

    @Override
    public CompteDto create(CompteDto compteDto) {
        // Convertir le DTO en entité
        Compte compte = compteMapper.toEntity(compteDto);
        compte.setDateCreation(LocalDateTime.now());
        compte.setActif(true); // Optionnel selon la logique métier

        // Sauvegarder l'entité dans la base de données
        Compte savedCompte = compteRepository.save(compte);

        // Mapper l'entité sauvegardée en DTO
        CompteDto createdCompte = compteMapper.toDto(savedCompte);

        // Publier un événement Kafka pour la création du compte
        kafkaProducer.sendCompteEvent("COMPTE_CREATED", createdCompte);
        return createdCompte;
    }

    @Override
    public CompteDto findById(Long id) {
        // Rechercher le compte par ID
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        // Mapper l'entité en DTO
        return compteMapper.toDto(compte);
    }

    @Override
    public List<CompteDto> findAll() {
        // Mapper les entités en DTOs
        return compteRepository.findAll().stream()
                .map(compteMapper::toDto)
                .toList();
    }

    @Override
    public CompteDto update(Long id, CompteDto compteDto) {
        // Rechercher le compte existant par ID
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé avec l'id: " + id));

        // Mettre à jour l'entité avec les nouvelles données
        compteMapper.updateEntityFromDto(compteDto, compte);
        compte.setDateMiseAJour(LocalDateTime.now());

        // Sauvegarder l'entité mise à jour
        Compte updatedCompte = compteRepository.save(compte);

        // Mapper l'entité mise à jour en DTO
        CompteDto updatedCompteDto = compteMapper.toDto(updatedCompte);

        // Publier un événement Kafka pour la mise à jour du compte
        kafkaProducer.sendCompteEvent("COMPTE_UPDATED", updatedCompteDto);
        return updatedCompteDto;
    }

    @Override
    public void delete(Long id) {
        // Vérifier si le compte existe
        if (!compteRepository.existsById(id)) {
            throw new EntityNotFoundException("Compte non trouvé avec l'id: " + id);
        }

        // Récupérer le compte avant suppression
        Compte compte = compteRepository.findById(id).get();

        // Mapper l'entité en DTO
        CompteDto deletedCompteDto = compteMapper.toDto(compte);

        // Supprimer le compte
        compteRepository.deleteById(id);

        // Publier un événement Kafka pour la suppression du compte
        kafkaProducer.sendCompteEvent("COMPTE_DELETED", deletedCompteDto);
    }
}