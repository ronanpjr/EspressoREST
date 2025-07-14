package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.BugCadastroDTO;
import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.BugEdicaoDTO;
import br.ufscar.dc.dsw.dtos.SessaoDetalhesDTO;
import br.ufscar.dc.dsw.mappers.SessaoMapper;
import br.ufscar.dc.dsw.models.BugModel;
import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import br.ufscar.dc.dsw.repositories.BugRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BugService {

    private final BugRepository bugRepository;
    private final SessaoRepository sessaoRepository;
    private final SessaoMapper sessaoMapper;

    public BugService(BugRepository bugRepository, SessaoRepository sessaoRepository, SessaoMapper sessaoMapper) {
        this.bugRepository = bugRepository;
        this.sessaoRepository = sessaoRepository;
        this.sessaoMapper = sessaoMapper;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@securityService.podeAcessarSessao(#idSessao)")
    public List<BugDTO> buscarTodosBugsPorSessao(UUID idSessao, UsuarioModel usuarioLogado) {
        List<BugModel> bugs = bugRepository.findBySessaoIdOrderByDataRegistroDesc(idSessao);
        return bugs.stream().map(this::toBugDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@securityService.podeAcessarBug(#id)")
    public BugDTO buscarBugPorId(UUID id) {
        BugModel bug = bugRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bug não encontrado com ID: " + id));
        return toBugDTO(bug);
    }

    @Transactional
    @PreAuthorize("@securityService.podeAcessarSessao(#dto.idSessao())")
    public BugDTO salvarNovoBug(BugCadastroDTO dto) {
        SessaoModel sessao = sessaoRepository.findById(dto.idSessao())
                .orElseThrow(() -> new IllegalArgumentException("Sessão de teste não encontrada."));
        if (sessao.getStatus() == StatusSessao.FINALIZADO) {
            throw new IllegalStateException("Não é possível adicionar bugs a uma sessão com status FINALIZADO.");
        }
        BugModel novoBug = new BugModel();
        novoBug.setDescricao(dto.descricao());
        novoBug.setSessao(sessao);
        
        BugModel bugSalvo = bugRepository.save(novoBug);
        return toBugDTO(bugSalvo);
    }

    @Transactional
    @PreAuthorize("@securityService.podeAcessarBug(#dto.id())")
    public BugDTO atualizarBug(BugEdicaoDTO dto) {
        BugModel bugExistente = bugRepository.findById(dto.id())
                .orElseThrow(() -> new IllegalArgumentException("Bug não encontrado."));
        
        bugExistente.setDescricao(dto.descricao());
        bugExistente.setResolvido(dto.resolvido());
        
        BugModel bugAtualizado = bugRepository.save(bugExistente);
        return toBugDTO(bugAtualizado);
    }

    @Transactional
    @PreAuthorize("@securityService.podeAcessarBug(#id)")
    public void excluirBug(UUID id) {
        if (!bugRepository.existsById(id)) {
            throw new IllegalArgumentException("Bug não encontrado.");
        }
        bugRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@securityService.podeAcessarSessao(#idSessao)")
    public SessaoDetalhesDTO buscarDetalhesDaSessao(UUID idSessao) {
        SessaoModel sessao = sessaoRepository.findById(idSessao)
                .orElseThrow(() -> new IllegalArgumentException("Sessão de teste não encontrada com ID: " + idSessao));
        return sessaoMapper.toDetalhesDTO(sessao);
    }

    private BugDTO toBugDTO(BugModel bug) {
        return new BugDTO(
                bug.getId(),
                bug.getSessao().getId(),
                bug.getDescricao(),
                bug.getDataRegistro(),
                bug.isResolvido()
        );
    }
}