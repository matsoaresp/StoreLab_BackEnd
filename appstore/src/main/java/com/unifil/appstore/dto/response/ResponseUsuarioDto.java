package com.unifil.appstore.dto.response;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUsuarioDto {


    private String nome;

    public ResponseUsuarioDto(String nome, String matricula, String email) {
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
    }

    private String email;
    private String matricula;
}
