package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.*;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.services.ProjetoService;
import br.ufscar.dc.dsw.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final UsuarioService usuarioService;

    public ProjetoController(ProjetoService projetoService, UsuarioService usuarioService) {
        this.projetoService = projetoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ProjetoDTO> criarProjeto(
            @Valid @RequestBody ProjetoCadastroDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        if (!usuarioLogado.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        ProjetoDTO novoProjeto = projetoService.salvar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoProjeto.id())
                .toUri();
        return ResponseEntity.created(location).body(novoProjeto);
    }

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> listarProjetos(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        List<ProjetoDTO> projetos = projetoService.listarParaUsuarioLogado(sortBy, sortOrder);
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoDTO> buscarPorId(
            @PathVariable UUID id,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        ProjetoDTO projeto = projetoService.buscarPorId(id);
        if (!projeto.usuarioLogadoEhMembro() && !usuarioLogado.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(projeto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoDTO> editarProjeto(
            @PathVariable UUID id,
            @Valid @RequestBody ProjetoEdicaoDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        if (!usuarioLogado.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        ProjetoDTO atualizado = projetoService.atualizar(dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProjeto(
            @PathVariable UUID id,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        if (!usuarioLogado.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
