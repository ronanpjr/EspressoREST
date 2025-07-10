package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.models.BugModel;
import br.ufscar.dc.dsw.models.SessaoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.Papel;
import br.ufscar.dc.dsw.repositories.BugRepository;
import br.ufscar.dc.dsw.repositories.SessaoRepository;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
public class SecurityService {

    @Autowired
    private SessaoRepository sessaoRepository;
    @Autowired
    private BugRepository bugRepository;

    public UsuarioModel getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UsuarioDetails) {
            return ((UsuarioDetails) principal).getUsuario();
        }
        return null;
    }

    public boolean podeAcessarSessao(UUID idSessao) {
        UsuarioModel usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) return false;
        if (Papel.ADMIN.equals(usuarioLogado.getPapel())) return true;

        SessaoModel sessao = sessaoRepository.findById(idSessao).orElse(null);
        return sessao != null && Papel.TESTER.equals(usuarioLogado.getPapel()) &&
               usuarioLogado.getId().equals(sessao.getTester().getId());
    }

    public boolean podeAcessarBug(UUID idBug) {
        UsuarioModel usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) return false;
        if (Papel.ADMIN.equals(usuarioLogado.getPapel())) return true;

        BugModel bug = bugRepository.findById(idBug).orElse(null);
        return bug != null && Papel.TESTER.equals(usuarioLogado.getPapel()) &&
               usuarioLogado.getId().equals(bug.getSessao().getTester().getId());
    }
}