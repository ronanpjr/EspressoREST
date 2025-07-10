package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record BugCadastroDTO(
    @NotNull(message = "{bug.idSessao.notNull}") 
    UUID idSessao,

    @NotBlank(message = "{bug.descricao.notBlank}")
    @Size(min = 10, max = 500, message = "{bug.descricao.size}")
    String descricao
) {}