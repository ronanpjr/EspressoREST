package br.ufscar.dc.dsw.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjetoDTO(
        UUID id,
        String nome,
        String descricao,
        LocalDate dataCriacao,
        List<String> nomesMembros,
        boolean usuarioLogadoEhMembro
) {}
