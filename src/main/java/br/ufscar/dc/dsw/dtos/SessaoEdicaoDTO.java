package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SessaoEdicaoDTO(
        @NotNull
        UUID estrategiaId,

        @NotNull
        Long duracao,

        @NotBlank
        String descricao
) {
}