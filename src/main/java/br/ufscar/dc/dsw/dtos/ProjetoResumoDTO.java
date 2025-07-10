package br.ufscar.dc.dsw.dtos;

import java.util.UUID;

public record ProjetoResumoDTO(
        UUID id,
        String nome
) {
}