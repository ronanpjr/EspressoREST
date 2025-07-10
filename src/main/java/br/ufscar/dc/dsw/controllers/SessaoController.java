package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.ProjetoDTO;
import br.ufscar.dc.dsw.dtos.SessaoDTO;
import br.ufscar.dc.dsw.dtos.SessaoEdicaoDTO;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import br.ufscar.dc.dsw.services.EstrategiaService;
import br.ufscar.dc.dsw.services.ProjetoService;
import br.ufscar.dc.dsw.services.SessaoService;
import br.ufscar.dc.dsw.services.BugService; 
import br.ufscar.dc.dsw.dtos.BugDTO; 
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sessoes")
public class SessaoController {
    private final SessaoService sessaoService;
    private final ProjetoService projetoService;
    private final EstrategiaService estrategiaService;
    private final BugService bugService; 

    public SessaoController(
            SessaoService sessaoService,
            ProjetoService projetoService,
            EstrategiaService estrategiaService,
            BugService bugService 
    ) {
        this.sessaoService = sessaoService;
        this.projetoService = projetoService;
        this.estrategiaService = estrategiaService;
        this.bugService = bugService; 
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(@RequestParam("projetoId") UUID projetoId, Model model) {
        ProjetoDTO projeto = projetoService.buscarPorId(projetoId);
        List<EstrategiaModel> estrategias = estrategiaService.findAll();
        model.addAttribute("sessaoDTO", new SessaoDTO(projetoId, null, null, ""));
        model.addAttribute("projeto", projeto);
        model.addAttribute("estrategias", estrategias);
        return "sessao/formulario";
    }

    @PostMapping("/criar")
    public String criarSessao(
            @ModelAttribute @Valid SessaoDTO sessaoDto,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        SessaoModel novaSessao = sessaoService.criarSessao(sessaoDto, usuarioLogado.getEmail());
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Sessão criada com sucesso!");
        return "redirect:/sessoes/" + novaSessao.getId();
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable UUID id, Model model, RedirectAttributes redirectAttrs, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        SessaoModel sessao = sessaoService.buscarParaEdicao(id, usuarioLogado);

        SessaoEdicaoDTO dto = new SessaoEdicaoDTO(
                sessao.getId(),
                sessao.getProjeto().getId(),
                sessao.getEstrategia().getId(),
                sessao.getDuracao().toMinutes(),
                sessao.getDescricao()
        );

        model.addAttribute("sessaoEdicaoDTO", dto);
        model.addAttribute("estrategias", estrategiaService.findAll());
        return "sessao/formulario";

    }

    @PostMapping("/editar")
    public String editarSessao(
            @ModelAttribute @Valid SessaoEdicaoDTO sessaoEdicaoDTO,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {

        sessaoService.atualizarSessao(sessaoEdicaoDTO, usuarioLogado);
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Sessão atualizada com sucesso!");
        return "redirect:/sessoes/" + sessaoEdicaoDTO.id();
    }

    @GetMapping("/{id}")
    public String detalhesSessao(@PathVariable UUID id, Model model, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        SessaoModel sessao = sessaoService.buscarPorId(id);
        
        List<BugDTO> listaBugs = bugService.buscarTodosBugsPorSessao(id, usuarioLogado); 

        model.addAttribute("sessao", sessao);
        model.addAttribute("todosStatus", StatusSessao.values());
        model.addAttribute("listaBugs", listaBugs); 
        return "sessao/detalhes";
    }

    @GetMapping("/projeto/{projetoId}")
    public String listarPorProjeto(@PathVariable UUID projetoId, Model model) {
        List<SessaoModel> sessoes = sessaoService.listarPorProjeto(projetoId);
        ProjetoDTO projeto = projetoService.buscarPorId(projetoId);
        model.addAttribute("sessoes", sessoes);
        model.addAttribute("projeto", projeto);
        return "sessao/lista";
    }

    @PostMapping("/atualizarStatus")
    public String atualizarStatus(
            @RequestParam UUID sessaoId,
            @RequestParam StatusSessao novoStatus,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        sessaoService.atualizarStatus(sessaoId, novoStatus, usuarioLogado);
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Status da sessão foi atualizado com sucesso!");
        return "redirect:/sessoes/" + sessaoId;
    }

    @PostMapping("/deletar")
    public String deletarSessao(
            @RequestParam UUID sessaoId,
            @RequestParam UUID projetoId,
            RedirectAttributes redirectAttrs,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        sessaoService.deletarSessao(sessaoId, usuarioLogado);
        redirectAttrs.addFlashAttribute("mensagemSucesso", "Sessão foi removida com sucesso.");
        return "redirect:/sessoes/projeto/" + projetoId;
    }

    @GetMapping("/minhas-sessoes")
    public String listarMinhasSessoes(Model model, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        List<SessaoModel> minhasSessoes = sessaoService.listarPorTester(usuarioLogado.getEmail());
        model.addAttribute("sessoes", minhasSessoes);
        model.addAttribute("tituloPagina", "Minhas Sessões de Teste");
        return "sessao/minhas_sessoes";
    }
}