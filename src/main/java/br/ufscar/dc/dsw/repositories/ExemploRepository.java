package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.ExemploModel;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExemploRepository extends JpaRepository<ExemploModel, UUID> {

    
    List<ExemploModel> findByEstrategia(EstrategiaModel estrategia);

    List<ExemploModel> findByEstrategiaId(UUID idEstrategia);
}
