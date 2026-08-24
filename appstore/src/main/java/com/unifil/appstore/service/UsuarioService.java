package com.unifil.appstore.service;

import com.unifil.appstore.dto.request.RequestUpdateUsuarioDto;
import com.unifil.appstore.dto.request.RequestUsuarioDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.enums.person.PersonRole;
import com.unifil.appstore.models.Credencial;
import com.unifil.appstore.models.Usuario;
import com.unifil.appstore.repository.CredencialRepository;
import com.unifil.appstore.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
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
    private final CredencialRepository credencialRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repository, CredencialRepository credencialRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.credencialRepository = credencialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Usuario construirUsuario(RequestUsuarioDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setAtivo(true);
        return usuario;
    }

    private Credencial construirCredencial(RequestUsuarioDto dto, Usuario usuario, PersonRole role) {
        Credencial credencial = new Credencial();
        credencial.setLogin(gerarLogin(dto.getNome()));
        credencial.setSenha(passwordEncoder.encode(dto.getSenha()));
        credencial.setRole(role);
        credencial.setUsuario(usuario);
        return credencial;
    }

    private ResponseUsuarioDto converterParaDto(Usuario usuario) {
        Credencial credencial = usuario.getCredencial();
        return new ResponseUsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                credencial.getLogin(),
                credencial.getRole(),
                usuario.isAtivo(),
                usuario.getDataCriacao()
        );
    }

    @Transactional
    public ResponseUsuarioDto criarAluno(RequestUsuarioDto dto) {
        Usuario usuario = repository.save(construirUsuario(dto));
        credencialRepository.save(construirCredencial(dto, usuario, PersonRole.STUDENT));
        return converterParaDto(usuario);
    }

    @Transactional
    public ResponseUsuarioDto criarProfessor(RequestUsuarioDto dto) {
        Usuario usuario = repository.save(construirUsuario(dto));
        credencialRepository.save(construirCredencial(dto, usuario, PersonRole.ADMIN));
        return converterParaDto(usuario);
    }

    public ResponseUsuarioDto encontrarUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return converterParaDto(usuario);
    }

    public ResponseUsuarioDto encontrarProfessor(Long id) {
        Credencial credencial = credencialRepository.findByUsuario_IdAndRole(id, PersonRole.ADMIN)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
        return converterParaDto(credencial.getUsuario());
    }

    public ResponseUsuarioDto encontrarAluno(Long id) {
        Credencial credencial = credencialRepository.findByUsuario_IdAndRole(id, PersonRole.STUDENT)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        return converterParaDto(credencial.getUsuario());
    }

    public List<ResponseUsuarioDto> listarUsuarios() {
        return repository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUsuarioDto> listarAlunos() {
        return credencialRepository.findAllByRole(PersonRole.STUDENT).stream()
                .map(Credencial::getUsuario)
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUsuarioDto> listarProfessores() {
        return credencialRepository.findAllByRole(PersonRole.ADMIN).stream()
                .map(Credencial::getUsuario)
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseUsuarioDto atualizarUsuario(Long id, RequestUpdateUsuarioDto dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        repository.save(usuario);

        return converterParaDto(usuario);
    }

    @Transactional
    public void removerUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        repository.delete(usuario);
    }

    @Transactional
    public int inativarUsuario(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setAtivo(false);
        repository.save(usuario);

        return 1;
    }

    private String gerarLogin(String nome) {
        String login = nome.toLowerCase().replaceAll("\\s+", "");
        if (credencialRepository.existsByLogin(login)) {
            login = login + "_" + UUID.randomUUID().toString().substring(0, 4);
        }
        return login;
    }
}