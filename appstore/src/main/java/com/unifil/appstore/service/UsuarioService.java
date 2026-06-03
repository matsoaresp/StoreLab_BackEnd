package com.unifil.appstore.service;

import com.unifil.appstore.dto.request.RequestUsuarioDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.usuario.Usuario;
import com.unifil.appstore.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(RequestUsuarioDto dto) {
        Usuario usuario = new Usuario(
                dto.getNome(),
                dto.getEmail(),
                dto.getMatricula(),
                dto.getSenha()
        );

        return repository.save(usuario);
    }

    public Usuario criarAluno(RequestUsuarioDto dto) {

        Usuario usuario = criarUsuario(dto);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setRole(PersonRole.STUDENT);
        usuario.setAtivo(true);
        return repository.save(usuario);
    }


    public Usuario criarProfesssor(RequestUsuarioDto dto) {

        Usuario usuario = criarUsuario(dto);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setRole(PersonRole.ADMIN);
        usuario.setAtivo(true);
        return repository.save(usuario);
    }

    public Usuario encontrarUsuario(Long id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new Exception("Usuario não encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public void deletarUsuario(Long id) throws Exception {
        Usuario usuario = encontrarUsuario(id);
        repository.deleteById(usuario.getId());
    }

    public ResponseUsuarioDto atualizarDados(Long id, RequestUsuarioDto dto) throws Exception {

        Usuario usuario = encontrarUsuario(id);
        if (!dto.getEmail().equals(usuario.getEmail()) &&
            usuarioRepository.existsByEmail(dto.getEmail())){
            throw new Exception("Email já Existe");
        }

        usuario.setEmail(dto.getEmail());
        usuario.setMatricula(dto.getMatricula());
        Usuario updated = repository.save(usuario);
        return new ResponseUsuarioDto(updated.getId(), updated.getEmail(), updated.getMatricula());

    }
}
