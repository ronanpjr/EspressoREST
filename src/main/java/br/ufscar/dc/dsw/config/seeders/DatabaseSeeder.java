package br.ufscar.dc.dsw.config.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {
    private final UsuarioSeeder usuarioSeeder;
    private final EstrategiaSeeder estrategiaSeeder;
    private final ProjetoSeeder projetoSeeder;
    private final SessaoSeeder sessaoSeeder;

    public DatabaseSeeder(UsuarioSeeder usuarioSeeder, EstrategiaSeeder estrategiaSeeder, ProjetoSeeder projetoSeeder, SessaoSeeder sessaoSeeder) {
        this.usuarioSeeder = usuarioSeeder;
        this.estrategiaSeeder = estrategiaSeeder;
        this.projetoSeeder = projetoSeeder;
        this.sessaoSeeder = sessaoSeeder;
    }

    public void seedDatabase() {
        System.out.println("Iniciando seed do banco de dados...");
        
        usuarioSeeder.seedUsuarios();
        estrategiaSeeder.seedEstrategias();
        projetoSeeder.seedProjetos();
        sessaoSeeder.seedSessoes();
        
        System.out.println("Seed do banco de dados concluído!");
    }
} 