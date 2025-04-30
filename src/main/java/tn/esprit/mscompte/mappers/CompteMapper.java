package tn.esprit.mscompte.mappers;

import org.mapstruct.*;
import tn.esprit.mscompte.dto.CompteDto;
import tn.esprit.mscompte.entities.Compte;


@Mapper(componentModel = "spring")
public interface CompteMapper {
    Compte toEntity(CompteDto dto);
    CompteDto toDto(Compte entity);
    @Mapping(target = "id", ignore = true)  // Correct way to ignore ID during updates
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CompteDto dto, @MappingTarget Compte entity);
}
