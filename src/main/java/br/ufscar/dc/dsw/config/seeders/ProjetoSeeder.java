package br.ufscar.dc.dsw.config.seeders;

import br.ufscar.dc.dsw.models.ProjetoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProjetoSeeder {
    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProjetoSeeder(ProjetoRepository projetoRepository, UsuarioRepository usuarioRepository) {
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void seedProjetos() {
        if (projetoRepository.count() > 0) {
            System.out.println("✓ Projetos já existem no banco de dados.");
            return;
        }

        System.out.println("Criando projetos iniciais...");

        UsuarioModel admin = usuarioRepository.findByEmail("admin@admin.com").orElse(null);
        UsuarioModel tester = usuarioRepository.findByEmail("tester@tester.com").orElse(null);

        if (admin == null || tester == null) {
            System.out.println("X Falha ao criar projetos: usuários 'admin' ou 'tester' não encontrados.");
            return;
        }

        ProjetoModel projetoAlpha = new ProjetoModel();
        projetoAlpha.setNome("Projeto Alpha");
        projetoAlpha.setDescricao("Desenvolvimento do novo sistema de gerenciamento de inventário.");
        Set<UsuarioModel> membrosAlpha = new HashSet<>();
        membrosAlpha.add(admin);
        membrosAlpha.add(tester);
        projetoAlpha.setMembros(membrosAlpha);
        projetoRepository.save(projetoAlpha);

        ProjetoModel projetoBeta = new ProjetoModel();
        projetoBeta.setNome("Projeto Beta");
        projetoBeta.setDescricao("Campanha de marketing para o lançamento do Q3.");
        Set<UsuarioModel> membrosBeta = new HashSet<>();
        membrosBeta.add(admin);
        projetoBeta.setMembros(membrosBeta);
        projetoRepository.save(projetoBeta);
        System.out.println("✓ Projetos iniciais criados com sucesso!");
    }
}