package br.ufscar.dc.dsw.dtos;

import br.ufscar.dc.dsw.models.enums.Papel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UsuarioCadastroDTO(
        UUID id,
        @NotBlank(message = "{usuario.nome.notBlank}")
        @Size(max = 256, message = "{usuario.nome.size}")
        String nome,

        @NotBlank(message = "{usuario.email.notBlank}")
        @Email(message = "{usuario.email.notValid}")
        @Size(max = 255, message = "{usuario.email.size}")
        String email,

        @NotBlank(message = "{usuario.senha.notBlank}")
        @Size(min = 6, max = 60, message = "{usuario.senha.size}") // Password will be encoded, so max 60 for BCrypt
        String senha,

        Papel papel
) {}