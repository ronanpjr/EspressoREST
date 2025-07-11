package br.ufscar.dc.dsw.mappers;

import br.ufscar.dc.dsw.dtos.*;
import br.ufscar.dc.dsw.models.SessaoModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessaoMapper {
    public SessaoDetalhesDTO toDetalhesDTO(SessaoModel model) {
        if (model == null) {
            return null;
        }

        ProjetoResumoDTO projetoDTO = new ProjetoResumoDTO(model.getProjeto().getId(), model.getProjeto().getNome());
        EstrategiaResumoDTO estrategiaDTO = new EstrategiaResumoDTO(model.getEstrategia().getId(), model.getEstrategia().getNome());
        UsuarioResumoDTO testerDTO = new UsuarioResumoDTO(model.getTester().getId(), model.getTester().getNome(), model.getTester().getEmail());

        List<HistoricoStatusDTO> historicoDTO = model.getHistorico().stream()
                .map(h -> new HistoricoStatusDTO(h.getStatusAnterior(), h.getStatusNovo(), h.getDataHora()))
                .collect(Collectors.toList());

        return new SessaoDetalhesDTO(
                model.getId(),
                model.getDescricao(),
                model.getStatus(),
                model.getDuracao(),
                projetoDTO,
                estrategiaDTO,
                testerDTO,
                historicoDTO
        );
    }
}

