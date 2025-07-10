package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.UsuarioCadastroDTO;
import br.ufscar.dc.dsw.dtos.UsuarioDTO;
import br.ufscar.dc.dsw.dtos.UsuarioEdicaoDTO;
import br.ufscar.dc.dsw.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable("id") UUID id) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(usuarioDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Inserir novo usuário
    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
        try {
            UsuarioDTO novoUsuario = usuarioService.salvarNovoUsuario(usuarioCadastroDTO);
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable("id") UUID id, @Valid @RequestBody UsuarioEdicaoDTO usuarioEdicaoDTO) {
        if (!id.equals(usuarioEdicaoDTO.id())) {
            return ResponseEntity.badRequest().body("O ID na URL deve ser o mesmo do corpo da requisição.");
        }
        try {
            UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(usuarioEdicaoDTO);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // Remover usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable("id") UUID id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}