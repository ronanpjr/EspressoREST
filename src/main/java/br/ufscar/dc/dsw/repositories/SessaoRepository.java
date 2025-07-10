package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessaoRepository extends JpaRepository<SessaoModel, UUID> {
    @Query("SELECT s FROM SessaoModel s JOIN FETCH s.tester JOIN FETCH s.projeto JOIN FETCH s.estrategia WHERE s.tester.email = :emailTester ORDER BY s.id DESC")
    List<SessaoModel> findAllByTesterEmailWithDetails(@Param("emailTester") String email);
    @Query("SELECT s FROM SessaoModel s JOIN FETCH s.tester JOIN FETCH s.projeto JOIN FETCH s.estrategia WHERE s.projeto.id = :projetoId ORDER BY s.id DESC")
    List<SessaoModel> findAllByProjetoIdWithDetails(@Param("projetoId") UUID projetoId);
}
