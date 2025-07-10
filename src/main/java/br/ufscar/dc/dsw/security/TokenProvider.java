package br.ufscar.dc.dsw.security;

import br.ufscar.dc.dsw.models.UsuarioModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenProvider {

    // A chave secreta deve ser longa e complexa. Guarde-a de forma segura!
    // Idealmente, viria do seu application.properties ou de uma variável de ambiente.
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Tempo de expiração do token em milissegundos (ex: 1 hora)
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // Gera o token JWT a partir das informações de autenticação do usuário.
    public String generateToken(Authentication authentication) {
        UsuarioModel usuarioPrincipal = (UsuarioModel) authentication.getPrincipal();
        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + jwtExpirationMs);

        // A chave secreta é convertida para um formato seguro para assinar o token.
        SecretKey chaveSecreta = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(usuarioPrincipal.getUsername()) // O "subject" do token será o email do usuário.
                .issuedAt(agora)
                .expiration(dataExpiracao)
                .signWith(chaveSecreta) // Assina o token com a chave secreta.
                .compact();
    }

    // Extrai o email do usuário (o "subject") a partir do token.
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Valida se o token é válido (não expirou e a assinatura confere).
    public boolean validateToken(String token) {
        try {
            // Se conseguir fazer o parse sem lançar exceção, o token é válido.
            getAllClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            // Pode logar o erro aqui se desejar (ex: token expirado, assinatura inválida, etc.)
            return false;
        }
    }

    // Função genérica para extrair qualquer "claim" (informação) do token.
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Extrai todas as informações (claims) do token usando a chave secreta.
    private Claims getAllClaimsFromToken(String token) {
        SecretKey chaveSecreta = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(chaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}