package br.ufscar.dc.dsw.config.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {
    private final UsuarioSeeder usuarioSeeder;
    private final EstrategiaSeeder estrategiaSeeder;
    private final ProjetoSeeder projetoSeeder;

    public DatabaseSeeder(UsuarioSeeder usuarioSeeder, EstrategiaSeeder estrategiaSeeder, ProjetoSeeder projetoSeeder) {
        this.usuarioSeeder = usuarioSeeder;
        this.estrategiaSeeder = estrategiaSeeder;
        this.projetoSeeder = projetoSeeder;
    }

    public void seedDatabase() {
        System.out.println("Iniciando seed do banco de dados...");
        
        usuarioSeeder.seedUsuarios();
        estrategiaSeeder.seedEstrategias();
        projetoSeeder.seedProjetos();
        
        System.out.println("Seed do banco de dados concluído!");
    }
} 