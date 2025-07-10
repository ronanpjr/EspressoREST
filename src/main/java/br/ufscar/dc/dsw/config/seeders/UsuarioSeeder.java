package br.ufscar.dc.dsw.config.seeders;

import br.ufscar.dc.dsw.dtos.UsuarioCadastroDTO;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.Papel;
import br.ufscar.dc.dsw.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioSeeder {
    private final UsuarioService usuarioService;

    public UsuarioSeeder(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void seedUsuarios() {
        System.out.println("Verificando usuários iniciais...");

        // Criar usuário 'admin' se não existir
        UsuarioModel adminExistente = usuarioService.buscarPorEmail("admin@admin.com");
        if (adminExistente == null) {
            UsuarioCadastroDTO admin = new UsuarioCadastroDTO(
                    null, // ID is null for new users
                    "Administrador",
                    "admin@admin.com",
                    "admin",
                    Papel.ADMIN
            );
            usuarioService.salvarNovoUsuario(admin);
            System.out.println("✓ Usuário 'admin' criado.");
        } else {
            System.out.println("✓ Usuário 'admin' já existe.");
        }

        // Criar usuário 'tester' se não existir
        UsuarioModel testerExistente = usuarioService.buscarPorEmail("tester@tester.com");
        if (testerExistente == null) {
            UsuarioCadastroDTO tester = new UsuarioCadastroDTO(
                    null, // ID is null for new users
                    "Tester",
                    "tester@tester.com",
                    "tester",
                    Papel.TESTER
            );
            usuarioService.salvarNovoUsuario(tester);
            System.out.println("✓ Usuário 'tester' criado.");
        } else {
            System.out.println("✓ Usuário 'tester' já existe.");
        }
    }
} 