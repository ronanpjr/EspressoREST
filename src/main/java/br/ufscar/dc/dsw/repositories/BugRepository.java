package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.models.BugModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BugRepository extends JpaRepository<BugModel, UUID> {

    List<BugModel> findBySessaoIdOrderByDataRegistroDesc(UUID sessaoId);

    List<BugModel> findByResolvido(boolean resolvido);

    List<BugModel> findByResolvidoFalseOrderByDataRegistroDesc();

    List<BugModel> findBySessaoIdAndResolvido(UUID sessaoId, boolean resolvido);

    long countBySessaoId(UUID sessaoId);
    long countBySessaoIdAndResolvidoFalse(UUID sessaoId);
}