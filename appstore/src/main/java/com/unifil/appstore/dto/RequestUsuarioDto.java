package com.unifil.appstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUsuarioDto {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String matricula;

    @NotBlank
    private String senha;
}
