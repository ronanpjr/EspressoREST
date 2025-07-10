package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.ProjetoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import org.springframework.data.domain.Sort; // Import Sort class
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<ProjetoModel, UUID> {

    Optional<ProjetoModel> findByNome(String nome);

    boolean existsByNome(String nome);

    // Original method (still valid but less flexible for dynamic sorting)
    @Query("SELECT p FROM ProjetoModel p JOIN p.membros m WHERE m = :usuario")
    List<ProjetoModel> findAllByMembro(@Param("usuario") UsuarioModel usuario);


    @Query("SELECT p FROM ProjetoModel p JOIN p.membros m WHERE m = :usuario")
    List<ProjetoModel> findAllByMembrosContains(@Param("usuario") UsuarioModel usuario, Sort sort);
}