package com.unifil.appstore.service.auth;

import com.unifil.appstore.dto.request.RequestAuthenticationDto;
import com.unifil.appstore.dto.response.ResponseAuthenticationDto;
import com.unifil.appstore.models.usuario.Usuario;
import com.unifil.appstore.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
                    new UsernamePasswordAuthenticationToken(
                            dto.getLogin(),
                            dto.getSenha()
                    )
            );

            Usuario usuario = usuarioRepository.findByLogin(dto.getLogin());

            if (usuario == null || !usuario.isAtivo()) {
                throw new RuntimeException("Usuário não encontrado ou inativo");
            }


            String token = tokenService.gerarToken(usuario);

            ResponseAuthenticationDto response = new ResponseAuthenticationDto();
            response.setToken(token);
            response.setTipo("Bearer");
            response.setUserId(usuario.getId());
            response.setNome(usuario.getNome());
            response.setEmail(usuario.getEmail());
            response.setRole(usuario.getRole());
            response.setTempoExpiracao(expiration * 3600);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação: " + e.getMessage());
        }
    }

    public void validarToken(String token) {
        tokenService.validarToken(token);
    }

    public Usuario obterUsuarioDoToken(String token) {
        String login = tokenService.validarToken(token);
        return (Usuario) usuarioRepository.findByLogin(login);
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
