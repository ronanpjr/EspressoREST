package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.BugCadastroDTO;
import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.BugEdicaoDTO;
import br.ufscar.dc.dsw.services.BugService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.ufscar.dc.dsw.models.UsuarioModel;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @GetMapping("/sessoes/{sessaoId}/bugs")
    public ResponseEntity<List<BugDTO>> listarBugsPorSessao(
            @PathVariable("sessaoId") UUID sessaoId,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        List<BugDTO> listaBugs = bugService.buscarTodosBugsPorSessao(sessaoId, usuarioLogado);
        return ResponseEntity.ok(listaBugs);
    }

    record BugApiCadastroRequest(String descricao) {}

    @PostMapping("/sessoes/{sessaoId}/bugs")
    public ResponseEntity<BugDTO> registrarBug(
            @PathVariable("sessaoId") UUID sessaoId,
            @Valid @RequestBody BugApiCadastroRequest request) {

        BugCadastroDTO bugCadastroDTO = new BugCadastroDTO(sessaoId, request.descricao());

        BugDTO novoBug = bugService.salvarNovoBug(bugCadastroDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoBug.id())
                .toUri();

        return ResponseEntity.created(location).body(novoBug);
    }

    record BugApiEdicaoRequest(String descricao, boolean resolvido) {}

    @PutMapping("/bugs/{bugId}")
    public ResponseEntity<BugDTO> atualizarBug(
            @PathVariable("bugId") UUID bugId,
            @Valid @RequestBody BugApiEdicaoRequest request) {
        BugDTO bugAtual = bugService.buscarBugPorId(bugId);

        BugEdicaoDTO bugEdicaoDTO = new BugEdicaoDTO(
                bugId,
                bugAtual.idSessao(),
                request.descricao(),
                request.resolvido()
        );

        BugDTO bugAtualizado = bugService.atualizarBug(bugEdicaoDTO);

        return ResponseEntity.ok(bugAtualizado);
    }

    @DeleteMapping("/bugs/{bugId}")
    public ResponseEntity<Void> removerBug(@PathVariable("bugId") UUID bugId) {
        bugService.excluirBug(bugId);
        return ResponseEntity.noContent().build();
    }
}