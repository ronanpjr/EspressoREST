package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.*;
import br.ufscar.dc.dsw.models.enums.Papel;
import br.ufscar.dc.dsw.services.ProjetoService;
import br.ufscar.dc.dsw.services.UsuarioService;
import br.ufscar.dc.dsw.models.UsuarioModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public String listar(
            ModelMap model,
            Authentication authentication,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) String sortOrder
    ) {
        // If sortBy or sortOrder are null, the service will apply its defaults
        List<ProjetoDTO> projetos = projetoService.listarParaUsuarioLogado(sortBy, sortOrder);
        model.addAttribute("listaProjetos", projetos);
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        model.addAttribute("ehAdmin", usuarioLogado.getPapel() == Papel.ADMIN);
        return "projeto/lista";
    }

    @GetMapping("/cadastro")
    public String preRenderCadastro(ModelMap model, Authentication authentication) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        if (usuarioLogado.getPapel() != Papel.ADMIN) {
            return "redirect:/projetos/listar";
        }
        model.addAttribute("projetoCadastroDTO", new ProjetoCadastroDTO("", "", null));
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "projeto/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid ProjetoCadastroDTO dto, BindingResult result, ModelMap model,
                         RedirectAttributes attr, Authentication authentication) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        if (usuarioLogado.getPapel() != Papel.ADMIN) {
            return "redirect:/projetos/listar";
        }
        if (result.hasErrors()) {
            model.addAttribute("projetoCadastroDTO", dto);
            model.addAttribute("usuarios", usuarioService.buscarTodos());
            return "projeto/formulario";
        }
        try {
            projetoService.salvar(dto);
            attr.addFlashAttribute("success", "Projeto salvo com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("fail", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    @GetMapping("/editar/{id}")
    public String preRenderEdicao(@PathVariable UUID id, ModelMap model, Authentication authentication) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        if (usuarioLogado.getPapel() != Papel.ADMIN) {
            return "redirect:/projetos/listar";
        }
        ProjetoDTO projeto = projetoService.buscarPorId(id);
        ProjetoEdicaoDTO dto = new ProjetoEdicaoDTO(
                projeto.id(),
                projeto.nome(),
                projeto.descricao(),
                usuarioService.buscarTodos().stream()
                        .filter(u -> projeto.nomesMembros().contains(u.nome()))
                        .map(UsuarioDTO::id)
                        .toList()
        );
        model.addAttribute("projetoEdicaoDTO", dto);
        model.addAttribute("usuarios", usuarioService.buscarTodos());
        return "projeto/formulario";
    }

    @PostMapping("/editar")
    public String editar(@Valid ProjetoEdicaoDTO dto, BindingResult result, ModelMap model,
                         RedirectAttributes attr, Authentication authentication) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        if (usuarioLogado.getPapel() != Papel.ADMIN) {
            return "redirect:/projetos/listar";
        }
        if (result.hasErrors()) {
            model.addAttribute("projetoEdicaoDTO", dto);
            model.addAttribute("usuarios", usuarioService.buscarTodos());
            return "projeto/formulario";
        }
        try {
            projetoService.atualizar(dto);
            attr.addFlashAttribute("success", "Projeto atualizado com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("fail", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable UUID id, RedirectAttributes attr, Authentication authentication) {
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
        if (usuarioLogado.getPapel() != Papel.ADMIN) {
            return "redirect:/projetos/listar";
        }
        try {
            projetoService.excluir(id);
            attr.addFlashAttribute("success", "Projeto removido com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("fail", "Erro ao remover: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }
}