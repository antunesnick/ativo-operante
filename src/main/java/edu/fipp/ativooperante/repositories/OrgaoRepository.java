package edu.fipp.ativooperante.repositories;

import edu.fipp.ativooperante.entities.Orgao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {
}
