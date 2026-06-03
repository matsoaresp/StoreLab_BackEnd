package com.unifil.appstore.controller;

import com.unifil.appstore.dto.request.RequestUsuarioDto;
import com.unifil.appstore.dto.response.ResponseUsuarioDto;
import com.unifil.appstore.models.usuario.Usuario;
import com.unifil.appstore.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {


    @Autowired
    private UsuarioService service;

    @PostMapping(value = "/aluno")
    public ResponseEntity<Usuario> criarAluno (
            @RequestBody RequestUsuarioDto dto){
        Usuario usuario = service.criarAluno(dto);
        return ResponseEntity.status(201).body(usuario);
    }

    @PostMapping(value = "/professor")
    public ResponseEntity<Usuario> criarProfessor (
            @RequestBody RequestUsuarioDto dto){
        Usuario usuario = service.criarProfesssor(dto);
        return ResponseEntity.status(201).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> encontrarUsuario(
            @PathVariable Long id) throws Exception {
        Usuario usuario = service.encontrarUsuario(id);
        return ResponseEntity.ok().body(usuario);
    }

    @GetMapping("/todos")
    ResponseEntity<List<Usuario>> ListarUsuarios (){
        List<Usuario> usuario = service.listarUsuarios();
        return ResponseEntity.ok().body(usuario);
    }

    @PutMapping("/atualizar/{id}")
    ResponseEntity<ResponseUsuarioDto> atualizarUsuario (
            @PathVariable Long id,
            @RequestBody RequestUsuarioDto dto
    ) throws Exception {
        ResponseUsuarioDto usuario = service.atualizarDados(id, dto);
        return ResponseEntity.ok().body(usuario);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deletarUsuario (@PathVariable Long id) throws Exception {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();

    }
}
