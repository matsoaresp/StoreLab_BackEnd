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

                if (revogado) {
                    throw new RuntimeException("Token revogado ou não reconhecido");
                }

                var credencial = credencialRepository.findByLogin(login)
                        .orElseThrow(() -> new RuntimeException("Credencial não encontrada"));

                var autenticacao = new UsernamePasswordAuthenticationToken(
                        credencial, null, credencial.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            } catch (RuntimeException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido ou expirado");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}