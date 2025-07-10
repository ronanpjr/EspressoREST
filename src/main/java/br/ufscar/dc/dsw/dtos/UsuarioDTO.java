
package br.ufscar.dc.dsw.dtos;

import br.ufscar.dc.dsw.models.enums.Papel;
import java.util.UUID;

public record UsuarioDTO(
        UUID id,
        String nome,
        String email,
        Papel papel
) {}