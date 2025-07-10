/*
package br.ufscar.dc.dsw.controllers; // Certifique-se do pacote correto

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication; // Importar
import org.springframework.security.core.context.SecurityContextHolder; // Importar

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Verifica se o principal não é "anonymousUser" se você quiser diferenciar
            if (!"anonymousUser".equals(authentication.getPrincipal())) {
                System.out.println("DEBUG: Usuário autenticado na Home: " + authentication.getName());
                System.out.println("DEBUG: Papéis do usuário: " + authentication.getAuthorities());
            } else {
                System.out.println("DEBUG: Usuário ANÔNIMO na Home. (Principal: anonymousUser)");
            }
        } else {
            System.out.println("DEBUG: Objeto Authentication é NULO ou NÃO AUTENTICADO na Home.");
        }
        return "home";
    }
}*/
