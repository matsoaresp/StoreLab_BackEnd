package com.unifil.appstore.service;

import com.unifil.appstore.dto.request.RequestUsuarioDto;
import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.usuario.Usuario;
import com.unifil.appstore.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
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

    public Usuario criarVisitante(RequestUsuarioDto dto) {

        Usuario usuario = criarUsuario(dto);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setRole(PersonRole.VISITOR);
        usuario.setAtivo(true);
        return repository.save(usuario);
    }

    public Usuario encontrarUsuario(Long id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new Exception("Usuario não encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public void deletarUsuario (Long id){
        repository.deleteById(id);
    }



}
