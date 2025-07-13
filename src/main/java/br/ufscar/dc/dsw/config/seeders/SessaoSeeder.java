package br.ufscar.dc.dsw.config.seeders;

import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.HistoricoStatusModel;
import br.ufscar.dc.dsw.models.ProjetoModel;
import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.StatusSessao;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class SessaoSeeder {

    private final SessaoRepository sessaoRepository;
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstrategiaRepository estrategiaRepository;

    public SessaoSeeder(
            SessaoRepository sessaoRepository,
            ProjetoRepository projetoRepository,
            UsuarioRepository usuarioRepository,
            EstrategiaRepository estrategiaRepository
    ) {
        this.sessaoRepository = sessaoRepository;
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estrategiaRepository = estrategiaRepository;
    }

    public void seedSessoes() {
        if (sessaoRepository.count() > 0) {
            System.out.println("✓ Sessões de teste já existem no banco de dados.");
            return;
        }

        System.out.println("Criando sessões de teste iniciais...");

        try {
            // Buscar entidades dependentes
            UsuarioModel tester = usuarioRepository.findByEmail("tester@tester.com")
                    .orElseThrow(() -> new IllegalStateException("Usuário 'tester' não encontrado."));
            ProjetoModel projetoAlpha = projetoRepository.findByNome("Projeto Alpha")
                    .orElseThrow(() -> new IllegalStateException("Projeto 'Alpha' não encontrado."));
            List<EstrategiaModel> estrategias = estrategiaRepository.findAll();
            if (estrategias.isEmpty()) {
                throw new IllegalStateException("Nenhuma estratégia de teste encontrada para associar às sessões.");
            }

            criarSessao(
                    projetoAlpha,
                    tester,
                    estrategias.get(0),
                    60,
                    "Focar nos testes de usabilidade do novo painel de controle.",
                    StatusSessao.CRIADO
            );

            criarSessao(
                    projetoAlpha,
                    tester,
                    estrategias.get(1),
                    120,
                    "Verificar a performance da API de relatórios com carga de dados massiva.",
                    StatusSessao.EM_EXECUCAO
            );

            System.out.println("✓ Sessões de teste iniciais criadas com sucesso!");

        } catch (IllegalStateException e) {
            System.err.println("X Falha ao criar sessões de teste: " + e.getMessage());
        }
    }

    private void criarSessao(ProjetoModel projeto, UsuarioModel tester, EstrategiaModel estrategia, long duracaoMinutos, String descricao, StatusSessao status) {
        SessaoModel sessao = new SessaoModel();
        sessao.setProjeto(projeto);
        sessao.setTester(tester);
        sessao.setEstrategia(estrategia);
        sessao.setDuracao(Duration.ofMinutes(duracaoMinutos));
        sessao.setDescricao(descricao);
        sessao.setStatus(status);

        // Adiciona o histórico de criação
        HistoricoStatusModel historicoCriacao = new HistoricoStatusModel();
        historicoCriacao.setStatusNovo(StatusSessao.CRIADO);
        historicoCriacao.setSessao(sessao);
        sessao.getHistorico().add(historicoCriacao);

        if (status != StatusSessao.CRIADO) {
            HistoricoStatusModel historicoAdicional = new HistoricoStatusModel();
            historicoAdicional.setStatusAnterior(StatusSessao.CRIADO);
            historicoAdicional.setStatusNovo(status);
            historicoAdicional.setSessao(sessao);
            sessao.getHistorico().add(historicoAdicional);
        }

        sessaoRepository.save(sessao);
    }
}