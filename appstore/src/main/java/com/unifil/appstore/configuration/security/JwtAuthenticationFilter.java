package com.unifil.appstore.configuration.security;

import com.unifil.appstore.repository.CredencialRepository;
import com.unifil.appstore.repository.TokensRepository;
import com.unifil.appstore.service.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CredencialRepository credencialRepository;

    @Autowired
    private TokensRepository tokensRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String login = tokenService.validarToken(token);

                boolean revogado = tokensRepository.findByToken(token)
                        .map(t -> t.isRevogado())
                        .orElse(true);

                if (!revogado) {
                    var credencial = credencialRepository.findByLogin(login).orElse(null);
                    if (credencial != null) {
                        var autenticacao = new UsernamePasswordAuthenticationToken(
                                credencial, null, credencial.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(autenticacao);
                    }
                }
            } catch (RuntimeException e) {

                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}