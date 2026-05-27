package com.unifil.appstore.service;

import com.unifil.appstore.models.entity.Usuario;
import com.unifil.appstore.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;


    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    private Usuario criarUsuario ()
}
