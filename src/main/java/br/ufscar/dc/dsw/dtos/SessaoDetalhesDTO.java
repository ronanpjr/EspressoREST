package br.ufscar.dc.dsw.dtos;

import java.util.UUID;

public record SessaoDetalhesDTO(
        UUID id,
        String descricao,
        ProjetoResumoDTO projeto 
) {
}