package br.ufscar.dc.dsw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record ProjetoEdicaoDTO(
        UUID id,
        @NotBlank @Size(max = 255) String nome,
        @Size(max = 1000) String descricao,
        List<UUID> membrosIds
) {}
