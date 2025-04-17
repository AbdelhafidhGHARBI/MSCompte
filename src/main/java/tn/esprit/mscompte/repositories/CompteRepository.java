package tn.esprit.mscompte.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mscompte.entities.Compte;
import tn.esprit.mscompte.entities.TypeCompte;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Optional<Compte> findByNumero(String numero);

    List<Compte> findByTitulaireContainingIgnoreCase(String titulaire);

    Page<Compte> findByBanqueId(String banqueId, Pageable pageable);

    Page<Compte> findByTitulaireContainingIgnoreCase(String titulaire, Pageable pageable);

    Page<Compte> findByType(TypeCompte type, Pageable pageable);

    Page<Compte> findByBanqueIdAndTitulaireContainingIgnoreCase(String banqueId, String titulaire, Pageable pageable);

    Page<Compte> findBySoldeGreaterThanEqual(BigDecimal montant, Pageable pageable);

    Page<Compte> findBySoldeLessThanEqual(BigDecimal montant, Pageable pageable);

    Page<Compte> findByActif(Boolean actif, Pageable pageable);

    Optional<Compte> findByIdWithLock(Long id);

}
