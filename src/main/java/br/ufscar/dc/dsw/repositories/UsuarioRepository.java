package br.ufscar.dc.dsw.repositories;

import java.util.Optional;
import java.util.UUID;

import br.ufscar.dc.dsw.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    // Optional usado para caso retorne null
    Optional<UsuarioModel> findByEmail(String email);

}