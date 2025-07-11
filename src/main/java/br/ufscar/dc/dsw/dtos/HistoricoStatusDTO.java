package br.ufscar.dc.dsw.dtos;

import br.ufscar.dc.dsw.models.enums.StatusSessao;

public record HistoricoStatusDTO(
        StatusSessao statusAnterior,
        StatusSessao statusNovo,
        java.time.LocalDateTime dataHora
) {
}
