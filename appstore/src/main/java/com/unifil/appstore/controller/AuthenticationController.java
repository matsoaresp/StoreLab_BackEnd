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
    public ResponseEntity<ResponseAuthenticationDto> login(@RequestBody @Valid RequestAuthenticationDto dto) {
        ResponseAuthenticationDto response = authenticationService.autenticar(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RequestRegisterDto dto) {
        authenticationService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso");
    }

    @GetMapping("/validar-token")
    public ResponseEntity<String> validarToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authenticationService.validarToken(token);
            return ResponseEntity.ok("Token válido");
        }
        return ResponseEntity.badRequest().body("Token inválido");
    }

    @GetMapping("/info-usuario")
    public ResponseEntity<ResponseUsuarioDto> obterInfoUsuario(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            ResponseUsuarioDto usuario = authenticationService.obterUsuarioDoToken(token);
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.badRequest().build();
    }
}

