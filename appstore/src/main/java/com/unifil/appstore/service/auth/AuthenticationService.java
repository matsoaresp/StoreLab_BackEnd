package com.unifil.appstore.service.auth;

import com.unifil.appstore.dto.request.RequestAuthenticationDto;
import com.unifil.appstore.dto.request.RequestRegisterDto;
import com.unifil.appstore.dto.response.ResponseAuthenticationDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.models.Credencial;
import com.unifil.appstore.models.Tokens;
import com.unifil.appstore.models.Usuario;
import com.unifil.appstore.repository.CredencialRepository;
import com.unifil.appstore.repository.TokensRepository;
import com.unifil.appstore.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CredencialRepository credencialRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    public ResponseAuthenticationDto autenticar(RequestAuthenticationDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha())
            );

            Credencial credencial = credencialRepository.findByLogin(dto.getLogin())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou inativo"));

            Usuario usuario = credencial.getUsuario();
            String token = tokenService.gerarToken(credencial);

            Tokens registro = new Tokens();
            registro.setToken(token);
            registro.setRevogado(false);
            registro.setExpirado(false);
            registro.setCredencial(credencial);
            tokensRepository.save(registro);

            ResponseAuthenticationDto response = new ResponseAuthenticationDto();
            response.setToken(token);
            response.setTipo("Bearer");
            response.setUserId(usuario.getId());
            response.setNome(usuario.getNome());
            response.setEmail(usuario.getEmail());
            response.setRole(credencial.getRole());
            response.setTempoExpiracao(expiration * 3600);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void registrar(RequestRegisterDto dto) {
        if (credencialRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new RuntimeException("Já existe um usuário com este login.");
        }
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Já existe um usuário com este email.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setDataCriacao(LocalDateTime.now());
        novoUsuario.setAtivo(true);
        usuarioRepository.save(novoUsuario);

        Credencial novaCredencial = new Credencial();
        novaCredencial.setLogin(dto.getLogin());
        novaCredencial.setSenha(passwordEncoder.encode(dto.getSenha()));
        novaCredencial.setRole(dto.getRole());
        novaCredencial.setUsuario(novoUsuario);
        credencialRepository.save(novaCredencial);
    }

    @Transactional
    public void logout(String token) {
        Tokens registro = tokensRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado"));
        registro.setRevogado(true);
        tokensRepository.save(registro);
    }

    public void validarToken(String token) {
        tokenService.validarToken(token);
        verificarNaoRevogado(token);
    }

    public void verificarNaoRevogado(String token) {
        Tokens registro = tokensRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado"));
        if (registro.isRevogado()) {
            throw new RuntimeException("Token revogado");
        }
    }

    public ResponseUsuarioDto obterUsuarioDoToken(String token) {
        String login = tokenService.validarToken(token);
        verificarNaoRevogado(token);

        Credencial credencial = credencialRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Usuario usuario = credencial.getUsuario();

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

    public String obterLoginDoToken(String token) {
        return tokenService.validarToken(token);
    }

    public Long obterIdDoToken(String token) {
        return tokenService.obterIdDirecaoToken(token);
    }

    public String obterRoleDoToken(String token) {
        return tokenService.obterRoleDirecaoToken(token);
    }
}