package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.DicaDto;
import br.ufscar.dc.dsw.dtos.EstrategiaDto;
import br.ufscar.dc.dsw.dtos.ExemploDto;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.services.EstrategiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/estrategias")
public class EstrategiaController {

    @Autowired
    private EstrategiaService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaEstrategias", service.findAll());
        return "estrategia/listar";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        EstrategiaDto estrategiaDto = new EstrategiaDto();
        estrategiaDto.setDicas(new ArrayList<>());
        estrategiaDto.setExemplos(new ArrayList<>());
        model.addAttribute("estrategia", estrategiaDto);
        return "estrategia/formulario";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable UUID id, Model model) {
        EstrategiaModel estrategiaModel = service.findById(id);
        EstrategiaDto estrategiaDto = service.convertModelToDto(estrategiaModel);
        model.addAttribute("estrategia", estrategiaDto);
        return "estrategia/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam("nome") String nome,
                         @RequestParam("descricao") String descricao,
                         @RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "exemplosTexto", required = false) String[] exemplosTexto,
                         @RequestParam(value = "exemplosId", required = false) String[] exemplosId,
                         @RequestParam(value = "exemplosUrlImagem", required = false) String[] exemplosUrlImagem,
                         @RequestParam(value = "dicasTexto", required = false) String[] dicasTexto,
                         @RequestParam(value = "dicasId", required = false) String[] dicasId,
                         @RequestParam("imagensExemplo") List<MultipartFile> imagensExemplo,
                         RedirectAttributes redirectAttributes) {
        try {
            EstrategiaDto dto = new EstrategiaDto();
            if (id != null && !id.isEmpty()) {
                dto.setId(UUID.fromString(id));
            }
            dto.setNome(nome);
            dto.setDescricao(descricao);

            List<ExemploDto> exemplos = new ArrayList<>();
            if (exemplosTexto != null) {
                for (int i = 0; i < exemplosTexto.length; i++) {
                    if (exemplosTexto[i] != null && !exemplosTexto[i].trim().isEmpty()) {
                        ExemploDto exemplo = new ExemploDto();
                        if (exemplosId != null && i < exemplosId.length && exemplosId[i] != null && !exemplosId[i].isEmpty()) {
                            exemplo.setId(UUID.fromString(exemplosId[i]));
                        }
                        exemplo.setTexto(exemplosTexto[i]);
                        if (exemplosUrlImagem != null && i < exemplosUrlImagem.length && exemplosUrlImagem[i] != null) {
                            exemplo.setUrlImagem(exemplosUrlImagem[i]);
                        }
                        exemplos.add(exemplo);
                    }
                }
            }
            dto.setExemplos(exemplos);

            List<DicaDto> dicas = new ArrayList<>();
            if (dicasTexto != null) {
                for (int i = 0; i < dicasTexto.length; i++) {
                    if (dicasTexto[i] != null && !dicasTexto[i].trim().isEmpty()) {
                        DicaDto dica = new DicaDto();
                        if (dicasId != null && i < dicasId.length && dicasId[i] != null && !dicasId[i].isEmpty()) {
                            dica.setId(UUID.fromString(dicasId[i]));
                        }
                        dica.setDica(dicasTexto[i]);
                        dicas.add(dica);
                    }
                }
            }
            dto.setDicas(dicas);

            service.save(dto, imagensExemplo);
            redirectAttributes.addFlashAttribute("success", "Estratégia salva com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao salvar estratégia: " + e.getMessage());
            return "estrategia/formulario";
        }
        return "redirect:/estrategias";
    }

    @GetMapping("/remover/{id}")
    public String excluir(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("success", "Estratégia excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao excluir estratégia.");
        }
        return "redirect:/estrategias";
    }

    @GetMapping("/detalhes/{id}")
    public String exibirDetalhes(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EstrategiaModel estrategia = service.findById(id);
            if (estrategia == null) {
                redirectAttributes.addFlashAttribute("fail", "Estratégia não encontrada.");
                return "redirect:/estrategias";
            }
            model.addAttribute("estrategia", estrategia);
            return "estrategia/detalhes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao carregar detalhes da estratégia.");
            return "redirect:/estrategias";
        }
    }
}