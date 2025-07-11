package br.ufscar.dc.dsw.dtos;

import br.ufscar.dc.dsw.models.enums.StatusSessao;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public record SessaoDetalhesDTO(
        UUID id,
        String descricao,
        StatusSessao status,
        Duration duracao,
        ProjetoResumoDTO projeto,
        EstrategiaResumoDTO estrategia,
        UsuarioResumoDTO tester,
        List<HistoricoStatusDTO> historico
) {
}