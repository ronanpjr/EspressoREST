package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BugEdicaoDTO(
    @NotNull(message = "{bug.id.notNull}")
    UUID id,

    @NotNull(message = "{bug.idSessao.notNull}")
    UUID idSessao,

    @NotBlank(message = "{bug.descricao.notBlank}")
    @Size(min = 10, max = 500, message = "{bug.descricao.size}")
    String descricao,

    boolean resolvido
) {}