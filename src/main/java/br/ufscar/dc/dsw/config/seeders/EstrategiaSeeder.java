package br.ufscar.dc.dsw.config.seeders;

import br.ufscar.dc.dsw.models.DicaModel;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.ExemploModel;
import br.ufscar.dc.dsw.repositories.DicaRepository;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.ExemploRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaSeeder {
    private final EstrategiaRepository estrategiaRepository;
    private final DicaRepository dicaRepository;
    private final ExemploRepository exemploRepository;

    public EstrategiaSeeder(EstrategiaRepository estrategiaRepository, DicaRepository dicaRepository, ExemploRepository exemploRepository) {
        this.estrategiaRepository = estrategiaRepository;
        this.dicaRepository = dicaRepository;
        this.exemploRepository = exemploRepository;
    }

    public void seedEstrategias() {
        if (estrategiaRepository.count() > 0) {
            System.out.println("✓ Estratégias já existem no banco de dados.");
            return;
        }

        System.out.println("Criando estratégias iniciais...");

        // Estratégia 1: Teste de Interface
        EstrategiaModel estrategia1 = criarEstrategiaInterface();
        
        // Estratégia 2: Teste de Performance
        EstrategiaModel estrategia2 = criarEstrategiaPerformance();
        
        // Estratégia 3: Teste de Segurança
        EstrategiaModel estrategia3 = criarEstrategiaSeguranca();

        System.out.println("✓ Estratégias iniciais criadas com sucesso!");
        System.out.println("  - " + estrategia1.getNome());
        System.out.println("  - " + estrategia2.getNome());
        System.out.println("  - " + estrategia3.getNome());
    }

    private EstrategiaModel criarEstrategiaInterface() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Teste de Interface de Usuário");
        estrategia.setDescricao("Estratégia para testar a interface do usuário, incluindo elementos visuais, navegação e responsividade.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Sempre teste em diferentes resoluções de tela para garantir responsividade.");
        criarDica(estrategia, "Verifique se todos os elementos clicáveis têm estados visuais distintos (hover, focus, active).");
        criarDica(estrategia, "Teste a navegação por teclado para garantir acessibilidade.");
        criarDica(estrategia, "Valide se as cores têm contraste adequado para leitura.");
        criarDica(estrategia, "Teste a funcionalidade em diferentes navegadores.");

        criarExemplo(estrategia, "Botão de login que muda de cor quando o mouse passa por cima", "/uploads/estrategias/exemplo-botao-hover.jpg");
        criarExemplo(estrategia, "Menu responsivo que se adapta a telas menores", "/uploads/estrategias/exemplo-menu-responsivo.jpg");
        criarExemplo(estrategia, "Formulário com validação visual em tempo real", "/uploads/estrategias/exemplo-validacao-formulario.jpg");

        return estrategia;
    }

    private EstrategiaModel criarEstrategiaPerformance() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Teste de Performance");
        estrategia.setDescricao("Estratégia para avaliar o desempenho da aplicação, incluindo tempo de resposta e uso de recursos.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Monitore o tempo de carregamento das páginas em diferentes conexões de internet.");
        criarDica(estrategia, "Teste com diferentes volumes de dados para identificar gargalos.");
        criarDica(estrategia, "Verifique o uso de memória e CPU durante operações intensivas.");
        criarDica(estrategia, "Teste a performance de consultas ao banco de dados.");
        criarDica(estrategia, "Monitore o tempo de resposta de APIs externas.");

        criarExemplo(estrategia, "Gráfico de tempo de resposta de uma API", "/uploads/estrategias/exemplo-grafico-performance.jpg");
        criarExemplo(estrategia, "Monitoramento de uso de memória durante testes de carga", "/uploads/estrategias/exemplo-monitoramento-memoria.jpg");
        criarExemplo(estrategia, "Análise de consultas lentas no banco de dados", "/uploads/estrategias/exemplo-consultas-lentas.jpg");

        return estrategia;
    }

    private EstrategiaModel criarEstrategiaSeguranca() {
        EstrategiaModel estrategia = new EstrategiaModel();
        estrategia.setNome("Teste de Segurança");
        estrategia.setDescricao("Estratégia para identificar vulnerabilidades de segurança na aplicação.");
        estrategia = estrategiaRepository.save(estrategia);

        criarDica(estrategia, "Teste injeção de SQL em todos os campos de entrada.");
        criarDica(estrategia, "Verifique se dados sensíveis não são expostos em logs ou respostas de erro.");
        criarDica(estrategia, "Teste autenticação e autorização em todas as rotas protegidas.");
        criarDica(estrategia, "Valide proteção contra ataques XSS (Cross-Site Scripting).");
        criarDica(estrategia, "Teste proteção contra CSRF (Cross-Site Request Forgery).");
        criarDica(estrategia, "Verifique se senhas são armazenadas de forma segura (hash).");

        criarExemplo(estrategia, "Tentativa de acesso não autorizado a uma área restrita", "/uploads/estrategias/exemplo-acesso-negado.jpg");
        criarExemplo(estrategia, "Validação de entrada contra XSS", "/uploads/estrategias/exemplo-validacao-xss.jpg");
        criarExemplo(estrategia, "Teste de força bruta em formulário de login", "/uploads/estrategias/exemplo-forca-bruta.jpg");
        criarExemplo(estrategia, "Verificação de tokens CSRF em formulários", "/uploads/estrategias/exemplo-token-csrf.jpg");

        return estrategia;
    }

    private void criarDica(EstrategiaModel estrategia, String texto) {
        DicaModel dica = new DicaModel();
        dica.setTexto(texto);
        dica.setEstrategia(estrategia);
        dicaRepository.save(dica);
    }

    private void criarExemplo(EstrategiaModel estrategia, String texto, String urlImagem) {
        ExemploModel exemplo = new ExemploModel();
        exemplo.setTexto(texto);
        exemplo.setUrlImagem(urlImagem);
        exemplo.setEstrategia(estrategia);
        exemploRepository.save(exemplo);
    }
} 