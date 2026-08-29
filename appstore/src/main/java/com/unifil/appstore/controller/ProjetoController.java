package com.unifil.appstore.controller;

import com.unifil.appstore.dto.request.RequestProjetoDto;
import com.unifil.appstore.dto.response.ResponseProjetoDto;
import com.unifil.appstore.models.Credencial;
import com.unifil.appstore.service.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService service;

    @PostMapping
    public ResponseEntity<ResponseProjetoDto> criarProjeto(
            @RequestBody @Valid RequestProjetoDto dto,
            @AuthenticationPrincipal Credencial credencial) {

        Long criadorId = credencial.getUsuario().getId();
        ResponseProjetoDto response = service.criarProjeto(dto, criadorId, credencial.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProjetoDto> encontrarProjeto(@PathVariable Long id) {
        ResponseProjetoDto response = service.encontrarProjeto(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ResponseProjetoDto>> listarProjetos() {
        List<ResponseProjetoDto> response = service.listarProjetos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meus")
    public ResponseEntity<List<ResponseProjetoDto>> listarMeusProjetos(
            @AuthenticationPrincipal Credencial credencial) {

        Long usuarioId = credencial.getUsuario().getId();
        List<ResponseProjetoDto> response = service.listarMeusProjetos(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseProjetoDto> atualizarProjeto(
            @PathVariable Long id,
            @RequestBody @Valid RequestProjetoDto dto,
            @AuthenticationPrincipal Credencial credencial) {

        ResponseProjetoDto response = service.atualizarProjeto(id, dto, credencial.getRole());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarProjeto(
            @PathVariable Long id,
            @AuthenticationPrincipal Credencial credencial) {

        service.inativarProjeto(id, credencial.getRole());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativarProjeto(
            @PathVariable Long id,
            @AuthenticationPrincipal Credencial credencial) {

        service.reativarProjeto(id, credencial.getRole());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProjeto(
            @PathVariable Long id,
            @AuthenticationPrincipal Credencial credencial) {

        service.excluirProjeto(id, credencial.getRole());
        return ResponseEntity.noContent().build();
    }
}