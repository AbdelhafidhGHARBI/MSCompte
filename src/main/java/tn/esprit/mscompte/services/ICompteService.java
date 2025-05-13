package tn.esprit.mscompte.services;

import tn.esprit.mscompte.dto.CompteDto;
import java.util.List;

public interface ICompteService {
    CompteDto create(CompteDto compteDto);
    CompteDto findById(Long id);
    List<CompteDto> findAll();
    CompteDto update(Long id, CompteDto compteDto);
    void delete(Long id);


}