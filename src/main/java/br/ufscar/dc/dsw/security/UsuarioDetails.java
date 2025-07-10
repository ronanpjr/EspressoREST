package br.ufscar.dc.dsw.security;

import br.ufscar.dc.dsw.models.UsuarioModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UsuarioDetails implements UserDetails {

    private final UsuarioModel usuario;

    public UsuarioDetails(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getPapel().name().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDetails that = (UsuarioDetails) o;
        return Objects.equals(usuario.getId(), that.usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario.getId());
    }
}