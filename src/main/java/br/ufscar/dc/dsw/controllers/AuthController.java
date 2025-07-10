package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.security.TokenProvider;
import br.ufscar.dc.dsw.dtos.LoginRequestDTO;
import br.ufscar.dc.dsw.dtos.LoginResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager; // Injete este Bean na sua WebSecurityConfig

    @Autowired
    private TokenProvider tokenProvider; // Serviço para gerar e validar tokens

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.senha()
        );

        // O Spring Security usa o AuthenticationManager para validar as credenciais
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Se a autenticação for bem-sucedida, gera um token JWT
        String token = tokenProvider.generateToken(authentication);

        // Retorna o token no corpo da resposta
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
