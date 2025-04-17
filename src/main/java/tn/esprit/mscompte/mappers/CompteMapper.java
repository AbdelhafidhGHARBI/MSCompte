package tn.esprit.mscompte.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.entities.Compte;


@Mapper(componentModel = "spring")
public interface CompteMapper {

    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateMiseAJour", ignore = true)
    Compte toEntity(CompteDto dto);

    CompteDto toDto(Compte entity);

    void updateEntityFromDto(CompteDto compteDto, @MappingTarget Compte compte);
}
