package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.DicaModel;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DicaRepository extends JpaRepository<DicaModel, UUID> {

    List<DicaModel> findByEstrategia(EstrategiaModel estrategia);

    
    List<DicaModel> findByEstrategiaId(UUID idEstrategia);
}
