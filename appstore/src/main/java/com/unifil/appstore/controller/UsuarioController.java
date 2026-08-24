package com.unifil.appstore.controller;

import com.unifil.appstore.dto.request.RequestUsuarioDto;
import com.unifil.appstore.dto.request.RequestUpdateUsuarioDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping(value = "/alunos")
    public ResponseEntity<ResponseUsuarioDto> criarAluno(@RequestBody @Valid RequestUsuarioDto dto) {
        ResponseUsuarioDto response = service.criarAluno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/professores")
    public ResponseEntity<ResponseUsuarioDto> criarProfessor(@RequestBody @Valid RequestUsuarioDto dto) {
        ResponseUsuarioDto response = service.criarProfessor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> encontrarUsuario(@PathVariable Long id) {
        ResponseUsuarioDto response = service.encontrarUsuario(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aluno/{id}")
    public ResponseEntity<ResponseUsuarioDto> encontrarAluno(@PathVariable Long id) {
        ResponseUsuarioDto response = service.encontrarAluno(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professor/{id}")
    public ResponseEntity<ResponseUsuarioDto> encontrarProfessor(@PathVariable Long id) {
        ResponseUsuarioDto response = service.encontrarProfessor(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ResponseUsuarioDto>> listarUsuarios() {
        List<ResponseUsuarioDto> response = service.listarUsuarios();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alunos")
    public ResponseEntity<List<ResponseUsuarioDto>> listarAlunos() {
        List<ResponseUsuarioDto> response = service.listarAlunos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professores")
    public ResponseEntity<List<ResponseUsuarioDto>> listarProfessores() {
        List<ResponseUsuarioDto> response = service.listarProfessores();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUsuarioDto> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid RequestUpdateUsuarioDto dto) {
        ResponseUsuarioDto response = service.atualizarUsuario(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable Long id) {
        service.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Integer> inativarUsuario(@PathVariable Long id) {
        int result = service.inativarUsuario(id);
        return ResponseEntity.ok(result);
    }
}