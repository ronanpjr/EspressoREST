package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.SessaoDetalhesDTO;

import br.ufscar.dc.dsw.dtos.BugCadastroDTO;
import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.BugEdicaoDTO;
import br.ufscar.dc.dsw.services.BugService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import br.ufscar.dc.dsw.models.UsuarioModel;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/bugs")
public class BugController {

    @Autowired
    private BugService bugService;

    @GetMapping("/lista-sessao")
    public String listarBugsPorSessao(
            @RequestParam("idSessao") UUID idSessao,
            ModelMap model,
            @AuthenticationPrincipal UsuarioModel usuarioLogado
    ) {
        List<BugDTO> listaBugs = bugService.buscarTodosBugsPorSessao(idSessao, usuarioLogado);

        SessaoDetalhesDTO sessaoDetalhes = bugService.buscarDetalhesDaSessao(idSessao);
        
        model.addAttribute("listaBugs", listaBugs);
        model.addAttribute("sessao", sessaoDetalhes);

        return "bug/listaPorSessao"; 
    }

    @GetMapping("/cadastro")
    public String preRenderCadastroBug(@RequestParam("idSessao") UUID idSessao, ModelMap model) {

        SessaoDetalhesDTO sessaoDetalhes = bugService.buscarDetalhesDaSessao(idSessao);
        
        model.addAttribute("bugCadastroDTO", new BugCadastroDTO(idSessao, ""));
        model.addAttribute("sessao", sessaoDetalhes); // Passa o objeto correto
        model.addAttribute("isEditModeBug", false);
        return "bug/formulario";
    }

    @PostMapping("/salvar")
    public String salvarBug(@Valid @ModelAttribute("bugCadastroDTO") BugCadastroDTO bugCadastroDTO,
                            BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.bugCadastroDTO", result);
            attr.addFlashAttribute("bugCadastroDTO", bugCadastroDTO);
            return "redirect:/bugs/cadastro?idSessao=" + bugCadastroDTO.idSessao();
        }
        bugService.salvarNovoBug(bugCadastroDTO);
        attr.addFlashAttribute("success", "Bug registrado com sucesso!");
        return "redirect:/bugs/lista-sessao?idSessao=" + bugCadastroDTO.idSessao();
    }

    @GetMapping("/editar/{id}")
    public String preRenderEdicaoBug(@PathVariable("id") UUID idBug, ModelMap model) {
        BugDTO bugDTO = bugService.buscarBugPorId(idBug);
        SessaoDetalhesDTO sessaoDetalhes = bugService.buscarDetalhesDaSessao(bugDTO.idSessao());      
        BugEdicaoDTO bugEdicaoDTO = new BugEdicaoDTO(bugDTO.id(), bugDTO.idSessao(), bugDTO.descricao(), bugDTO.resolvido());
        model.addAttribute("bugEdicaoDTO", bugEdicaoDTO);
        model.addAttribute("sessao", sessaoDetalhes);
        model.addAttribute("isEditModeBug", true);
        return "bug/formulario";
    }

    @PostMapping("/atualizar")
    public String atualizarBug(@Valid @ModelAttribute("bugEdicaoDTO") BugEdicaoDTO bugEdicaoDTO,
                               BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.bugEdicaoDTO", result);
            attr.addFlashAttribute("bugEdicaoDTO", bugEdicaoDTO);
            return "redirect:/bugs/editar/" + bugEdicaoDTO.id();
        }
        bugService.atualizarBug(bugEdicaoDTO);
        attr.addFlashAttribute("success", "Bug atualizado com sucesso!");
        return "redirect:/bugs/lista-sessao?idSessao=" + bugEdicaoDTO.idSessao();
    }

    @GetMapping("/remover/{id}")
    public String removerBug(@PathVariable("id") UUID idBug, RedirectAttributes attr) {
        BugDTO bug = bugService.buscarBugPorId(idBug);
        UUID idSessaoRetorno = bug.idSessao();
        bugService.excluirBug(idBug);
        attr.addFlashAttribute("success", "Bug removido com sucesso!");
        return "redirect:/bugs/lista-sessao?idSessao=" + idSessaoRetorno;
    }
}