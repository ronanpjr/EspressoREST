package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.SessaoDTO;
import br.ufscar.dc.dsw.dtos.SessaoDetalhesDTO;
import br.ufscar.dc.dsw.dtos.SessaoEdicaoDTO;
import br.ufscar.dc.dsw.mappers.SessaoMapper;
import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import br.ufscar.dc.dsw.services.SessaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessoes")
public class SessaoController {

    private final SessaoService sessaoService;
    private final SessaoMapper sessaoMapper;

    public SessaoController(SessaoService sessaoService, SessaoMapper sessaoMapper) {
        this.sessaoService = sessaoService;
        this.sessaoMapper = sessaoMapper;
    }

    @PostMapping
    public ResponseEntity<SessaoDetalhesDTO> criarSessao(
            @Valid @RequestBody SessaoDTO sessaoDto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        SessaoModel novaSessao = sessaoService.criarSessao(sessaoDto, usuarioLogado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaSessao.getId()).toUri();
        return ResponseEntity.created(location).body(sessaoMapper.toDetalhesDTO(novaSessao));
    }

    @GetMapping("/me")
    public ResponseEntity<List<SessaoDetalhesDTO>> listarMinhasSessoes(@AuthenticationPrincipal UsuarioModel usuarioLogado) {
        List<SessaoModel> minhasSessoes = sessaoService.listarPorTester(usuarioLogado.getEmail());
        return ResponseEntity.ok(minhasSessoes.stream().map(sessaoMapper::toDetalhesDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoDetalhesDTO> detalhesSessao(
            @PathVariable UUID id,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        SessaoModel sessao = sessaoService.buscarPorIdEVerificarDono(id, usuarioLogado);
        return ResponseEntity.ok(sessaoMapper.toDetalhesDTO(sessao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SessaoDetalhesDTO> editarSessao(
            @PathVariable UUID id,
            @Valid @RequestBody SessaoEdicaoDTO sessaoEdicaoDTO,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        SessaoModel sessaoAtualizada = sessaoService.atualizarSessao(id, sessaoEdicaoDTO, usuarioLogado);
        return ResponseEntity.ok(sessaoMapper.toDetalhesDTO(sessaoAtualizada));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SessaoDetalhesDTO> atualizarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody StatusUpdateDTO statusUpdateDTO,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        SessaoModel sessaoAtualizada = sessaoService.atualizarStatus(id, statusUpdateDTO.novoStatus(), usuarioLogado);
        return ResponseEntity.ok(sessaoMapper.toDetalhesDTO(sessaoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSessao(
            @PathVariable UUID id,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        sessaoService.deletarSessao(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<SessaoDetalhesDTO>> listarPorProjeto(
            @PathVariable UUID projetoId,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        List<SessaoModel> sessoes = sessaoService.listarPorProjetoEVerificarPermissao(projetoId, usuarioLogado);
        return ResponseEntity.ok(sessoes.stream().map(sessaoMapper::toDetalhesDTO).collect(Collectors.toList()));
    }

    public record StatusUpdateDTO(@NotNull StatusSessao novoStatus) {
    }
}