package br.ufscar.dc.dsw.dtos;

import java.util.UUID;

public record UsuarioResumoDTO(
        UUID id,
        String nome,
        String email
) {
}
