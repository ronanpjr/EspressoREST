package br.ufscar.dc.dsw.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record BugDTO(
    UUID id,
    UUID idSessao, 
    String descricao,
    LocalDateTime dataRegistro,
    boolean resolvido
) {}