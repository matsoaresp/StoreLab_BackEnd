package com.unifil.appstore.service;

import com.unifil.appstore.dto.request.RequestUsuarioDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.usuario.Usuario;
import com.unifil.appstore.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método auxiliar privado: Monta o objeto em memória sem salvar (evita duplo save)
    private Usuario construirUsuarioBase(RequestUsuarioDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setLogin(gerarLogin(dto.getNome()));
        usuario.setAtivo(true);
        return usuario;
    }

    public ResponseUsuarioDto criarAluno(RequestUsuarioDto dto) {
        Usuario usuario = construirUsuarioBase(dto);
        usuario.setRole(PersonRole.STUDENT);
        return converterParaDto(repository.save(usuario));
    }

    public ResponseUsuarioDto criarProfessor(RequestUsuarioDto dto) {
        Usuario usuario = construirUsuarioBase(dto);
        usuario.setRole(PersonRole.ADMIN);
        return converterParaDto(repository.save(usuario));
    }

    // Busca usuário genérico (usado internamente)
    private Usuario buscarEntityPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public ResponseUsuarioDto encontrarUsuario(Long id) {
        return converterParaDto(buscarEntityPorId(id));
    }

    public ResponseUsuarioDto encontrarAlunoPorId(Long id) {
        Usuario aluno = repository.findByIdAndRole(id, PersonRole.STUDENT)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        return converterParaDto(aluno);
    }

    public List<ResponseUsuarioDto> listarUsuarios() {
        return repository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUsuarioDto> listarAlunos() {
        return repository.findAllByRole(PersonRole.STUDENT).stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUsuarioDto> listarProfessores() {
        return repository.findAllByRole(PersonRole.ADMIN).stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public ResponseUsuarioDto atualizarDados(Long id, RequestUsuarioDto dto) {
        Usuario usuario = buscarEntityPorId(id);

        if (!dto.getEmail().equals(usuario.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já está em uso por outra conta");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());


        if(dto.getRole() != null) {
            usuario.setRole(dto.getRole());
        }

        Usuario updated = repository.save(usuario);
        return converterParaDto(updated);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = buscarEntityPorId(id);
        repository.delete(usuario);
    }

    private String gerarLogin(String nome) {
        String login = nome.toLowerCase().replaceAll("\\s+", "");
        if (repository.existsByLogin(login)) {
            login = login + "_" + UUID.randomUUID().toString().substring(0, 4);
        }
        return login;
    }


    private ResponseUsuarioDto converterParaDto(Usuario usuario) {
        return new ResponseUsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getRole(),
                usuario.isAtivo(),
                usuario.getDataCriacao()
        );
    }
}