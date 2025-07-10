package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.SessaoDTO;
import br.ufscar.dc.dsw.dtos.SessaoEdicaoDTO;
import br.ufscar.dc.dsw.models.*;
import br.ufscar.dc.dsw.models.enums.Papel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SessaoService {
    private final SessaoRepository sessaoRepository;
    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;
    private final UsuarioRepository usuarioRepository;

    public SessaoService(
            SessaoRepository sessaoRepository,
            ProjetoRepository projetoRepository,
            EstrategiaRepository estrategiaRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.sessaoRepository = sessaoRepository;
        this.projetoRepository = projetoRepository;
        this.estrategiaRepository = estrategiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SessaoModel criarSessao(SessaoDTO dto, String emailTester) {
        ProjetoModel projeto = projetoRepository.findById(dto.projetoId())
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));
        EstrategiaModel estrategia = estrategiaRepository.findById(dto.estrategiaId())
                .orElseThrow(() -> new EntityNotFoundException("Estratégia não encontrada"));
        UsuarioModel testerManaged = usuarioRepository.findByEmail(emailTester)
                .orElseThrow(() -> new EntityNotFoundException("Tester com email " + emailTester + " não encontrado no banco de dados"));
        boolean isMembro = projeto.getMembros().contains(testerManaged);
        if (testerManaged.getPapel() != Papel.ADMIN && !isMembro) {
            throw new AccessDeniedException("Acesso negado. Apenas administradores ou membros do projeto podem criar sessões");
        }
        SessaoModel sessao = new SessaoModel();
        sessao.setProjeto(projeto);
        sessao.setEstrategia(estrategia);
        sessao.setTester(testerManaged);
        sessao.setDuracao(Duration.ofMinutes(dto.duracao()));
        sessao.setDescricao(dto.descricao());
        sessao.setStatus(StatusSessao.CRIADO);
        HistoricoStatusModel historico = new HistoricoStatusModel();
        historico.setSessao(sessao);
        historico.setStatusAnterior(null);
        historico.setStatusNovo(StatusSessao.CRIADO);
        sessao.getHistorico().add(historico);
        return sessaoRepository.save(sessao);
    }

    @Transactional
    public SessaoModel atualizarSessao(SessaoEdicaoDTO dto, UsuarioModel usuarioLogado) {
        SessaoModel sessao = getSessaoAndCheckOwnership(dto.id(), usuarioLogado);
        if (sessao.getStatus() != StatusSessao.CRIADO) {
            throw new IllegalStateException("Só é possível editar sessões com status 'CRIADO'.");
        }
        EstrategiaModel estrategia = estrategiaRepository.findById(dto.estrategiaId())
                .orElseThrow(() -> new EntityNotFoundException("Estratégia não encontrada"));
        sessao.setEstrategia(estrategia);
        sessao.setDuracao(Duration.ofMinutes(dto.duracao()));
        sessao.setDescricao(dto.descricao());
        return sessaoRepository.save(sessao);
    }

    @Transactional
    public SessaoModel atualizarStatus(UUID sessaoId, StatusSessao novoStatus, UsuarioModel usuarioLogado) {
        SessaoModel sessao = getSessaoAndCheckOwnership(sessaoId, usuarioLogado);
        StatusSessao statusAtual = sessao.getStatus();
        validarTransicaoStatus(statusAtual, novoStatus);
        HistoricoStatusModel historico = new HistoricoStatusModel();
        historico.setSessao(sessao);
        historico.setStatusAnterior(statusAtual);
        historico.setStatusNovo(novoStatus);
        historico.setDataHora(LocalDateTime.now());
        sessao.getHistorico().add(historico);
        sessao.setStatus(novoStatus);
        return sessaoRepository.save(sessao);
    }

    @Transactional
    public void deletarSessao(UUID id, UsuarioModel usuarioLogado) {
        SessaoModel sessao = getSessaoAndCheckOwnership(id, usuarioLogado);
        sessaoRepository.delete(sessao);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public SessaoModel buscarPorId(UUID id) {
        return sessaoRepository.findById(id).orElse(null);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<SessaoModel> listarPorProjeto(UUID projetoId) {
        return sessaoRepository.findAllByProjetoIdWithDetails(projetoId);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<SessaoModel> listarPorTester(String emailTester) {
        return sessaoRepository.findAllByTesterEmailWithDetails(emailTester);
    }

    private SessaoModel getSessaoAndCheckOwnership(UUID sessaoId, UsuarioModel usuarioLogado) {
        SessaoModel sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));
        if (usuarioLogado.getPapel() != Papel.ADMIN && !sessao.getTester().getEmail().equals(usuarioLogado.getEmail())) {
            throw new AccessDeniedException("Acesso negado. O usuário não é o dono do recurso ou um administrador");
        }
        return sessao;
    }

    private void validarTransicaoStatus(StatusSessao atual, StatusSessao novo) {
        if (atual == novo) return;
        boolean transicaoValida = switch (atual) {
            case CRIADO -> novo == StatusSessao.EM_EXECUCAO;
            case EM_EXECUCAO -> novo == StatusSessao.FINALIZADO;
            default -> false;
        };
        if (!transicaoValida) {
            throw new IllegalStateException("Transição de status inválida de " + atual + " para " + novo);
        }
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public SessaoModel buscarParaEdicao(UUID id, UsuarioModel usuarioLogado) {
        SessaoModel sessao = getSessaoAndCheckOwnership(id, usuarioLogado);
        if (sessao.getStatus() != StatusSessao.CRIADO) {
            throw new IllegalStateException("Apenas sessões com status 'CRIADO' podem ser editadas");
        }
        return sessao;
    }
}
