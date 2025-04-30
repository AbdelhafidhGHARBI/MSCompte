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

    List<CompteDto> findAll();

    CompteDto update(Long id, CompteDto compteDto);

    void delete(Long id);


}