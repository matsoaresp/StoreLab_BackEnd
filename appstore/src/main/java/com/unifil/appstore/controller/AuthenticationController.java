package com.unifil.appstore.controller;

import com.unifil.appstore.dto.request.RequestAuthenticationDto;
import com.unifil.appstore.dto.request.RequestRegisterDto;
import com.unifil.appstore.dto.response.ResponseAuthenticationDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid RequestAuthenticationDto dto) {
        try {
            ResponseAuthenticationDto response = authenticationService.autenticar(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RequestRegisterDto dto) {
        try {
            authenticationService.registrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/validar-token")
    public ResponseEntity<String> validarToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                authenticationService.validarToken(token);
                return ResponseEntity.ok("Token válido");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body("Token inválido");
            }
        }
        return ResponseEntity.badRequest().body("Token ausente ou mal formatado");
    }

    @GetMapping("/info-usuario")
    public ResponseEntity<?> obterInfoUsuario(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                ResponseUsuarioDto usuario = authenticationService.obterUsuarioDoToken(token);
                return ResponseEntity.ok(usuario);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}