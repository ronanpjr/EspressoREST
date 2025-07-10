package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.UsuarioCadastroDTO;
import br.ufscar.dc.dsw.dtos.UsuarioDTO;
import br.ufscar.dc.dsw.dtos.UsuarioEdicaoDTO;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.Papel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioDTO salvarNovoUsuario(UsuarioCadastroDTO usuarioCadastroDTO) {
        Optional<UsuarioModel> existingUser = usuarioRepository.findByEmail(usuarioCadastroDTO.email());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("email.error.duplicate");
        }

        UsuarioModel usuario = new UsuarioModel();

        usuario.setNome(usuarioCadastroDTO.nome());
        usuario.setEmail(usuarioCadastroDTO.email());
        usuario.setSenha(passwordEncoder.encode(usuarioCadastroDTO.senha()));
        usuario.setPapel(usuarioCadastroDTO.papel()); // Get papel directly from DTO

        try {
            UsuarioModel savedUsuario = usuarioRepository.save(usuario);
            return new UsuarioDTO(savedUsuario.getId(), savedUsuario.getNome(), savedUsuario.getEmail(), savedUsuario.getPapel());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("user.error.dataIntegrityViolation", e);
        }
    }

    public UsuarioDTO atualizarUsuario(UsuarioEdicaoDTO usuarioEdicaoDTO) {
        if (usuarioEdicaoDTO.id() == null) {
            throw new IllegalArgumentException("user.error.idRequiredForUpdate");
        }

        UsuarioModel existingUsuario = usuarioRepository.findById(usuarioEdicaoDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("user.error.notFoundById"));

        if (!existingUsuario.getEmail().equals(usuarioEdicaoDTO.email())) {
            Optional<UsuarioModel> userWithNewEmail = usuarioRepository.findByEmail(usuarioEdicaoDTO.email());
            if (userWithNewEmail.isPresent() && !userWithNewEmail.get().getId().equals(usuarioEdicaoDTO.id())) {
                throw new IllegalArgumentException("email.error.duplicate.anotherUser");
            }
        }

        existingUsuario.setNome(usuarioEdicaoDTO.nome());
        existingUsuario.setEmail(usuarioEdicaoDTO.email());

        if (usuarioEdicaoDTO.senha() != null && !usuarioEdicaoDTO.senha().isEmpty()) {
            existingUsuario.setSenha(passwordEncoder.encode(usuarioEdicaoDTO.senha()));
        }
        if (usuarioEdicaoDTO.papel() != null) {
            existingUsuario.setPapel(usuarioEdicaoDTO.papel());
        }

        try {
            UsuarioModel updatedUsuario = usuarioRepository.save(existingUsuario);
            return new UsuarioDTO(updatedUsuario.getId(), updatedUsuario.getNome(), updatedUsuario.getEmail(), updatedUsuario.getPapel());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("user.error.dataIntegrityViolation", e);
        }
    }

    public void excluir(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("user.error.notFound");
        }
        try {
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("user.error.deletionFailedDependencies", e);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(UUID id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user.error.notFoundById"));
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPapel());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPapel()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        return usuario;
    }
}